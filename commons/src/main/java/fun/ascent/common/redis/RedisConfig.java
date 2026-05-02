package fun.ascent.common.redis;

/**
 * Redis connection settings resolved from environment variables.
 * Shared between backend servers and the Velocity proxy.
 *
 * Environment variables:
 *   REDIS_HOST     – hostname / IP (default: redis)
 *   REDIS_PORT     – port          (default: 6379)
 *   REDIS_PASSWORD – password      (default: none)
 */
public record RedisConfig(String host, int port, String password) {

    public static RedisConfig fromEnv() {
        String host = env("REDIS_HOST", "redis");
        int port = Integer.parseInt(env("REDIS_PORT", "6379"));
        String password = env("REDIS_PASSWORD", "");
        return new RedisConfig(host, port, password.isBlank() ? null : password);
    }

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v.trim() : fallback;
    }
}
