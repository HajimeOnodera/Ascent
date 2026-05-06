package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendAddRequestEvent extends FriendEvent {
    private final UUID sender;
    private final UUID target;
    private final String senderName;
    private final String targetName;

    public FriendAddRequestEvent(UUID sender, UUID target, String senderName, String targetName) {
        super();
        this.sender = sender;
        this.target = target;
        this.senderName = senderName;
        this.targetName = targetName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender, target);
    }

    @Override
    public Serializer<FriendAddRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendAddRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("sender", value.sender.toString());
                json.put("target", value.target.toString());
                json.put("senderName", value.senderName);
                json.put("targetName", value.targetName);
                return json.toString();
            }

            @Override
            public FriendAddRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendAddRequestEvent(
                        UUID.fromString(jsonObject.getString("sender")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("senderName"),
                        jsonObject.getString("targetName")
                );
            }

            @Override
            public FriendAddRequestEvent clone(FriendAddRequestEvent value) {
                return new FriendAddRequestEvent(value.sender, value.target, value.senderName, value.targetName);
            }
        };
    }
}
