package fun.ascent.common.party.events;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyChatMessageEvent extends PartyEvent {
    private final UUID sender;
    private final String message;

    public PartyChatMessageEvent(UUID sender, String message) {
        super(null);
        this.sender = sender;
        this.message = message;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender);
    }

    @Override
    public Serializer<PartyChatMessageEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyChatMessageEvent value) {
                JSONObject json = new JSONObject();
                json.put("sender", value.sender.toString());
                json.put("message", value.message);
                return json.toString();
            }

            @Override
            public PartyChatMessageEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID sender = UUID.fromString(jsonObject.getString("sender"));
                String message = jsonObject.getString("message");
                return new PartyChatMessageEvent(sender, message);
            }

            @Override
            public PartyChatMessageEvent clone(PartyChatMessageEvent value) {
                return new PartyChatMessageEvent(value.sender, value.message);
            }
        };
    }
}
