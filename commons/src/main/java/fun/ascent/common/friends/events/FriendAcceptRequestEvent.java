package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendAcceptRequestEvent extends FriendEvent {
    private final UUID accepter;
    private final UUID requester;
    private final String accepterName;
    private final String requesterName;

    public FriendAcceptRequestEvent(UUID accepter, UUID requester, String accepterName, String requesterName) {
        super();
        this.accepter = accepter;
        this.requester = requester;
        this.accepterName = accepterName;
        this.requesterName = requesterName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(accepter, requester);
    }

    @Override
    public Serializer<FriendAcceptRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendAcceptRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("accepter", value.accepter.toString());
                json.put("requester", value.requester.toString());
                json.put("accepterName", value.accepterName);
                json.put("requesterName", value.requesterName);
                return json.toString();
            }

            @Override
            public FriendAcceptRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendAcceptRequestEvent(
                        UUID.fromString(jsonObject.getString("accepter")),
                        UUID.fromString(jsonObject.getString("requester")),
                        jsonObject.getString("accepterName"),
                        jsonObject.getString("requesterName")
                );
            }

            @Override
            public FriendAcceptRequestEvent clone(FriendAcceptRequestEvent value) {
                return new FriendAcceptRequestEvent(value.accepter, value.requester, value.accepterName, value.requesterName);
            }
        };
    }
}
