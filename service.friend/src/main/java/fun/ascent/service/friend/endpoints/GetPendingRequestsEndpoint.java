package fun.ascent.service.friend.endpoints;

import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.objects.friend.GetPendingFriendRequestsProtocolObject;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.service.friend.FriendCache;

public class GetPendingRequestsEndpoint implements ServiceEndpoint<GetPendingFriendRequestsProtocolObject.GetPendingRequestsMessage, GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse> {
    @Override
    public ProtocolObject<GetPendingFriendRequestsProtocolObject.GetPendingRequestsMessage, GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse> associatedProtocolObject() {
        return new GetPendingFriendRequestsProtocolObject();
    }

    @Override
    public GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse onMessage(ServiceProxyRequest request, GetPendingFriendRequestsProtocolObject.GetPendingRequestsMessage message) {
        return new GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse(FriendCache.getPendingRequestsFor(message.playerUuid()));
    }
}
