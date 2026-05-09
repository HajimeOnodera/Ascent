package fun.ascent.common.service.redis;

import fun.ascent.common.redis.RedisConfig;
import fun.ascent.common.redis.RedisManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Manages Redis connectivity for services.
 * Handles connection initialization and reliable message publishing with retry logic.
 */
public class ServiceRedisManager {

    private static final int MAX_PUBLISH_RETRIES = 3;

    public static void connect(RedisConfig config) {
        if (!RedisManager.isInitialized()) {
            RedisManager.connect(config);
        }
        System.out.println("[ServiceRedisManager] Connected to Redis at " + config.host() + ":" + config.port());
    }

    /**
     * Publishes a message to a Redis channel with automatic retry on failure.
     */
    public static void publish(String channel, String message) {
        for (int attempt = 1; attempt <= MAX_PUBLISH_RETRIES; attempt++) {
            try (Jedis jedis = RedisManager.get().getResource()) {
                jedis.publish(channel, message);
                return;
            } catch (JedisException e) {
                System.err.println("[ServiceRedisManager] Publish failed (attempt " + attempt
                        + "/" + MAX_PUBLISH_RETRIES + "): " + e.getMessage());
                if (attempt < MAX_PUBLISH_RETRIES) {
                    try {
                        Thread.sleep(500L * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
        System.err.println("[ServiceRedisManager] Publish exhausted all retries for channel: " + channel);
    }

    public static RedisManager getRedisManager() {
        return RedisManager.get();
    }
}
