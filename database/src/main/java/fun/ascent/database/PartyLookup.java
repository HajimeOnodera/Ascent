package fun.ascent.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Read-only lookup for a player's party from the party-data collection.
 * Intended for use by any module that needs to know a player's party
 * without depending on the full party service.
 */
public final class PartyLookup {

    private static final String COLLECTION_NAME = "party-data";

    private PartyLookup() {}

    /**
     * Lightweight record representing a party member for GUI display.
     */
    public record PartyMember(UUID uuid, String role, boolean joined) {}

    /**
     * Lightweight record representing a full party for GUI display.
     */
    public record PartyInfo(UUID partyUuid, List<PartyMember> members) {
        public PartyMember getLeader() {
            return members.stream()
                    .filter(m -> "LEADER".equals(m.role()))
                    .findFirst()
                    .orElse(null);
        }
    }

    public static PartyInfo getPartyForPlayer(UUID playerUuid) {
        try {
            MongoCollection<Document> collection = MongoProvider.getCollection(COLLECTION_NAME);

            // The party-data collection stores parties with serialized data.
            // Members are embedded in the serialized JSON.
            // We need to scan for a party containing this player UUID.
            for (Document doc : collection.find()) {
                String data = doc.getString("data");
                if (data == null) continue;

                JsonObject json = JsonParser.parseString(data).getAsJsonObject();
                JsonArray membersArray = json.getAsJsonArray("members");
                if (membersArray == null) continue;

                // Check if this player is in this party
                boolean found = false;
                for (JsonElement element : membersArray) {
                    JsonObject memberObj = element.getAsJsonObject();
                    if (memberObj.has("uuid") &&
                            playerUuid.toString().equals(memberObj.get("uuid").getAsString())) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    UUID partyUuid = UUID.fromString(json.get("uuid").getAsString());
                    List<PartyMember> members = new ArrayList<>();
                    for (JsonElement element : membersArray) {
                        JsonObject memberObj = element.getAsJsonObject();
                        UUID uuid = UUID.fromString(memberObj.get("uuid").getAsString());
                        String role = memberObj.has("role") ? memberObj.get("role").getAsString() : "MEMBER";
                        boolean joined = !memberObj.has("joined") || memberObj.get("joined").getAsBoolean();
                        members.add(new PartyMember(uuid, role, joined));
                    }
                    return new PartyInfo(partyUuid, members);
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("[PartyLookup] Failed to load party for " + playerUuid + ": " + e.getMessage());
            return null;
        }
    }
}
