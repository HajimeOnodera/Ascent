package fun.ascent.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Singleton MongoDB connection provider.
 * All modules share a single MongoClient for efficiency.
 */
public final class MongoProvider {

    private static final String DATABASE_NAME = "Minestom";

    private static MongoClient client;
    private static MongoDatabase database;

    private MongoProvider() {}

    /**
     * Initializes the shared MongoDB connection. Call once at startup.
     */
    public static synchronized void connect(String uri) {
        if (client != null) return;

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        client = MongoClients.create(settings);
        database = client.getDatabase(DATABASE_NAME);

        System.out.println("[Database] Connected to MongoDB (" + uri + ")");
    }

    /**
     * Returns the shared database instance.
     */
    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("MongoProvider not initialized. Call connect() first.");
        }
        return database;
    }

    /**
     * Convenience: returns a collection from the shared database.
     */
    public static MongoCollection<Document> getCollection(String name) {
        return getDatabase().getCollection(name);
    }

    /**
     * Returns the underlying MongoClient for advanced usage.
     */
    public static MongoClient getClient() {
        if (client == null) {
            throw new IllegalStateException("MongoProvider not initialized. Call connect() first.");
        }
        return client;
    }

    /**
     * Whether the provider has been initialized.
     */
    public static boolean isInitialized() {
        return client == null;
    }

    /**
     * Closes the shared connection. Call during shutdown.
     */
    public static synchronized void close() {
        if (client != null) {
            client.close();
            client = null;
            database = null;
        }
    }
}
