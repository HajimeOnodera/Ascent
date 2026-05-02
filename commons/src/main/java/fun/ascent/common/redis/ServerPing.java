package fun.ascent.common.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

/**
 * Represents the data a backend server publishes to Redis every second.
 *
 * Redis key: {@code ascent:servers:<serverName>}
 * Value    : JSON representation of this class
 * TTL      : {@link RedisManager#PING_TTL_SECONDS} seconds
 *
 * Fields:
 *   serverName     – logical name (e.g. "lobby", "skyblock")
 *   host           – the host/IP Velocity should use to connect (e.g. Docker service name)
 *   port           – Minecraft port
 *   onlinePlayers  – number of currently online players
 *   lastPing       – epoch millis of the last update
 */
public record ServerPing(String serverName, String host, int port, int onlinePlayers, long lastPing) {

    private static final Gson GSON = new Gson();

    public String toJson() {
        return GSON.toJson(this);
    }

    public static ServerPing fromJson(String json) {
        return GSON.fromJson(json, ServerPing.class);
    }

    /** Writes this ping into Redis, resetting the TTL each call. */
    public void publish(Jedis jedis) {
        String key = RedisManager.SERVERS_KEY_PREFIX + serverName;
        jedis.setex(key, RedisManager.PING_TTL_SECONDS, toJson());
    }
}
