package fun.ascent.common.redis;

public record RedisConfig(String host, int port, String password) {

    public static RedisConfig fromEnv() {
        String host = env("REDIS_HOST", "127.0.0.1");
        int port = Integer.parseInt(env("REDIS_PORT", "6379"));
        String password = env("REDIS_PASSWORD", "");
        return new RedisConfig(host, port, password.isBlank() ? null : password);
    }

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v.trim() : fallback;
    }
}
