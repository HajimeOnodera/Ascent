package fun.ascent.common.party.events;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyWarpRequestEvent extends PartyEvent {
    private final UUID leader;

    public PartyWarpRequestEvent(UUID leader) {
        super(null);
        this.leader = leader;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(leader);
    }

    @Override
    public Serializer<PartyWarpRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyWarpRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("leader", value.leader.toString());
                return json.toString();
            }

            @Override
            public PartyWarpRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID leader = UUID.fromString(jsonObject.getString("leader"));
                return new PartyWarpRequestEvent(leader);
            }

            @Override
            public PartyWarpRequestEvent clone(PartyWarpRequestEvent value) {
                return new PartyWarpRequestEvent(value.leader);
            }
        };
    }
}
