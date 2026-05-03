package fun.ascent.proxy;

import com.google.gson.Gson;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class ServerRegistryManager {

    private static final String KEY_PREFIX = "ascent:servers:";
    private static final Gson GSON = new Gson();

    private final ProxyServer proxy;
    private final Logger logger;
    private final JedisPool jedisPool;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "ascent-redis-watcher");
                t.setDaemon(true);
                return t;
            });

    ServerRegistryManager(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
        this.jedisPool = buildPool();
    }

    void start() {
        scheduler.scheduleAtFixedRate(this::sync, 0, 1, TimeUnit.SECONDS);
        logger.info("ServerRegistryManager started – polling Redis every 1 s");
    }

    void stop() {
        scheduler.shutdownNow();
        jedisPool.close();
    }

    // ── Core sync loop ──────────────────────────────────────────────────────

    private void sync() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> liveServerNames = new HashSet<>();

            // Scan all ping keys
            for (String key : jedis.keys(KEY_PREFIX + "*")) {
                String json = jedis.get(key);
                if (json == null) continue;

                ServerRecord rec = GSON.fromJson(json, ServerRecord.class);
                if (rec == null || rec.serverName() == null || rec.host() == null) continue;

                liveServerNames.add(rec.serverName());
                ensureRegistered(rec);
            }

            // Remove any servers no longer in Redis
            for (RegisteredServer rs : proxy.getAllServers()) {
                String name = rs.getServerInfo().getName();
                if (!liveServerNames.contains(name)) {
                    proxy.unregisterServer(rs.getServerInfo());
                    logger.info("Unregistered server '{}' (no ping)", name);
                }
            }
        } catch (JedisException e) {
            logger.warn("Redis sync error: {}", e.getMessage());
        }
    }

    private void ensureRegistered(ServerRecord rec) {
        InetSocketAddress address = new InetSocketAddress(rec.host(), rec.port());
        ServerInfo info = new ServerInfo(rec.serverName(), address);

        proxy.getServer(rec.serverName()).ifPresentOrElse(
                existing -> {
                    // Already registered – check if address changed
                    if (!existing.getServerInfo().getAddress().equals(address)) {
                        proxy.unregisterServer(existing.getServerInfo());
                        proxy.registerServer(info);
                        logger.info("Re-registered server '{}' with new address {}:{}",
                                rec.serverName(), rec.host(), rec.port());
                    }
                },
                () -> {
                    proxy.registerServer(info);
                    logger.info("Registered server '{}' @ {}:{} ({} players online)",
                            rec.serverName(), rec.host(), rec.port(), rec.onlinePlayers());
                }
        );
    }

    // ── Redis pool ──────────────────────────────────────────────────────────

    private static JedisPool buildPool() {
        String host = env("REDIS_HOST", "redis");
        int port = Integer.parseInt(env("REDIS_PORT", "6379"));
        String password = env("REDIS_PASSWORD", "");

        JedisPoolConfig cfg = new JedisPoolConfig();
        cfg.setMaxTotal(4);
        cfg.setMaxIdle(2);
        cfg.setMinIdle(1);
        cfg.setTestOnBorrow(true);
        cfg.setMaxWait(Duration.ofSeconds(5));

        if (password.isBlank()) {
            return new JedisPool(cfg, host, port, 5000);
        } else {
            return new JedisPool(cfg, host, port, 5000, password);
        }
    }

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v.trim() : fallback;
    }

    // ── DTO ─────────────────────────────────────────────────────────────────

    private record ServerRecord(String serverName, String host, int port, int onlinePlayers, long lastPing) {}
}
