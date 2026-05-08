package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyPlayerSwitchedServerResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID player;

    public PartyPlayerSwitchedServerResponseEvent(FullParty party, UUID player) {
        super(party);
        this.party = party;
        this.player = player;
    }

    @Override
    public Serializer<PartyPlayerSwitchedServerResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyPlayerSwitchedServerResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("player", value.getPlayer().toString());
                return json.toString();
            }

            @Override
            public PartyPlayerSwitchedServerResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID player = UUID.fromString(jsonObject.getString("player"));
                return new PartyPlayerSwitchedServerResponseEvent(party, player);
            }

            @Override
            public PartyPlayerSwitchedServerResponseEvent clone(PartyPlayerSwitchedServerResponseEvent value) {
                return new PartyPlayerSwitchedServerResponseEvent(value.getParty(), value.getPlayer());
            }
        };
    }
}
