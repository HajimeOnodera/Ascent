package fun.ascent.common.protocol.objects.party;

import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class IsPlayerInPartyProtocolObject extends ProtocolObject
        <IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage,
                IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse> {

    @Override
    public Serializer<IsPlayerInPartyMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(IsPlayerInPartyMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUuid.toString());
                return json.toString();
            }

            @Override
            public IsPlayerInPartyMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID playerUUID = UUID.fromString(jsonObject.getString("playerUUID"));
                return new IsPlayerInPartyMessage(playerUUID);
            }

            @Override
            public IsPlayerInPartyMessage clone(IsPlayerInPartyMessage value) {
                return new IsPlayerInPartyMessage(value.playerUuid);
            }
        };
    }

    @Override
    public Serializer<IsPlayerInPartyResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(IsPlayerInPartyResponse value) {
                JSONObject json = new JSONObject();
                json.put("inParty", value.inParty);
                json.put("partyUUID", value.partyUUID != null ? value.partyUUID.toString() : null);
                return json.toString();
            }

            @Override
            public IsPlayerInPartyResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                boolean inParty = jsonObject.getBoolean("inParty");
                UUID partyUUID = jsonObject.has("partyUUID") && !jsonObject.isNull("partyUUID")
                        ? UUID.fromString(jsonObject.getString("partyUUID"))
                        : null;
                return new IsPlayerInPartyResponse(inParty, partyUUID);
            }

            @Override
            public IsPlayerInPartyResponse clone(IsPlayerInPartyResponse value) {
                return new IsPlayerInPartyResponse(value.inParty, value.partyUUID);
            }
        };
    }

    public record IsPlayerInPartyMessage(UUID playerUuid) { }

    public record IsPlayerInPartyResponse(boolean inParty, UUID partyUUID) { }
}
