package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyMemberRejoinedResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID player;

    public PartyMemberRejoinedResponseEvent(FullParty party, UUID player) {
        super(party);
        this.party = party;
        this.player = player;
    }

    @Override
    public Serializer<PartyMemberRejoinedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyMemberRejoinedResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("player", value.getPlayer().toString());
                return json.toString();
            }

            @Override
            public PartyMemberRejoinedResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID player = UUID.fromString(jsonObject.getString("player"));
                return new PartyMemberRejoinedResponseEvent(party, player);
            }

            @Override
            public PartyMemberRejoinedResponseEvent clone(PartyMemberRejoinedResponseEvent value) {
                return new PartyMemberRejoinedResponseEvent(value.getParty(), value.getPlayer());
            }
        };
    }
}
