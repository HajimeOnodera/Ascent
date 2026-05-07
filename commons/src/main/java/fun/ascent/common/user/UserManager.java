package fun.ascent.common.user;

import fun.ascent.common.redis.RedisManager;
import net.kyori.adventure.text.Component;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private static final Map<UUID, User> userCache = new ConcurrentHashMap<>();
    private static final String REDIS_PREFIX = "ascent:user:";

    public static Component getDisplayName(UUID uuid) {
        return getUser(uuid).getDisplayName();
    }

    public static String getLegacyDisplayName(UUID uuid) {
        return getUser(uuid).getLegacyDisplayName();
    }

    public static void init(String mongoUri) {
        UserDatabase.connect(mongoUri);
    }

    public static User getUser(UUID uuid) {
        if (userCache.containsKey(uuid)) {
            return userCache.get(uuid);
        }

        // Try Redis
        try (Jedis jedis = RedisManager.get().getResource()) {
            String data = jedis.get(REDIS_PREFIX + uuid);
            if (data != null) {
                User user = deserialize(data);
                userCache.put(uuid, user);
                return user;
            }
        } catch (Exception ignored) {}

        // Try MongoDB
        User mongoUser = UserDatabase.loadUser(uuid);
        if (mongoUser != null) {
            userCache.put(uuid, mongoUser);
            // Cache back to Redis
            try (Jedis jedis = RedisManager.get().getResource()) {
                jedis.set(REDIS_PREFIX + uuid, serialize(mongoUser));
            } catch (Exception ignored) {}
            return mongoUser;
        }

        // Fallback or create new
        User user = new User(uuid, "Unknown", Rank.DEFAULT);
        userCache.put(uuid, user);
        return user;
    }

    public static void saveUser(User user) {
        userCache.put(user.getUuid(), user);
        
        // Save to Redis
        try (Jedis jedis = RedisManager.get().getResource()) {
            jedis.set(REDIS_PREFIX + user.getUuid(), serialize(user));
        } catch (Exception ignored) {}

        // Save to MongoDB
        UserDatabase.saveUser(user);
    }

    private static String serialize(User user) {
        JSONObject json = new JSONObject();
        json.put("uuid", user.getUuid().toString());
        json.put("name", user.getName());
        json.put("rank", user.getRank().name());
        return json.toString();
    }

    private static User deserialize(String data) {
        JSONObject json = new JSONObject(data);
        return new User(
                UUID.fromString(json.getString("uuid")),
                json.getString("name"),
                Rank.valueOf(json.getString("rank"))
        );
    }
}
