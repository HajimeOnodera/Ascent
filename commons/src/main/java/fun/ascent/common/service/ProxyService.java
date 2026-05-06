package fun.ascent.common.service;

import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.service.redis.ServerOutboundMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public record ProxyService(ServiceType type) {
    public CompletableFuture<Boolean> isOnline() {
        return CompletableFuture.completedFuture(true); // Simplified for now
    }

    public <T, R> void handleRequest(T request) {
        ProtocolObject<T, R> protocolObject = (ProtocolObject<T, R>) ServerOutboundMessage.protocolObjects.get(request.getClass().getSimpleName());

        if (protocolObject == null) {
            CompletableFuture.failedFuture(new IllegalArgumentException("Unknown protocol object for " + request.getClass().getSimpleName()));
            return;
        }

        CompletableFuture<R> future = new CompletableFuture<>();
        ServerOutboundMessage.sendMessageToService(
            type,
            protocolObject,
            request,
            (s) -> future.complete(protocolObject.translateReturnFromString(s))
        );

        future.orTimeout(5, TimeUnit.SECONDS);
    }
}
