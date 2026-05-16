package fun.ascent.discord.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class LinkRepository {

    private static final String CODES_COL = "discord_codes";
    private static final String LINKS_COL = "discord_links";
    private static final String DEFAULT_URI = "mongodb://127.0.0.1:27017";
    private static final String DEFAULT_DB = "ascentSync";
    private static final String MINECRAFT_NAME_LOWER = "minecraftNameLower";

    private final MongoCollection<Document> codes;
    private final MongoCollection<Document> links;
    private final ExecutorService io = Executors.newVirtualThreadPerTaskExecutor();
    private final MongoClient client;

    public LinkRepository() {
        String uri = System.getenv().getOrDefault("MONGODB_URI", DEFAULT_URI);
        ConnectionString connectionString = new ConnectionString(uri);
        String databaseName = System.getenv("MONGO_DB");
        if (databaseName == null || databaseName.isBlank()) {
            databaseName = connectionString.getDatabase();
        }
        if (databaseName == null || databaseName.isBlank()) {
            databaseName = DEFAULT_DB;
        }

        this.client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .build()
        );

        MongoDatabase database = client.getDatabase(databaseName);
        this.codes = database.getCollection(CODES_COL);
        this.links = database.getCollection(LINKS_COL);
        bootstrap();
    }

    private void bootstrap() {
        try {
            codes.createIndex(
                    Indexes.ascending("createdAt"),
                    new IndexOptions().expireAfter(120L, TimeUnit.SECONDS)
            );
        } catch (Exception ignored) {}

        try {
            links.createIndex(Indexes.ascending("discordId"), new IndexOptions().unique(true).sparse(true));
            links.createIndex(Indexes.ascending("minecraftUUID"), new IndexOptions().unique(true).sparse(true));
            links.createIndex(Indexes.ascending(MINECRAFT_NAME_LOWER), new IndexOptions().unique(true).sparse(true));
            links.createIndex(Indexes.ascending("minecraftIGN"));
        } catch (Exception ignored) {}
    }

    public CompletableFuture<Outcome<Document>> fetchCode(String code) {
        return CompletableFuture.supplyAsync(() -> {
            var doc = codes.find(
                    Filters.and(
                            Filters.eq("code", code),
                            Filters.ne("used", true)
                    )
            ).first();
            return doc != null ? Outcome.ok(doc) : Outcome.<Document>fail("not_found");
        }, io);
    }

    public CompletableFuture<Boolean> isLinked(String ign) {
        String ignLower = ign.toLowerCase();
        return CompletableFuture.supplyAsync(() ->
                        links.countDocuments(
                                Filters.and(
                                        Filters.eq("isVerified", true),
                                        Filters.or(
                                                Filters.eq(MINECRAFT_NAME_LOWER, ignLower),
                                                Filters.regex("minecraftIGN", "^" + java.util.regex.Pattern.quote(ign) + "$", "i")
                                        )
                                )
                        ) > 0,
                io
        );
    }

    public CompletableFuture<Void> consumeAndLink(String code, String ign, String uuid) {
        return CompletableFuture.runAsync(() -> {
            var codeDoc = codes.findOneAndUpdate(
                    Filters.and(Filters.eq("code", code), Filters.ne("used", true)),
                    Updates.combine(
                            Updates.set("used", true),
                            Updates.set("usedBy", ign),
                            Updates.set("usedAt", new Date())
                    )
            );

            if (codeDoc == null) return;

            String discordId = codeDoc.getString("discordId");
            Date now = new Date();
            String ignLower = ign.toLowerCase();

            // Force other active or stale mappings for this Minecraft name inactive
            // so there is only ever one canonical current owner.
            links.updateMany(
                    Filters.and(
                            Filters.eq(MINECRAFT_NAME_LOWER, ignLower),
                            Filters.ne("discordId", discordId)
                    ),
                    Updates.combine(
                            Updates.set("isVerified", false),
                            Updates.set("unlinkedAt", now),
                            Updates.unset("minecraftUUID"),
                            Updates.unset(MINECRAFT_NAME_LOWER)
                    )
            );

            // Release stale inactive mappings so the same Minecraft account
            // can be linked to a different Discord account later.
            links.updateMany(
                    Filters.and(
                            Filters.regex("minecraftIGN", "^" + java.util.regex.Pattern.quote(ign) + "$", "i"),
                            Filters.eq("isVerified", false),
                            Filters.ne("discordId", discordId)
                    ),
                    Updates.combine(
                            Updates.unset("minecraftUUID"),
                            Updates.unset(MINECRAFT_NAME_LOWER)
                    )
            );

            var historyEntry = new Document()
                    .append("discordId", discordId)
                    .append("minecraftIGN", ign)
                    .append("linkedAt", now)
                    .append("status", "active");

            try {
                links.updateOne(
                        Filters.eq("discordId", discordId),
                        Updates.combine(
                                Updates.set("discordId", discordId),
                                Updates.set("minecraftIGN", ign),
                                Updates.set(MINECRAFT_NAME_LOWER, ignLower),
                                Updates.set("minecraftUUID", uuid),
                                Updates.set("isVerified", true),
                                Updates.set("linkedAt", now),
                                Updates.set("lastVerified", now),
                                Updates.unset("unlinkedAt"),
                                Updates.addToSet("history", historyEntry)
                        ),
                        new UpdateOptions().upsert(true)
                );
            } catch (RuntimeException ex) {
                codes.updateOne(
                        Filters.eq("_id", codeDoc.getObjectId("_id")),
                        Updates.combine(
                                Updates.set("used", false),
                                Updates.unset("usedBy"),
                                Updates.unset("usedAt")
                        )
                );
                throw ex;
            }
        }, io);
    }

    public CompletableFuture<Boolean> unlink(String ign) {
        String ignLower = ign.toLowerCase();
        return CompletableFuture.supplyAsync(() -> {
            var result = links.updateOne(
                    Filters.and(
                            Filters.eq("isVerified", true),
                            Filters.or(
                                    Filters.eq(MINECRAFT_NAME_LOWER, ignLower),
                                    Filters.regex("minecraftIGN", "^" + java.util.regex.Pattern.quote(ign) + "$", "i")
                            )
                    ),
                    Updates.combine(
                            Updates.set("isVerified", false),
                            Updates.set("unlinkedAt", new Date()),
                            Updates.unset("minecraftUUID"),
                            Updates.unset(MINECRAFT_NAME_LOWER)
                    )
            );
            return result.getModifiedCount() > 0;
        }, io);
    }

    public CompletableFuture<List<Document>> historyByIGN(String ign) {
        return CompletableFuture.supplyAsync(() ->
                        links.find(Filters.regex("minecraftIGN", "^" + ign + "$", "i")).into(new java.util.ArrayList<>()),
                io
        );
    }

    public CompletableFuture<List<Document>> historyByDiscordId(String discordId) {
        return CompletableFuture.supplyAsync(() ->
                        links.find(Filters.eq("discordId", discordId)).into(new java.util.ArrayList<>()),
                io
        );
    }
}
