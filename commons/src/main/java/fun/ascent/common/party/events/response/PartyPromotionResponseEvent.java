package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyPromotionResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID promoter;
    private final UUID promoted;
    private final FullParty.Role newRole;

    public PartyPromotionResponseEvent(FullParty party, UUID promoter, UUID promoted, FullParty.Role newRole) {
        super(party);
        this.party = party;
        this.promoter = promoter;
        this.promoted = promoted;
        this.newRole = newRole;
    }

    @Override
    public Serializer<PartyPromotionResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyPromotionResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("promoter", value.getPromoter().toString());
                json.put("promoted", value.getPromoted().toString());
                json.put("newRole", value.getNewRole().name());
                return json.toString();
            }

            @Override
            public PartyPromotionResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID promoter = UUID.fromString(jsonObject.getString("promoter"));
                UUID promoted = UUID.fromString(jsonObject.getString("promoted"));
                FullParty.Role newRole = FullParty.Role.valueOf(jsonObject.getString("newRole"));
                return new PartyPromotionResponseEvent(party, promoter, promoted, newRole);
            }

            @Override
            public PartyPromotionResponseEvent clone(PartyPromotionResponseEvent value) {
                return new PartyPromotionResponseEvent(value.getParty(), value.getPromoter(), value.getPromoted(), value.getNewRole());
            }
        };
    }
}
