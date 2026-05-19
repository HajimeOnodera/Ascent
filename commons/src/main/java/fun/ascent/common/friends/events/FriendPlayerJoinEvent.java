package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendPlayerJoinEvent extends FriendEvent {
    private final UUID playerUuid;
    private final String playerName;

    public FriendPlayerJoinEvent(UUID playerUuid, String playerName) {
        super();
        this.playerUuid = playerUuid;
        this.playerName = playerName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(playerUuid);
    }

    @Override
    public Serializer<FriendPlayerJoinEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendPlayerJoinEvent value) {
                JSONObject json = new JSONObject();
                json.put("playerUuid", value.playerUuid.toString());
                json.put("playerName", value.playerName);
                return json.toString();
            }

            @Override
            public FriendPlayerJoinEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendPlayerJoinEvent(
                        UUID.fromString(jsonObject.getString("playerUuid")),
                        jsonObject.getString("playerName")
                );
            }

            @Override
            public FriendPlayerJoinEvent clone(FriendPlayerJoinEvent value) {
                return new FriendPlayerJoinEvent(value.playerUuid, value.playerName);
            }
        };
    }
}
