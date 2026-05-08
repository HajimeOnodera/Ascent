package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyChatMessageResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID sender;
    private final String message;

    public PartyChatMessageResponseEvent(FullParty party, UUID sender, String message) {
        super(party);
        this.party = party;
        this.sender = sender;
        this.message = message;
    }

    @Override
    public Serializer<PartyChatMessageResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyChatMessageResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("sender", value.getSender().toString());
                json.put("message", value.getMessage());
                return json.toString();
            }

            @Override
            public PartyChatMessageResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID sender = UUID.fromString(jsonObject.getString("sender"));
                String message = jsonObject.getString("message");
                return new PartyChatMessageResponseEvent(party, sender, message);
            }

            @Override
            public PartyChatMessageResponseEvent clone(PartyChatMessageResponseEvent value) {
                return new PartyChatMessageResponseEvent(value.getParty(), value.getSender(), value.getMessage());
            }
        };
    }
}
