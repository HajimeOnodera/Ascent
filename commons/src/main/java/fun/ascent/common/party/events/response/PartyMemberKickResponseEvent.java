package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyMemberKickResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID kicker;
    private final UUID kicked;

    public PartyMemberKickResponseEvent(FullParty party, UUID kicker, UUID kicked) {
        super(party);
        this.party = party;
        this.kicker = kicker;
        this.kicked = kicked;
    }

    @Override
    public Serializer<PartyMemberKickResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyMemberKickResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("kicker", value.getKicker().toString());
                json.put("kicked", value.getKicked().toString());
                return json.toString();
            }

            @Override
            public PartyMemberKickResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID kicker = UUID.fromString(jsonObject.getString("kicker"));
                UUID kicked = UUID.fromString(jsonObject.getString("kicked"));
                return new PartyMemberKickResponseEvent(party, kicker, kicked);
            }

            @Override
            public PartyMemberKickResponseEvent clone(PartyMemberKickResponseEvent value) {
                return new PartyMemberKickResponseEvent(value.getParty(), value.getKicker(), value.getKicked());
            }
        };
    }
}
