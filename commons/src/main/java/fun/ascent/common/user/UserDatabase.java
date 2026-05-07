package fun.ascent.common.user;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import java.util.UUID;

public class UserDatabase {
    private static MongoClient mongoClient;
    private static MongoCollection<Document> userCollection;

    public static void connect(String uri) {
        if (mongoClient != null) return;
        
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        
        mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("Minestom");
        userCollection = database.getCollection("users");
    }

    public static User loadUser(UUID uuid) {
        Document doc = userCollection.find(Filters.eq("_id", uuid.toString())).first();
        if (doc == null) return null;

        User user = new User();
        user.setUuid(uuid);
        user.setName(doc.getString("name"));
        user.setRank(Rank.valueOf(doc.getString("rank")));
        return user;
    }

    public static void saveUser(User user) {
        Document doc = new Document("_id", user.getUuid().toString())
                .append("name", user.getName())
                .append("rank", user.getRank().name());
        
        userCollection.replaceOne(Filters.eq("_id", user.getUuid().toString()), doc, new ReplaceOptions().upsert(true));
    }
}
