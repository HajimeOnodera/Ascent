package fun.ascent.common.service;

import fun.ascent.common.redis.RedisConfig;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.common.service.redis.ServiceRedisManager;
import fun.ascent.common.protocol.ProtocolObject;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class ServiceInitializer {
    private final SkyBlockService service;

    public void init() {
        System.out.println("Initializing service " + service.getType().name() + "...");

        /**
         * Register Redis
         */
        ServiceRedisManager.connect(RedisConfig.fromEnv());

        List<ServiceEndpoint> endpoints = service.getEndpoints();

        endpoints.forEach(endpoint -> {
            ProtocolObject protocolObject = endpoint.associatedProtocolObject();
            System.out.println("Registering channel " + protocolObject.channel());

            ServiceRedisManager.registerChannel(protocolObject.channel(), message -> {
                // message format: requesterServer;requestId;messageData
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

        System.out.println("Service " + service.getType().name() + " initialized!");

        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
