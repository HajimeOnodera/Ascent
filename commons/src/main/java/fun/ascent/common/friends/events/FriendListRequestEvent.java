package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class FriendListRequestEvent extends FriendEvent {
    private final UUID player;
    private final int page;
    private final boolean bestOnly;
    private final Map<UUID, String> playerNames;
    private final Map<UUID, Boolean> onlineStatus;

    public FriendListRequestEvent(UUID player, int page, boolean bestOnly, Map<UUID, String> playerNames, Map<UUID, Boolean> onlineStatus) {
        super();
        this.player = player;
        this.page = page;
        this.bestOnly = bestOnly;
        this.playerNames = playerNames != null ? playerNames : new HashMap<>();
        this.onlineStatus = onlineStatus != null ? onlineStatus : new HashMap<>();
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendListRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendListRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("page", value.page);
                json.put("bestOnly", value.bestOnly);
                
                JSONObject namesJson = new JSONObject();
                value.playerNames.forEach((u, n) -> namesJson.put(u.toString(), n));
                json.put("playerNames", namesJson);
                
                JSONObject onlineJson = new JSONObject();
                value.onlineStatus.forEach((u, o) -> onlineJson.put(u.toString(), o));
                json.put("onlineStatus", onlineJson);
                
                return json.toString();
            }

            @Override
            public FriendListRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                Map<UUID, String> names = new HashMap<>();
                JSONObject namesJson = jsonObject.optJSONObject("playerNames");
                if (namesJson != null) {
                    namesJson.keySet().forEach(k -> names.put(UUID.fromString(k), namesJson.getString(k)));
                }
                
                Map<UUID, Boolean> online = new HashMap<>();
                JSONObject onlineJson = jsonObject.optJSONObject("onlineStatus");
                if (onlineJson != null) {
                    onlineJson.keySet().forEach(k -> online.put(UUID.fromString(k), onlineJson.getBoolean(k)));
                }

                return new FriendListRequestEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        jsonObject.getInt("page"),
                        jsonObject.getBoolean("bestOnly"),
                        names,
                        online
                );
            }

            @Override
            public FriendListRequestEvent clone(FriendListRequestEvent value) {
                return new FriendListRequestEvent(value.player, value.page, value.bestOnly, value.playerNames, value.onlineStatus);
            }
        };
    }
}
