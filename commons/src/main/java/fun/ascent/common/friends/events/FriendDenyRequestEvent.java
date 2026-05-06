package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendDenyRequestEvent extends FriendEvent {
    private final UUID denier;
    private final UUID requester;
    private final String denierName;

    public FriendDenyRequestEvent(UUID denier, UUID requester, String denierName) {
        super();
        this.denier = denier;
        this.requester = requester;
        this.denierName = denierName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(denier, requester);
    }

    @Override
    public Serializer<FriendDenyRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendDenyRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("denier", value.denier.toString());
                json.put("requester", value.requester.toString());
                json.put("denierName", value.denierName);
                return json.toString();
            }

            @Override
            public FriendDenyRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendDenyRequestEvent(
                        UUID.fromString(jsonObject.getString("denier")),
                        UUID.fromString(jsonObject.getString("requester")),
                        jsonObject.getString("denierName")
                );
            }

            @Override
            public FriendDenyRequestEvent clone(FriendDenyRequestEvent value) {
                return new FriendDenyRequestEvent(value.denier, value.requester, value.denierName);
            }
        };
    }
}
