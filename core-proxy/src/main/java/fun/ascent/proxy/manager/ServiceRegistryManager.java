package fun.ascent.proxy.manager;

import fun.ascent.proxy.config.*;
import fun.ascent.proxy.service.*;
import fun.ascent.common.service.ServiceType;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceRegistryManager {
    private static final String KEY_PREFIX = "ascent:services:";
    private static final long SERVICE_TIMEOUT_SECONDS = 10;

    /**
     * Grace period: don't expire services during Redis outages.
     * Services are only expired if the registry has had at least one
     * successful sync within this window.
     */
    private static final long GRACE_PERIOD_SECONDS = 30;

    private final Logger logger;
    private final JedisPool jedisPool;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "ascent-service-registry");
                t.setDaemon(true);
                return t;
            });

    private final Map<ServiceType, Instant> lastSeen = new ConcurrentHashMap<>();
    private volatile Instant lastSuccessfulSync = null;
    private final AtomicInteger consecutiveFailures = new AtomicInteger(0);

    public ServiceRegistryManager(Logger logger) {
        this.logger = logger;
        this.jedisPool = buildPool();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::sync, 0, 2, TimeUnit.SECONDS);
        logger.info("ServiceRegistryManager started – polling Redis every 2 s");
    }

    public void stop() {
        scheduler.shutdownNow();
        jedisPool.close();
    }

    public boolean isServiceOnline(ServiceType type) {
        Instant last = lastSeen.get(type);
        if (last == null) return false;

        // If Redis has been unreachable, extend the grace period
        // so we don't falsely mark services as offline
        long timeout = isRedisHealthy()
                ? SERVICE_TIMEOUT_SECONDS
                : GRACE_PERIOD_SECONDS;

        return Duration.between(last, Instant.now()).getSeconds() < timeout;
    }

    /**
     * Returns true if the registry has had a successful Redis sync recently.
     */
    private boolean isRedisHealthy() {
        return lastSuccessfulSync != null
                && Duration.between(lastSuccessfulSync, Instant.now()).getSeconds() < 15;
    }

    private void sync() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys(KEY_PREFIX + "*");

            for (String key : keys) {
                String serviceName = key.substring(KEY_PREFIX.length());
                ServiceType type;
                try {
                    type = ServiceType.valueOf(serviceName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    continue;
                }

                String timestamp = jedis.get(key);
                if (timestamp != null) {
                    Instant wasOnline = lastSeen.get(type);
                    lastSeen.put(type, Instant.now());

                    if (wasOnline == null || Duration.between(wasOnline, Instant.now()).getSeconds() >= SERVICE_TIMEOUT_SECONDS) {
                        logger.info("Service '{}' is now online (ping received)", serviceName);
                    }
                }
            }

            // Only expire services if Redis is healthy (not during outages)
            for (Map.Entry<ServiceType, Instant> entry : lastSeen.entrySet()) {
                if (Duration.between(entry.getValue(), Instant.now()).getSeconds() >= SERVICE_TIMEOUT_SECONDS) {
                    logger.info("Service '{}' appears offline (no ping for {}s)",
                            entry.getKey().name().toLowerCase(), SERVICE_TIMEOUT_SECONDS);
                    lastSeen.remove(entry.getKey());
                }
            }

            lastSuccessfulSync = Instant.now();
            consecutiveFailures.set(0);

        } catch (JedisException e) {
            int failures = consecutiveFailures.incrementAndGet();
            if (failures <= 3 || failures % 10 == 0) {
                logger.warn("Service registry sync error (failure #{}): {}", failures, e.getMessage());
            }
            // Don't clear lastSeen during Redis outages — preserve last known state
        }
    }

    // ── Redis pool ──────────────────────────────────────────────────────────

    private JedisPool buildPool() {
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
}
