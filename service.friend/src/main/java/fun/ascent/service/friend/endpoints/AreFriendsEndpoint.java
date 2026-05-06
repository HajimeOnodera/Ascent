package fun.ascent.service.friend.endpoints;

import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.objects.friend.AreFriendsProtocolObject;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.service.friend.FriendCache;

public class AreFriendsEndpoint implements ServiceEndpoint<AreFriendsProtocolObject.AreFriendsMessage, AreFriendsProtocolObject.AreFriendsResponse> {
    @Override
    public ProtocolObject<AreFriendsProtocolObject.AreFriendsMessage, AreFriendsProtocolObject.AreFriendsResponse> associatedProtocolObject() {
        return new AreFriendsProtocolObject();
    }

    @Override
    public AreFriendsProtocolObject.AreFriendsResponse onMessage(ServiceProxyRequest request, AreFriendsProtocolObject.AreFriendsMessage message) {
        return new AreFriendsProtocolObject.AreFriendsResponse(FriendCache.areFriends(message.player1(), message.player2()));
    }
}
