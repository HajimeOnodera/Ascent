package fun.ascent.common.service;

import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.service.redis.ServerOutboundMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public record ProxyService(ServiceType type) {

    private static final int REQUEST_TIMEOUT_SECONDS = 15;

    public <T, R> void handleRequest(T request) {
        ProtocolObject<T, R> protocolObject = (ProtocolObject<T, R>) ServerOutboundMessage.protocolObjects.get(request.getClass().getSimpleName());

        if (protocolObject == null) {
            System.err.println("[ProxyService] Unknown protocol object for " + request.getClass().getSimpleName());
            return;
        }

        CompletableFuture<R> future = new CompletableFuture<>();
        ServerOutboundMessage.sendMessageToService(
            type,
            protocolObject,
            request,
            (s) -> future.complete(protocolObject.translateReturnFromString(s))
        );

        future.orTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
              .exceptionally(ex -> {
                  System.err.println("[ProxyService] Request timed out for "
                          + request.getClass().getSimpleName() + " (" + type + "): " + ex.getMessage());
                  return null;
              });
    }
}
