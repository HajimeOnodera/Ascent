package fun.ascent.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Read-only lookup for a player's friend list from the friend-data collection.
 * Intended for use by any module that needs to know a player's friends
 * without depending on the full friend service.
 */
public final class FriendLookup {

    private static final String COLLECTION_NAME = "friend-data";

    private FriendLookup() {}

    /**
     * Returns the UUIDs of all friends for the given player.
     * Returns an empty list if no friend data exists or on error.
     */
    public static List<UUID> getFriendUuids(UUID playerUuid) {
        try {
            MongoCollection<Document> collection = MongoProvider.getCollection(COLLECTION_NAME);
            Document doc = collection.find(Filters.eq("_id", playerUuid.toString())).first();
            if (doc == null) {
                return Collections.emptyList();
            }

            String data = doc.getString("data");
            if (data == null) {
                return Collections.emptyList();
            }

            JsonObject json = JsonParser.parseString(data).getAsJsonObject();
            JsonArray friendsArray = json.getAsJsonArray("friends");
            if (friendsArray == null) {
                return Collections.emptyList();
            }

            List<UUID> uuids = new ArrayList<>(friendsArray.size());
            for (JsonElement element : friendsArray) {
                JsonObject friendObj = element.getAsJsonObject();
                if (friendObj.has("uuid")) {
                    uuids.add(UUID.fromString(friendObj.get("uuid").getAsString()));
                }
            }
            return uuids;
        } catch (Exception e) {
            System.err.println("[FriendLookup] Failed to load friends for " + playerUuid + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
