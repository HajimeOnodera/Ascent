package fun.ascent.service.friend.endpoints;

import fun.ascent.common.friends.*;
import fun.ascent.common.friends.events.*;
import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.objects.friend.SendFriendEventToServiceProtocolObject;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.service.friend.FriendCache;

public class FriendEventToServiceEndpoint implements ServiceEndpoint<SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage, SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse> {
    @Override
    public ProtocolObject<SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage, SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse> associatedProtocolObject() {
        return new SendFriendEventToServiceProtocolObject();
    }

    @Override
    public SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse onMessage(ServiceProxyRequest request, SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage message) {
        FriendEvent event = message.event();

        if (event instanceof FriendAddRequestEvent addEvent) {
            FriendCache.handleAddRequest(addEvent, addEvent.getSenderName(), addEvent.getTargetName());
        } else if (event instanceof FriendAcceptRequestEvent acceptEvent) {
            FriendCache.handleAcceptRequest(acceptEvent, acceptEvent.getAccepterName(), acceptEvent.getRequesterName());
        } else if (event instanceof FriendDenyRequestEvent denyEvent) {
            FriendCache.handleDenyRequest(denyEvent, denyEvent.getDenierName());
        } else if (event instanceof FriendRemoveRequestEvent removeEvent) {
            FriendCache.handleRemoveRequest(removeEvent, removeEvent.getRemoverName(), removeEvent.getTargetName());
        } else if (event instanceof FriendRemoveAllRequestEvent removeAllEvent) {
            FriendCache.handleRemoveAllRequest(removeAllEvent);
        } else if (event instanceof FriendToggleBestRequestEvent toggleBestEvent) {
            FriendCache.handleToggleBestRequest(toggleBestEvent, toggleBestEvent.getTargetName());
        } else if (event instanceof FriendSetNicknameRequestEvent setNicknameEvent) {
            FriendCache.handleSetNicknameRequest(setNicknameEvent, setNicknameEvent.getTargetName());
        } else if (event instanceof FriendToggleSettingRequestEvent toggleSettingEvent) {
            FriendCache.handleToggleSettingRequest(toggleSettingEvent);
        } else if (event instanceof FriendListRequestEvent listEvent) {
            FriendCache.handleListRequest(listEvent, listEvent.getPlayerNames(), listEvent.getOnlineStatus());
        } else if (event instanceof FriendRequestsListEvent requestsListEvent) {
            FriendCache.handleRequestsListRequest(requestsListEvent, requestsListEvent.getPlayerNames());
        } else if (event instanceof FriendPlayerJoinEvent joinEvent) {
            FriendCache.handlePlayerJoin(joinEvent.getPlayerUuid(), joinEvent.getPlayerName());
        } else if (event instanceof FriendPlayerLeaveEvent leaveEvent) {
            FriendCache.handlePlayerLeave(leaveEvent.getPlayerUuid(), leaveEvent.getPlayerName());
        }

        return new SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse(true);
    }
}
