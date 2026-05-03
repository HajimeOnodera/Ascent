package fun.ascent.common.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

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
