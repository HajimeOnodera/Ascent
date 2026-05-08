package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyWarpOverviewResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID leader;
    private final List<UUID> onlinePlayers;
    private final List<UUID> offlinePlayers;

    public PartyWarpOverviewResponseEvent(FullParty party, UUID leader, List<UUID> onlinePlayers, List<UUID> offlinePlayers) {
        super(party);
        this.party = party;
        this.leader = leader;
        this.onlinePlayers = onlinePlayers;
        this.offlinePlayers = offlinePlayers;
    }

    @Override
    public Serializer<PartyWarpOverviewResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyWarpOverviewResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("leader", value.getLeader().toString());
                JSONArray onlineArray = new JSONArray();
                for (UUID uuid : value.getOnlinePlayers()) {
                    onlineArray.put(uuid.toString());
                }
                json.put("onlinePlayers", onlineArray);
                JSONArray offlineArray = new JSONArray();
                for (UUID uuid : value.getOfflinePlayers()) {
                    offlineArray.put(uuid.toString());
                }
                json.put("offlinePlayers", offlineArray);
                return json.toString();
            }

            @Override
            public PartyWarpOverviewResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID leader = UUID.fromString(jsonObject.getString("leader"));
                List<UUID> onlinePlayers = jsonObject.getJSONArray("onlinePlayers").toList().stream()
                        .map(obj -> UUID.fromString(obj.toString())).toList();
                List<UUID> offlinePlayers = jsonObject.getJSONArray("offlinePlayers").toList().stream()
                        .map(obj -> UUID.fromString(obj.toString())).toList();
                return new PartyWarpOverviewResponseEvent(party, leader, onlinePlayers, offlinePlayers);
            }

            @Override
            public PartyWarpOverviewResponseEvent clone(PartyWarpOverviewResponseEvent value) {
                return new PartyWarpOverviewResponseEvent(value.getParty(), value.getLeader(), value.getOnlinePlayers(), value.getOfflinePlayers());
            }
        };
    }
}
