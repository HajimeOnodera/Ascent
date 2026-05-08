package fun.ascent.common.party.events;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyPlayerSwitchedServerEvent extends PartyEvent {
    private final UUID player;

    public PartyPlayerSwitchedServerEvent(UUID player) {
        super(null);
        this.player = player;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<PartyPlayerSwitchedServerEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyPlayerSwitchedServerEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                return json.toString();
            }

            @Override
            public PartyPlayerSwitchedServerEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID player = UUID.fromString(jsonObject.getString("player"));
                return new PartyPlayerSwitchedServerEvent(player);
            }

            @Override
            public PartyPlayerSwitchedServerEvent clone(PartyPlayerSwitchedServerEvent value) {
                return new PartyPlayerSwitchedServerEvent(value.player);
            }
        };
    }
}
