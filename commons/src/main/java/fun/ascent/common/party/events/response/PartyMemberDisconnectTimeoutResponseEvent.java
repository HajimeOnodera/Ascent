package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyMemberDisconnectTimeoutResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID player;
    private final boolean kicked;

    public PartyMemberDisconnectTimeoutResponseEvent(FullParty party, UUID player, boolean kicked) {
        super(party);
        this.party = party;
        this.player = player;
        this.kicked = kicked;
    }

    @Override
    public Serializer<PartyMemberDisconnectTimeoutResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyMemberDisconnectTimeoutResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("player", value.getPlayer().toString());
                json.put("kicked", value.isKicked());
                return json.toString();
            }

            @Override
            public PartyMemberDisconnectTimeoutResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID player = UUID.fromString(jsonObject.getString("player"));
                boolean kicked = jsonObject.getBoolean("kicked");
                return new PartyMemberDisconnectTimeoutResponseEvent(party, player, kicked);
            }

            @Override
            public PartyMemberDisconnectTimeoutResponseEvent clone(PartyMemberDisconnectTimeoutResponseEvent value) {
                return new PartyMemberDisconnectTimeoutResponseEvent(value.getParty(), value.getPlayer(), value.isKicked());
            }
        };
    }
}
