package fun.ascent.common.party.events;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyPlayerRejoinEvent extends PartyEvent {
    private final UUID player;

    public PartyPlayerRejoinEvent(UUID player) {
        super(null);
        this.player = player;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<PartyPlayerRejoinEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyPlayerRejoinEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                return json.toString();
            }

            @Override
            public PartyPlayerRejoinEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID player = UUID.fromString(jsonObject.getString("player"));
                return new PartyPlayerRejoinEvent(player);
            }

            @Override
            public PartyPlayerRejoinEvent clone(PartyPlayerRejoinEvent value) {
                return new PartyPlayerRejoinEvent(value.player);
            }
        };
    }
}
