package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyDisbandResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID leader;
    private final String reason;

    public PartyDisbandResponseEvent(FullParty party, UUID leader, String reason) {
        super(party);
        this.party = party;
        this.leader = leader;
        this.reason = reason;
    }

    @Override
    public Serializer<PartyDisbandResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyDisbandResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("leader", value.getLeader().toString());
                json.put("reason", value.getReason());
                return json.toString();
            }

            @Override
            public PartyDisbandResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID leader = UUID.fromString(jsonObject.getString("leader"));
                String reason = jsonObject.getString("reason");
                return new PartyDisbandResponseEvent(party, leader, reason);
            }

            @Override
            public PartyDisbandResponseEvent clone(PartyDisbandResponseEvent value) {
                return new PartyDisbandResponseEvent(value.getParty(), value.getLeader(), value.getReason());
            }
        };
    }
}
