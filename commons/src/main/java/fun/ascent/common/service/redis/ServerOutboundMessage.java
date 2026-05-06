package fun.ascent.common.service.redis;

import fun.ascent.common.redis.RedisManager;
import fun.ascent.common.service.ServiceType;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.protocol.ProtocolObject;
import org.json.JSONObject;
import org.reflections.Reflections;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ServerOutboundMessage {
    public static final Map<String, ProtocolObject> protocolObjects = new ConcurrentHashMap<>();
    private static final Map<UUID, Consumer<String>> pendingRequests = new ConcurrentHashMap<>();
    private static final Map<String, ServiceToClient> clientListeners = new ConcurrentHashMap<>();
    private static final String SERVER_ID = UUID.randomUUID().toString();
    private static boolean listening = false;

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

        Thread.startVirtualThread(() -> {
            try (Jedis jedis = RedisManager.get().getResource()) {
                JedisPubSub pubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        if (channel.equals(SERVER_ID)) {
                            String[] split = message.split(";");
                            if (split.length < 2) return;
                            UUID requestId = UUID.fromString(split[0]);
                            String data = split[1];

                            Consumer<String> consumer = pendingRequests.remove(requestId);
                            if (consumer != null) {
                                consumer.accept(data);
                            }
                        } else if (channel.startsWith("service_broadcast_")) {
                            String channelName = channel.substring("service_broadcast_".length());
                            ServiceToClient listener = clientListeners.get(channelName);
                            if (listener != null) {
                                // message format: ServiceType;requestId;JSONObject
                                String[] split = message.split(";");
                                if (split.length < 3) return;
                                JSONObject json = new JSONObject(split[2]);
                                listener.onMessage(json);
                            }
                        }
                    }
                };
                
                String[] channels = new String[clientListeners.size() + 1];
                channels[0] = SERVER_ID;
                int i = 1;
                for (String chan : clientListeners.keySet()) {
                    channels[i++] = "service_broadcast_" + chan;
                }
                
                jedis.subscribe(pubSub, channels);
            }
        });
    }

    public static <T, R> void sendMessageToService(ServiceType type, ProtocolObject<T, R> protocol, T message, Consumer<String> callback) {
        init();
        UUID requestId = UUID.randomUUID();
        pendingRequests.put(requestId, callback);

        String channel = protocol.channel();
        String serialized = protocol.translateToString(message);
        String fullMessage = SERVER_ID + ";" + requestId + ";" + serialized;

        try (Jedis jedis = RedisManager.get().getResource()) {
            jedis.publish(channel, fullMessage);
        }
    }
}
