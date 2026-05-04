package fun.ascent.lobby.cache;

import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.redis.ServerPing;
import fun.ascent.lobby.game.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A shared cache for server information with 5-second TTL.
 * Used by game menus, lobby selectors, and NPCs to get player counts.
 */
public class ServerInfoCache {
    private static List<ServerPing> cachedServers = new ArrayList<>();
    private static long lastCacheTime = 0;
    private static final long CACHE_TTL_MS = 5000;

    /**
     * Get all servers, using cache if available.
     */
    public static CompletableFuture<List<ServerPing>> getServers() {
        if (!isCacheStale()) {
            return CompletableFuture.completedFuture(new ArrayList<>(cachedServers));
        }
        return refreshCache();
    }

    /**
     * Get servers of a specific game type.
     */
    public static CompletableFuture<List<ServerPing>> getServersByType(GameType game) {
        return getServers().thenApply(servers ->
                servers.stream().filter(s -> s.serverName().startsWith(game.getServerPrefix())).toList()
        );
    }

    /**
     * Get total player count for a game type (uses cached data).
     */
    public static int getTotalPlayersForType(GameType game) {
        return cachedServers.stream()
                .filter(s -> s.serverName().startsWith(game.getServerPrefix()))
                .mapToInt(ServerPing::onlinePlayers)
                .sum();
    }

    /**
     * Get total player count across all SkyBlock server instances.
     */
    public static int getTotalSkyBlockPlayers() {
        return cachedServers.stream()
                .filter(s -> s.serverName().startsWith("skyblock"))
                .mapToInt(ServerPing::onlinePlayers)
                .sum();
    }

    /**
     * Force refresh the cache asynchronously.
     */
    public static CompletableFuture<List<ServerPing>> refreshCache() {
        return CompletableFuture.supplyAsync(() -> {
            List<ServerPing> servers = ServerLookup.findByPrefix("");
            cachedServers = new ArrayList<>(servers);
            lastCacheTime = System.currentTimeMillis();
            return servers;
        });
    }

    /**
     * Check if cache is stale.
     */
    public static boolean isCacheStale() {
        return System.currentTimeMillis() - lastCacheTime >= CACHE_TTL_MS || cachedServers.isEmpty();
    }
}
