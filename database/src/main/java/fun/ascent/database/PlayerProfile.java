package fun.ascent.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

/**
 * Represents a player's profile stored in MongoDB.
 * This is the central data object for a player, containing all core info.
 *
 * <p>Document structure in the "players" collection:
 * <pre>
 * {
 *   "_id": "uuid-string",
 *   "profile": {
 *     "name": "PlayerName",
 *     "rank": "DEFAULT",
 *     "firstJoin": 1234567890,
 *     "lastSeen": 1234567890
 *   }
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProfile {

    private static final String SECTION = "profile";

    private UUID uuid;
    private String name;
    private String rank = "DEFAULT";
    private int level = 1;
    private long firstJoin;
    private long lastSeen;

    // ── Persistence ─────────────────────────────────────────────────────────

    /**
     * Loads a player profile from MongoDB. Returns null if not found.
     */
    public static PlayerProfile load(UUID uuid) {
        Document section = PlayerRepository.getSection(uuid, SECTION);
        if (section == null) return null;

        PlayerProfile profile = new PlayerProfile();
        profile.uuid = uuid;
        profile.name = section.getString("name");
        profile.rank = section.getString("rank");
        profile.level = section.getInteger("level", 1);
        profile.firstJoin = section.getLong("firstJoin");
        profile.lastSeen = section.getLong("lastSeen");
        return profile;
    }

    /**
     * Saves this profile to MongoDB.
     */
    public void save() {
        Document doc = new Document()
                .append("name", name)
                .append("rank", rank)
                .append("level", level)
                .append("firstJoin", firstJoin)
                .append("lastSeen", lastSeen);

        PlayerRepository.setSection(uuid, SECTION, doc);
    }

    /**
     * Loads or creates a new profile with defaults.
     */
    public static PlayerProfile loadOrCreate(UUID uuid, String name) {
        PlayerProfile profile = load(uuid);
        if (profile != null) {
            profile.setLastSeen(System.currentTimeMillis());
            return profile;
        }

        long now = System.currentTimeMillis();
        profile = new PlayerProfile(uuid, name, "DEFAULT", 1, now, now);
        profile.save();
        return profile;
    }
}
