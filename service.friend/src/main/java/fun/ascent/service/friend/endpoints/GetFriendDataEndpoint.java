package fun.ascent.service.friend.endpoints;

import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.objects.friend.GetFriendDataProtocolObject;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.service.friend.FriendCache;

public class GetFriendDataEndpoint implements ServiceEndpoint<GetFriendDataProtocolObject.GetFriendDataMessage, GetFriendDataProtocolObject.GetFriendDataResponse> {
    @Override
    public ProtocolObject<GetFriendDataProtocolObject.GetFriendDataMessage, GetFriendDataProtocolObject.GetFriendDataResponse> associatedProtocolObject() {
        return new GetFriendDataProtocolObject();
    }

    @Override
    public GetFriendDataProtocolObject.GetFriendDataResponse onMessage(ServiceProxyRequest request, GetFriendDataProtocolObject.GetFriendDataMessage message) {
        return new GetFriendDataProtocolObject.GetFriendDataResponse(FriendCache.getFriendData(message.playerUuid()));
    }
}
