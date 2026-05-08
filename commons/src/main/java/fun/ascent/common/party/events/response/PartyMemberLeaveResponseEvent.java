package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyMemberLeaveResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID leaver;

    public PartyMemberLeaveResponseEvent(FullParty party, UUID leaver) {
        super(party);
        this.party = party;
        this.leaver = leaver;
    }

    @Override
    public Serializer<PartyMemberLeaveResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyMemberLeaveResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("leaver", value.getLeaver().toString());
                return json.toString();
            }

            @Override
            public PartyMemberLeaveResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID leaver = UUID.fromString(jsonObject.getString("leaver"));
                return new PartyMemberLeaveResponseEvent(party, leaver);
            }

            @Override
            public PartyMemberLeaveResponseEvent clone(PartyMemberLeaveResponseEvent value) {
                return new PartyMemberLeaveResponseEvent(value.getParty(), value.getLeaver());
            }
        };
    }
}
