package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendSetNicknameRequestEvent extends FriendEvent {
    private final UUID player;
    private final UUID target;
    private final String targetName;
    private final String nickname;

    public FriendSetNicknameRequestEvent(UUID player, UUID target, String targetName, String nickname) {
        super();
        this.player = player;
        this.target = target;
        this.targetName = targetName;
        this.nickname = nickname;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player, target);
    }

    @Override
    public Serializer<FriendSetNicknameRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendSetNicknameRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("target", value.target.toString());
                json.put("targetName", value.targetName);
                json.put("nickname", value.nickname == null ? "" : value.nickname);
                return json.toString();
            }

            @Override
            public FriendSetNicknameRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendSetNicknameRequestEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("targetName"),
                        jsonObject.optString("nickname", "")
                );
            }

            @Override
            public FriendSetNicknameRequestEvent clone(FriendSetNicknameRequestEvent value) {
                return new FriendSetNicknameRequestEvent(value.player, value.target, value.targetName, value.nickname);
            }
        };
    }
}
