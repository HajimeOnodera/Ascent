package fun.ascent.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

/**
 * Central repository for all player data.
 * Each player has a single document in the "players" collection,
 * organized by sections (profile, friends, party, skyblock, etc.)
 */
public final class PlayerRepository {

    private static final String COLLECTION_NAME = "players";
    private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private PlayerRepository() {}

    private static MongoCollection<Document> collection() {
        return MongoProvider.getCollection(COLLECTION_NAME);
    }

    // ── Full document operations ────────────────────────────────────────────

    /**
     * Loads the full player document by UUID. Returns null if not found.
     */
    public static Document getPlayerDocument(UUID uuid) {
        return collection().find(Filters.eq("_id", uuid.toString())).first();
    }

    /**
     * Saves a full player document (upsert).
     */
    public static void savePlayerDocument(UUID uuid, Document doc) {
        doc.put("_id", uuid.toString());
        collection().replaceOne(Filters.eq("_id", uuid.toString()), doc, UPSERT);
    }

    /**
     * Checks if a player document exists.
     */
    public static boolean exists(UUID uuid) {
        return collection().find(Filters.eq("_id", uuid.toString())).first() != null;
    }

    /**
     * Deletes a player document entirely.
     */
    public static boolean delete(UUID uuid) {
        return collection().deleteOne(Filters.eq("_id", uuid.toString())).getDeletedCount() > 0;
    }

    // ── Section-based operations ────────────────────────────────────────────

    /**
     * Gets a specific section (sub-document) from a player's document.
     * Example: getSection(uuid, "profile") returns the "profile" sub-document.
     */
    public static Document getSection(UUID uuid, String section) {
        Document doc = getPlayerDocument(uuid);
        if (doc == null) return null;
        return doc.get(section, Document.class);
    }

    /**
     * Sets a specific section in a player's document.
     * Creates the player document if it doesn't exist.
     */
    public static void setSection(UUID uuid, String section, Document data) {
        collection().updateOne(
                Filters.eq("_id", uuid.toString()),
                Updates.set(section, data),
                new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    // ── Field-level operations ──────────────────────────────────────────────

    /**
     * Gets a single field from a player's document.
     * Supports dot-notation for nested fields: "profile.name"
     */
    @SuppressWarnings("unchecked")
    public static <T> T getField(UUID uuid, String field, T defaultValue) {
        Document doc = getPlayerDocument(uuid);
        if (doc == null) return defaultValue;

        // Support dot-notation
        String[] parts = field.split("\\.");
        Object current = doc;
        for (String part : parts) {
            if (current instanceof Document d) {
                current = d.get(part);
            } else {
                return defaultValue;
            }
        }

        return current != null ? (T) current : defaultValue;
    }

    /**
     * Sets a single field in a player's document.
     * Supports dot-notation for nested fields: "profile.name"
     * Creates the player document if it doesn't exist.
     */
    public static void setField(UUID uuid, String field, Object value) {
        collection().updateOne(
                Filters.eq("_id", uuid.toString()),
                Updates.set(field, value),
                new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    /**
     * Removes a field from a player's document.
     */
    public static void removeField(UUID uuid, String field) {
        collection().updateOne(
                Filters.eq("_id", uuid.toString()),
                Updates.unset(field)
        );
    }

    // ── Batch / query operations ────────────────────────────────────────────

    /**
     * Updates multiple fields at once.
     */
    public static void setFields(UUID uuid, Map<String, Object> fields) {
        List<Bson> updates = new ArrayList<>();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            updates.add(Updates.set(entry.getKey(), entry.getValue()));
        }
        collection().updateOne(
                Filters.eq("_id", uuid.toString()),
                Updates.combine(updates),
                new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    /**
     * Increments a numeric field. Creates it with the given amount if it doesn't exist.
     */
    public static void incrementField(UUID uuid, String field, Number amount) {
        collection().updateOne(
                Filters.eq("_id", uuid.toString()),
                Updates.inc(field, amount),
                new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    /**
     * Pushes a value to an array field.
     */
    public static void pushToArray(UUID uuid, String field, Object value) {
        collection().updateOne(
                Filters.eq("_id", uuid.toString()),
                Updates.push(field, value),
                new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    /**
     * Pulls (removes) a value from an array field.
     */
    public static void pullFromArray(UUID uuid, String field, Object value) {
        collection().updateOne(
                Filters.eq("_id", uuid.toString()),
                Updates.pull(field, value)
        );
    }
}
