package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyLeaderTransferResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID oldLeader;
    private final UUID newLeader;

    public PartyLeaderTransferResponseEvent(FullParty party, UUID oldLeader, UUID newLeader) {
        super(party);
        this.party = party;
        this.oldLeader = oldLeader;
        this.newLeader = newLeader;
    }

    @Override
    public Serializer<PartyLeaderTransferResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyLeaderTransferResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("oldLeader", value.getOldLeader().toString());
                json.put("newLeader", value.getNewLeader().toString());
                return json.toString();
            }

            @Override
            public PartyLeaderTransferResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID oldLeader = UUID.fromString(jsonObject.getString("oldLeader"));
                UUID newLeader = UUID.fromString(jsonObject.getString("newLeader"));
                return new PartyLeaderTransferResponseEvent(party, oldLeader, newLeader);
            }

            @Override
            public PartyLeaderTransferResponseEvent clone(PartyLeaderTransferResponseEvent value) {
                return new PartyLeaderTransferResponseEvent(value.getParty(), value.getOldLeader(), value.getNewLeader());
            }
        };
    }
}
