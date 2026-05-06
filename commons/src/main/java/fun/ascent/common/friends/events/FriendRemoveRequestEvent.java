package fun.ascent.common.friends.events;

import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRemoveRequestEvent extends FriendEvent {
    private final UUID remover;
    private final UUID target;
    private final String removerName;
    private final String targetName;

    public FriendRemoveRequestEvent(UUID remover, UUID target, String removerName, String targetName) {
        super();
        this.remover = remover;
        this.target = target;
        this.removerName = removerName;
        this.targetName = targetName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(remover, target);
    }

    @Override
    public Serializer<FriendRemoveRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRemoveRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("remover", value.remover.toString());
                json.put("target", value.target.toString());
                json.put("removerName", value.removerName);
                json.put("targetName", value.targetName);
                return json.toString();
            }

            @Override
            public FriendRemoveRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRemoveRequestEvent(
                        UUID.fromString(jsonObject.getString("remover")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("removerName"),
                        jsonObject.getString("targetName")
                );
            }

            @Override
            public FriendRemoveRequestEvent clone(FriendRemoveRequestEvent value) {
                return new FriendRemoveRequestEvent(value.remover, value.target, value.removerName, value.targetName);
            }
        };
    }
}
