package fun.ascent.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public final class ItemRepository {
    private static final String COLLECTION_NAME = "hypixel_items_v3";
    private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private ItemRepository() {}

    private static MongoCollection<Document> collection() {
        return MongoProvider.getCollection(COLLECTION_NAME);
    }

    public static List<Document> getAllItems() {
        return collection().find().into(new ArrayList<>());
    }

    public static void saveItem(String id, Document doc) {
        doc.put("_id", id);
        collection().replaceOne(Filters.eq("_id", id), doc, UPSERT);
    }

    public static void saveItems(List<Document> docs) {
        for (Document doc : docs) {
            String id = doc.getString("_id");
            if (id == null) id = doc.getString("id");
            if (id != null) {
                saveItem(id, doc);
            }
        }
    }
    
    public static long getItemCount() {
        return collection().countDocuments();
    }
}
