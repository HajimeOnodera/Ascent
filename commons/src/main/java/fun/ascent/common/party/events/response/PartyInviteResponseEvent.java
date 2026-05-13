package fun.ascent.common.party.events.response;

import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.party.PendingParty;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyInviteResponseEvent extends PartyResponseEvent {
    private final UUID inviter;
    private final UUID invitee;

    public PartyInviteResponseEvent(PendingParty party) {
        super(party);
        this.inviter = party.leader();
        this.invitee = party.invitee();
    }

    @Override
    public Serializer<PartyInviteResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyInviteResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<PendingParty> partySerializer = PendingParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((PendingParty) value.getParty()));
                json.put("inviter", value.inviter.toString());
                json.put("invitee", value.invitee.toString());
                return json.toString();
            }

            @Override
            public PartyInviteResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                PendingParty party = PendingParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                return new PartyInviteResponseEvent(party);
            }

            @Override
            public PartyInviteResponseEvent clone(PartyInviteResponseEvent value) {
                return new PartyInviteResponseEvent((PendingParty) value.getParty());
            }
        };
    }
}
