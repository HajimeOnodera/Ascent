package fun.ascent.service.party.endpoints;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.party.events.*;
import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.objects.party.SendPartyEventToServiceProtocolObject;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.service.party.PartyCache;

public class PartyEventToServiceEndpoint implements ServiceEndpoint<SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage, SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse> {
    @Override
    public ProtocolObject<SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage, SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse> associatedProtocolObject() {
        return new SendPartyEventToServiceProtocolObject();
    }

    @Override
    public SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse onMessage(ServiceProxyRequest request, SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage message) {
        PartyEvent event = message.event();

        if (event instanceof PartyInviteEvent inviteEvent) {
            PartyCache.handleInvite(inviteEvent);
        } else if (event instanceof PartyAcceptInviteEvent acceptEvent) {
            PartyCache.handleAcceptInvite(acceptEvent);
        } else if (event instanceof PartyLeaveRequestEvent leaveEvent) {
            PartyCache.handleLeaveRequest(leaveEvent);
        } else if (event instanceof PartyDisbandRequestEvent disbandEvent) {
            PartyCache.handleDisbandRequest(disbandEvent);
        } else if (event instanceof PartyKickRequestEvent kickEvent) {
            PartyCache.handleKickRequest(kickEvent);
        } else if (event instanceof PartyTransferRequestEvent transferEvent) {
            PartyCache.handleTransferRequest(transferEvent);
        } else if (event instanceof PartyPromoteRequestEvent promoteEvent) {
            PartyCache.handlePromoteRequest(promoteEvent);
        } else if (event instanceof PartyDemoteRequestEvent demoteEvent) {
            PartyCache.handleDemoteRequest(demoteEvent);
        } else if (event instanceof PartyWarpRequestEvent warpEvent) {
            PartyCache.handleWarpRequest(warpEvent);
        } else if (event instanceof PartyChatMessageEvent chatEvent) {
            PartyCache.handleChatMessage(chatEvent);
        } else if (event instanceof PartyListRequestEvent listEvent) {
            PartyCache.handleListRequest(listEvent);
        } else if (event instanceof PartyPlayerDisconnectEvent disconnectEvent) {
            PartyCache.handlePlayerDisconnect(disconnectEvent);
        } else if (event instanceof PartyPlayerRejoinEvent rejoinEvent) {
            PartyCache.handlePlayerRejoin(rejoinEvent);
        } else if (event instanceof PartyPlayerSwitchedServerEvent switchEvent) {
            PartyCache.handlePlayerSwitchedServer(switchEvent);
        }

        return new SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse(true);
    }
}
