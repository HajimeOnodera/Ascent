package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyInviteExpiredResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID inviter;
    private final UUID invitee;

    public PartyInviteExpiredResponseEvent(FullParty party, UUID inviter, UUID invitee) {
        super(party);
        this.party = party;
        this.inviter = inviter;
        this.invitee = invitee;
    }

    @Override
    public Serializer<PartyInviteExpiredResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyInviteExpiredResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("inviter", value.getInviter().toString());
                json.put("invitee", value.getInvitee().toString());
                return json.toString();
            }

            @Override
            public PartyInviteExpiredResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID inviter = UUID.fromString(jsonObject.getString("inviter"));
                UUID invitee = UUID.fromString(jsonObject.getString("invitee"));
                return new PartyInviteExpiredResponseEvent(party, inviter, invitee);
            }

            @Override
            public PartyInviteExpiredResponseEvent clone(PartyInviteExpiredResponseEvent value) {
                return new PartyInviteExpiredResponseEvent(value.getParty(), value.getInviter(), value.getInvitee());
            }
        };
    }
}
