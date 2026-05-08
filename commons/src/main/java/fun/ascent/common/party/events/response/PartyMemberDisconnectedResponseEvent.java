package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyMemberDisconnectedResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID player;
    private final long timeoutSeconds;

    public PartyMemberDisconnectedResponseEvent(FullParty party, UUID player, long timeoutSeconds) {
        super(party);
        this.party = party;
        this.player = player;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public Serializer<PartyMemberDisconnectedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyMemberDisconnectedResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("player", value.getPlayer().toString());
                json.put("timeoutSeconds", value.getTimeoutSeconds());
                return json.toString();
            }

            @Override
            public PartyMemberDisconnectedResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID player = UUID.fromString(jsonObject.getString("player"));
                long timeoutSeconds = jsonObject.getLong("timeoutSeconds");
                return new PartyMemberDisconnectedResponseEvent(party, player, timeoutSeconds);
            }

            @Override
            public PartyMemberDisconnectedResponseEvent clone(PartyMemberDisconnectedResponseEvent value) {
                return new PartyMemberDisconnectedResponseEvent(value.getParty(), value.getPlayer(), value.getTimeoutSeconds());
            }
        };
    }
}
