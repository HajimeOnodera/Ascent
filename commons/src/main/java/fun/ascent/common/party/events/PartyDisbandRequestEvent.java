package fun.ascent.common.party.events;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyDisbandRequestEvent extends PartyEvent {
    private final UUID leader;

    public PartyDisbandRequestEvent(UUID leader) {
        super(null);
        this.leader = leader;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(leader);
    }

    @Override
    public Serializer<PartyDisbandRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyDisbandRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("leader", value.leader.toString());
                return json.toString();
            }

            @Override
            public PartyDisbandRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID leader = UUID.fromString(jsonObject.getString("leader"));
                return new PartyDisbandRequestEvent(leader);
            }

            @Override
            public PartyDisbandRequestEvent clone(PartyDisbandRequestEvent value) {
                return new PartyDisbandRequestEvent(value.leader);
            }
        };
    }
}
