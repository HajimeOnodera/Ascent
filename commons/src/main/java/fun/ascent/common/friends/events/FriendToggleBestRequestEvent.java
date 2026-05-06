package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendToggleBestRequestEvent extends FriendEvent {
    private final UUID player;
    private final UUID target;
    private final String targetName;

    public FriendToggleBestRequestEvent(UUID player, UUID target, String targetName) {
        super();
        this.player = player;
        this.target = target;
        this.targetName = targetName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player, target);
    }

    @Override
    public Serializer<FriendToggleBestRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendToggleBestRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("target", value.target.toString());
                json.put("targetName", value.targetName);
                return json.toString();
            }

            @Override
            public FriendToggleBestRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendToggleBestRequestEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("targetName")
                );
            }

            @Override
            public FriendToggleBestRequestEvent clone(FriendToggleBestRequestEvent value) {
                return new FriendToggleBestRequestEvent(value.player, value.target, value.targetName);
            }
        };
    }
}
