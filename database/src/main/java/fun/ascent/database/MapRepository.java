package fun.ascent.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.Binary;

public final class MapRepository {

    private static final String COLLECTION_NAME = "worlds";
    private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private MapRepository() {}

    private static MongoCollection<Document> collection() {
        return MongoProvider.getCollection(COLLECTION_NAME);
    }

    /**
     * Saves a world's binary data to MongoDB.
     */
    public static void saveWorld(String id, byte[] data) {
        Document doc = new Document("_id", id);
        doc.append("data", new Binary(data));
        collection().replaceOne(Filters.eq("_id", id), doc, UPSERT);
    }

    /**
     * Loads a world's binary data from MongoDB.
     */
    public static byte[] loadWorld(String id) {
        Document doc = collection().find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        
        Binary binary = doc.get("data", Binary.class);
        return binary != null ? binary.getData() : null;
    }

    /**
     * Checks if a world exists in MongoDB.
     */
    public static boolean exists(String id) {
        return collection().find(Filters.eq("_id", id)).first() != null;
    }
}
