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
public class FriendRequestsListEvent extends FriendEvent {
    private final UUID player;
    private final int page;
    private final Map<UUID, String> playerNames;

    public FriendRequestsListEvent(UUID player, int page, Map<UUID, String> playerNames) {
        super();
        this.player = player;
        this.page = page;
        this.playerNames = playerNames != null ? playerNames : new HashMap<>();
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendRequestsListEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRequestsListEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("page", value.page);
                
                JSONObject namesJson = new JSONObject();
                value.playerNames.forEach((u, n) -> namesJson.put(u.toString(), n));
                json.put("playerNames", namesJson);
                
                return json.toString();
            }

            @Override
            public FriendRequestsListEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                Map<UUID, String> names = new HashMap<>();
                JSONObject namesJson = jsonObject.optJSONObject("playerNames");
                if (namesJson != null) {
                    namesJson.keySet().forEach(k -> names.put(UUID.fromString(k), namesJson.getString(k)));
                }

                return new FriendRequestsListEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        jsonObject.getInt("page"),
                        names
                );
            }

            @Override
            public FriendRequestsListEvent clone(FriendRequestsListEvent value) {
                return new FriendRequestsListEvent(value.player, value.page, value.playerNames);
            }
        };
    }
}
