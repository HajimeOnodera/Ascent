package fun.ascent.service.party.endpoints;

import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.objects.party.GetPartyProtocolObject;
import fun.ascent.common.service.impl.ServiceProxyRequest;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.service.party.PartyCache;

public class GetPartyEndpoint implements ServiceEndpoint<GetPartyProtocolObject.GetPartyMessage, GetPartyProtocolObject.GetPartyResponse> {
    @Override
    public ProtocolObject<GetPartyProtocolObject.GetPartyMessage, GetPartyProtocolObject.GetPartyResponse> associatedProtocolObject() {
        return new GetPartyProtocolObject();
    }

    @Override
    public GetPartyProtocolObject.GetPartyResponse onMessage(ServiceProxyRequest request, GetPartyProtocolObject.GetPartyMessage message) {
        return new GetPartyProtocolObject.GetPartyResponse(PartyCache.getPartyByMember(message.memberUuid()));
    }
}
