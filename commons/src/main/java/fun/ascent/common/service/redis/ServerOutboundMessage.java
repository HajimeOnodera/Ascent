package fun.ascent.common.service.redis;

import fun.ascent.common.redis.RedisManager;
import fun.ascent.common.service.ServiceType;
import fun.ascent.common.protocol.ProtocolObject;
import org.json.JSONObject;
import org.reflections.Reflections;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ServerOutboundMessage {
    public static final Map<String, ProtocolObject> protocolObjects = new ConcurrentHashMap<>();
    private static final Map<UUID, PendingRequest> pendingRequests = new ConcurrentHashMap<>();
    private static final Map<String, ServiceToClient> clientListeners = new ConcurrentHashMap<>();
    private static final String SERVER_ID = UUID.randomUUID().toString();
    private static volatile boolean listening = false;

    private static final long REQUEST_TIMEOUT_MS = 30_000;
    private static final long MAX_RECONNECT_DELAY_MS = 15_000;

    private static final ScheduledExecutorService cleanupScheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "ascent-request-cleanup");
                t.setDaemon(true);
                return t;
            });

    private record PendingRequest(Consumer<String> callback, long createdAt) {}

    static {
        Reflections reflections = new Reflections("fun.ascent.common.protocol.objects");
        Set<Class<? extends ProtocolObject>> classes = reflections.getSubTypesOf(ProtocolObject.class);
        for (Class<? extends ProtocolObject> clazz : classes) {
            try {
                ProtocolObject obj = clazz.getDeclaredConstructor().newInstance();
                protocolObjects.put(clazz.getSimpleName(), obj);
            } catch (Exception ignored) {}
        }
    }

    public static void registerClientListener(ServiceToClient listener) {
        clientListeners.put(listener.getChannel().getChannelName(), listener);
    }

    public static void init() {
        if (listening) return;
        listening = true;

        startPubSubListener();

        // Cleanup stale pending requests every 10 seconds
        cleanupScheduler.scheduleAtFixedRate(
                ServerOutboundMessage::cleanupStaleRequests, 10, 10, TimeUnit.SECONDS);
    }

    // ── Auto-reconnecting PubSub for responses & broadcasts ─────────────────

    private static void startPubSubListener() {
        Thread.startVirtualThread(() -> {
            int consecutiveFailures = 0;

            while (listening) {
                try (Jedis jedis = RedisManager.get().getResource()) {
                    consecutiveFailures = 0;

                    JedisPubSub pubSub = new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            try {
                                if (channel.equals(SERVER_ID)) {
                                    handleResponse(message);
                                } else if (channel.startsWith("service_broadcast_")) {
                                    handleBroadcast(channel, message);
                                }
                            } catch (Exception e) {
                                System.err.println("[Ascent] Error handling PubSub message: " + e.getMessage());
                            }
                        }
                    };

                    String[] channels = buildChannelArray();
                    System.out.println("[Ascent] PubSub listener connected (" + channels.length + " channels)");
                    jedis.subscribe(pubSub, channels);

                } catch (JedisException e) {
                    consecutiveFailures++;
                    long delay = Math.min(1000L * consecutiveFailures, MAX_RECONNECT_DELAY_MS);
                    System.err.println("[Ascent] PubSub connection lost: " + e.getMessage()
                            + " — reconnecting in " + delay + "ms...");
                    sleep(delay);
                } catch (Exception e) {
                    consecutiveFailures++;
                    long delay = Math.min(1000L * consecutiveFailures, MAX_RECONNECT_DELAY_MS);
                    System.err.println("[Ascent] PubSub error: " + e.getMessage()
                            + " — reconnecting in " + delay + "ms...");
                    sleep(delay);
                }
            }
        });
    }

    private static void handleResponse(String message) {
        String[] split = message.split(";", 2);
        if (split.length < 2) return;

        try {
            UUID requestId = UUID.fromString(split[0]);
            PendingRequest pending = pendingRequests.remove(requestId);
            if (pending != null) {
                pending.callback().accept(split[1]);
            }
        } catch (IllegalArgumentException ignored) {}
    }

    private static void handleBroadcast(String channel, String message) {
        String channelName = channel.substring("service_broadcast_".length());
        ServiceToClient listener = clientListeners.get(channelName);
        if (listener == null) return;

        try {
            String[] split = message.split(";", 3);
            if (split.length < 3) return;
            JSONObject json = new JSONObject(split[2]);
            listener.onMessage(json);
        } catch (Exception e) {
            System.err.println("[Ascent] Error processing broadcast on " + channelName + ": " + e.getMessage());
        }
    }

    private static String[] buildChannelArray() {
        String[] channels = new String[clientListeners.size() + 1];
        channels[0] = SERVER_ID;
        int i = 1;
        for (String chan : clientListeners.keySet()) {
            channels[i++] = "service_broadcast_" + chan;
        }
        return channels;
    }

    private static void cleanupStaleRequests() {
        long now = System.currentTimeMillis();
        pendingRequests.entrySet().removeIf(uuidPendingRequestEntry -> now - uuidPendingRequestEntry.getValue().createdAt() > REQUEST_TIMEOUT_MS);
    }

    // ── Queue-based message sending (persistent via Redis Lists) ────────────

    public static <T, R> void sendMessageToService(ServiceType type, ProtocolObject<T, R> protocol,
                                                     T message, Consumer<String> callback) {
        init();
        UUID requestId = UUID.randomUUID();
        pendingRequests.put(requestId, new PendingRequest(callback, System.currentTimeMillis()));

        String channel = protocol.channel();
        String serialized = protocol.translateToString(message);
        String fullMessage = SERVER_ID + ";" + requestId + ";" + serialized;
        String queueKey = "ascent:queue:" + channel;

        try (Jedis jedis = RedisManager.get().getResource()) {
            jedis.lpush(queueKey, fullMessage);
        } catch (JedisException e) {
            pendingRequests.remove(requestId);
            System.err.println("[Ascent] Failed to queue message for " + type + ": " + e.getMessage());
        }
    }

    // ── Utility ─────────────────────────────────────────────────────────────

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
