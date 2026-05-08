package fun.ascent.service.party.endpoints;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.objects.party.IsPlayerInPartyProtocolObject;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.service.party.PartyCache;

public class IsPlayerInPartyEndpoint implements ServiceEndpoint<IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage, IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse> {
    @Override
    public ProtocolObject<IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage, IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse> associatedProtocolObject() {
        return new IsPlayerInPartyProtocolObject();
    }

    @Override
    public IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse onMessage(ServiceProxyRequest request, IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage message) {
        FullParty party = PartyCache.getPartyByMember(message.playerUuid());
        if (party == null) {
            return new IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse(false, null);
        }
        return new IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse(true, party.getUuid());
    }
}
