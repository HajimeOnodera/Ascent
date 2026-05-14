package fun.ascent.skyblock.island.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import fun.ascent.database.MongoProvider;
import org.bson.Document;
import org.bson.types.Binary;

import java.util.List;
import java.util.UUID;

public class IslandDatabase {
    private static final MongoCollection<Document> collection = MongoProvider.getDatabase().getCollection("islands");

    public static void saveIsland(UUID islandId, byte[] data, int version, List<Document> minions, List<Document> npcs) {
        collection.updateOne(
                Filters.eq("_id", islandId.toString()),
                Updates.combine(
                        Updates.set("data", new Binary(data)),
                        Updates.set("version", version),
                        Updates.set("minions", minions),
                        Updates.set("npcs", npcs),
                        Updates.set("lastSaved", System.currentTimeMillis())
                ),
                new UpdateOptions().upsert(true)
        );
    }

    public static Document getIsland(UUID islandId) {
        return collection.find(Filters.eq("_id", islandId.toString())).first();
    }

    public static boolean exists(UUID islandId) {
        return collection.find(Filters.eq("_id", islandId.toString())).first() != null;
    }
}
