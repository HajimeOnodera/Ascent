package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyWarpResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID leader;

    public PartyWarpResponseEvent(FullParty party, UUID leader) {
        super(party);
        this.party = party;
        this.leader = leader;
    }

    @Override
    public Serializer<PartyWarpResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyWarpResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("leader", value.getLeader().toString());
                return json.toString();
            }

            @Override
            public PartyWarpResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID leader = UUID.fromString(jsonObject.getString("leader"));
                return new PartyWarpResponseEvent(party, leader);
            }

            @Override
            public PartyWarpResponseEvent clone(PartyWarpResponseEvent value) {
                return new PartyWarpResponseEvent(value.getParty(), value.getLeader());
            }
        };
    }
}
