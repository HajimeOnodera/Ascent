package fun.ascent.common.service;

import fun.ascent.common.redis.RedisConfig;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.common.service.redis.ServiceRedisManager;
import fun.ascent.common.protocol.ProtocolObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceInitializer {
    private static final String KEY_PREFIX = "ascent:services:";
    private static final String QUEUE_PREFIX = "ascent:queue:";
    private static final int BRPOP_TIMEOUT_SECONDS = 5;
    private static final long MAX_RECONNECT_DELAY_MS = 15_000;

    private final SkyBlockService service;
    private final ScheduledExecutorService pingScheduler;

    public ServiceInitializer(SkyBlockService service) {
        this.service = service;
        this.pingScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "service-ping-" + service.getType().name().toLowerCase());
            t.setDaemon(true);
            return t;
        });
    }

    public void init() {
        System.out.println("Initializing service " + service.getType().name() + "...");

        ServiceRedisManager.connect(RedisConfig.fromEnv());

        List<ServiceEndpoint> endpoints = service.getEndpoints();

        // Start a queue consumer thread for each endpoint (BRPOP-based)
        endpoints.forEach(endpoint -> {
            ProtocolObject protocolObject = endpoint.associatedProtocolObject();
            String channel = protocolObject.channel();
            String queueKey = QUEUE_PREFIX + channel;

            System.out.println("Starting queue consumer for channel: " + channel + " (key: " + queueKey + ")");
            Thread.startVirtualThread(() -> consumeQueue(queueKey, endpoint, protocolObject));
        });

        // Start ping
        startPing();

        System.out.println("Service " + service.getType().name() + " initialized with " + endpoints.size() + " endpoint(s)!");

        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ── Queue consumer (BRPOP-based, auto-reconnecting) ────────────────────

    private void consumeQueue(String queueKey, ServiceEndpoint endpoint, ProtocolObject protocolObject) {
        int consecutiveFailures = 0;

        while (true) {
            try (Jedis jedis = ServiceRedisManager.getRedisManager().getResource()) {
                consecutiveFailures = 0;

                // BRPOP blocks until a message is available, with a timeout
                // to allow periodic health checks and graceful shutdown
                List<String> result = jedis.brpop(BRPOP_TIMEOUT_SECONDS, queueKey);
                if (result == null || result.size() < 2) continue;

                String message = result.get(1); // [0] = key, [1] = value
                processMessage(message, endpoint, protocolObject);

            } catch (JedisException e) {
                consecutiveFailures++;
                long delay = Math.min(1000L * consecutiveFailures, MAX_RECONNECT_DELAY_MS);
                System.err.println("[" + service.getType().name() + "] Queue consumer error: "
                        + e.getMessage() + " — retrying in " + delay + "ms...");
                sleep(delay);
            }
        }
    }

    private void processMessage(String message, ServiceEndpoint endpoint, ProtocolObject protocolObject) {
        String[] split = message.split(";", 3);
        if (split.length < 3) return;

        String requesterServer = split[0];
        String requestId = split[1];
        String messageData = split[2];

        ServiceProxyRequest request = new ServiceProxyRequest(
                java.util.UUID.fromString(requestId),
                requesterServer,
                protocolObject.channel(),
                messageData
        );

        Object data = protocolObject.translateFromString(messageData);

        Thread.startVirtualThread(() -> {
            try {
                Object rawResponse = endpoint.onMessage(request, data);
                String response = protocolObject.translateReturnToString(rawResponse);

                // Send response back to requester's private channel
                ServiceRedisManager.publish(requesterServer, requestId + ";" + response);
            } catch (Exception e) {
                System.err.println("[" + service.getType().name() + "] Error processing message on "
                        + protocolObject.channel() + ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    // ── Heartbeat ping ─────────────────────────────────────────────────────

    private void startPing() {
        String key = KEY_PREFIX + service.getType().name().toLowerCase();
        pingScheduler.scheduleAtFixedRate(() -> {
            try (Jedis jedis = ServiceRedisManager.getRedisManager().getResource()) {
                jedis.setex(key, 5, String.valueOf(System.currentTimeMillis()));
            } catch (Exception e) {
                System.err.println("Failed to send ping: " + e.getMessage());
            }
        }, 0, 3, TimeUnit.SECONDS);
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
