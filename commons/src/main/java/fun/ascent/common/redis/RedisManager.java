package fun.ascent.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public final class RedisManager {

    /** Redis key prefix for all server ping entries. */
    public static final String SERVERS_KEY_PREFIX = "ascent:servers:";

    /** TTL for each ping key — slightly more than the 1s ping interval. */
    public static final int PING_TTL_SECONDS = 5;

    private static RedisManager instance;

    private final JedisPool pool;

    private RedisManager(RedisConfig config) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setMaxWait(Duration.ofSeconds(5));

        if (config.password() != null) {
            this.pool = new JedisPool(poolConfig, config.host(), config.port(), 5000, config.password());
        } else {
            this.pool = new JedisPool(poolConfig, config.host(), config.port(), 5000);
        }
    }

    /**
     * Initialise the global instance. Call once per JVM.
     */
    public static void connect(RedisConfig config) {
        if (instance != null) {
            throw new IllegalStateException("RedisManager already initialised");
        }
        instance = new RedisManager(config);
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static RedisManager get() {
        if (instance == null) {
            throw new IllegalStateException("RedisManager not initialised");
        }
        return instance;
    }

    /** Borrows a Jedis connection from the pool. Always use in try-with-resources. */
    public Jedis getResource() {
        return pool.getResource();
    }

    public void close() {
        pool.close();
    }
}
