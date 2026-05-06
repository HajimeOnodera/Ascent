package fun.ascent.common.service.redis;

import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.service.impl.ServiceProxyRequest;

public interface ServiceEndpoint<T, R> {
    ProtocolObject<T, R> associatedProtocolObject();

    R onMessage(ServiceProxyRequest request, T message);
}
