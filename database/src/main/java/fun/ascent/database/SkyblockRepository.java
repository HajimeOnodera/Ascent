package fun.ascent.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository for Skyblock profile data.
 * Profiles are stored in the "skyblock_profiles" collection.
 */
public final class SkyblockRepository {

    private static final String COLLECTION_NAME = "skyblock_profiles";
    private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private SkyblockRepository() {}

    private static MongoCollection<Document> collection() {
        return MongoProvider.getCollection(COLLECTION_NAME);
    }

    /**
     * Loads a profile document by ID.
     */
    public static Document getProfile(UUID profileID) {
        return collection().find(Filters.eq("_id", profileID.toString())).first();
    }

    /**
     * Saves a profile document.
     */
    public static void saveProfile(UUID profileID, Document doc) {
        doc.put("_id", profileID.toString());
        collection().replaceOne(Filters.eq("_id", profileID.toString()), doc, UPSERT);
    }

    /**
     * Finds all profiles that a specific player belongs to.
     */
    public static List<Document> getProfilesForPlayer(UUID playerUUID) {
        return collection().find(Filters.exists("members." + playerUUID.toString()))
                .into(new ArrayList<>());
    }

    /**
     * Deletes a profile.
     */
    public static void deleteProfile(UUID profileID) {
        collection().deleteOne(Filters.eq("_id", profileID.toString()));
    }
}
