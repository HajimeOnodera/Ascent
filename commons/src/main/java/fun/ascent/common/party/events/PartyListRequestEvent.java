package fun.ascent.common.party.events;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyListRequestEvent extends PartyEvent {
    private final UUID player;

    public PartyListRequestEvent(UUID player) {
        super(null);
        this.player = player;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<PartyListRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyListRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                return json.toString();
            }

            @Override
            public PartyListRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID player = UUID.fromString(jsonObject.getString("player"));
                return new PartyListRequestEvent(player);
            }

            @Override
            public PartyListRequestEvent clone(PartyListRequestEvent value) {
                return new PartyListRequestEvent(value.player);
            }
        };
    }
}
