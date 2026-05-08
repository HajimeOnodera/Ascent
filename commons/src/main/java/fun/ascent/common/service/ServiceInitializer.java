package fun.ascent.common.service;

import fun.ascent.common.redis.RedisConfig;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.common.service.redis.ServiceRedisManager;
import fun.ascent.common.protocol.ProtocolObject;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceInitializer {
    private static final String KEY_PREFIX = "ascent:services:";
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

        endpoints.forEach(endpoint -> {
            ProtocolObject protocolObject = endpoint.associatedProtocolObject();
            System.out.println("Registering channel " + protocolObject.channel());

            ServiceRedisManager.registerChannel(protocolObject.channel(), message -> {
                String[] split = message.split(";");
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
                    Object rawResponse = endpoint.onMessage(request, data);
                    String response = protocolObject.translateReturnToString(rawResponse);

                    // Send response back to requester's private channel
                    ServiceRedisManager.publish(requesterServer, requestId + ";" + response);
                });
            });
        });

        // Start ping
        startPing();

        System.out.println("Service " + service.getType().name() + " initialized!");

        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

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
}
