package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendPlayerLeaveEvent extends FriendEvent {
    private final UUID playerUuid;
    private final String playerName;

    public FriendPlayerLeaveEvent(UUID playerUuid, String playerName) {
        super();
        this.playerUuid = playerUuid;
        this.playerName = playerName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(playerUuid);
    }

    @Override
    public Serializer<FriendPlayerLeaveEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendPlayerLeaveEvent value) {
                JSONObject json = new JSONObject();
                json.put("playerUuid", value.playerUuid.toString());
                json.put("playerName", value.playerName);
                return json.toString();
            }

            @Override
            public FriendPlayerLeaveEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendPlayerLeaveEvent(
                        UUID.fromString(jsonObject.getString("playerUuid")),
                        jsonObject.getString("playerName")
                );
            }

            @Override
            public FriendPlayerLeaveEvent clone(FriendPlayerLeaveEvent value) {
                return new FriendPlayerLeaveEvent(value.playerUuid, value.playerName);
            }
        };
    }
}
