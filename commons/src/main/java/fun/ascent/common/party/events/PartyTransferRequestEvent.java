package fun.ascent.common.party.events;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyTransferRequestEvent extends PartyEvent {
    private final UUID leader;
    private final UUID target;

    public PartyTransferRequestEvent(UUID leader, UUID target) {
        super(null);
        this.leader = leader;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(leader, target);
    }

    @Override
    public Serializer<PartyTransferRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyTransferRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("leader", value.leader.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public PartyTransferRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID leader = UUID.fromString(jsonObject.getString("leader"));
                UUID target = UUID.fromString(jsonObject.getString("target"));
                return new PartyTransferRequestEvent(leader, target);
            }

            @Override
            public PartyTransferRequestEvent clone(PartyTransferRequestEvent value) {
                return new PartyTransferRequestEvent(value.leader, value.target);
            }
        };
    }
}
