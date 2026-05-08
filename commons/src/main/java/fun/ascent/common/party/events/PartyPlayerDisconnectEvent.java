package fun.ascent.common.party.events;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyPlayerDisconnectEvent extends PartyEvent {
    private final UUID player;

    public PartyPlayerDisconnectEvent(UUID player) {
        super(null);
        this.player = player;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<PartyPlayerDisconnectEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyPlayerDisconnectEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                return json.toString();
            }

            @Override
            public PartyPlayerDisconnectEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID player = UUID.fromString(jsonObject.getString("player"));
                return new PartyPlayerDisconnectEvent(player);
            }

            @Override
            public PartyPlayerDisconnectEvent clone(PartyPlayerDisconnectEvent value) {
                return new PartyPlayerDisconnectEvent(value.player);
            }
        };
    }
}
