package fun.ascent.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Queries Redis for live server entries published by {@link PingService}.
 * Use this from backend servers (lobby, skyblock) to discover
 * other servers by type prefix (e.g. "lobby", "skyblock").
 */
public final class ServerLookup {

    private ServerLookup() {}

    public static List<ServerPing> findByPrefix(String prefix) {
        try (Jedis jedis = RedisManager.get().getResource()) {
            List<ServerPing> result = new ArrayList<>();
            for (String key : jedis.keys(RedisManager.SERVERS_KEY_PREFIX + prefix + "*")) {
                String json = jedis.get(key);
                if (json == null) continue;
                try {
                    ServerPing ping = ServerPing.fromJson(json);
                    if (ping != null && ping.serverName() != null) {
                        result.add(ping);
                    }
                } catch (Exception ignored) {}
            }
            // Sort by name for consistent ordering
            result.sort(Comparator.comparing(ServerPing::serverName));
            return result;
        } catch (JedisException e) {
            System.err.println("[ServerLookup] Redis error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static String findAnyByPrefix(String prefix) {
        List<ServerPing> servers = findByPrefix(prefix);
        return servers.isEmpty() ? null : servers.getFirst().serverName();
    }
}
