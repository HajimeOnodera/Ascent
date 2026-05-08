package fun.ascent.common.service.redis;

import fun.ascent.common.redis.RedisConfig;
import fun.ascent.common.redis.RedisManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ServiceRedisManager {
    private static final Map<String, Consumer<String>> channels = new ConcurrentHashMap<>();
    private static JedisPubSub pubSub;

    public static void connect(RedisConfig config) {
        if (!RedisManager.isInitialized()) {
            RedisManager.connect(config);
        }

        Thread.startVirtualThread(() -> {
            try (Jedis jedis = RedisManager.get().getResource()) {
                pubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        Consumer<String> consumer = channels.get(channel);
                        if (consumer != null) {
                            consumer.accept(message);
                        }
                    }
                };
                jedis.subscribe(pubSub, "placeholder_initial"); // Subscription starts here
            }
        });
    }

    public static void registerChannel(String channel, Consumer<String> handler) {
        channels.put(channel, handler);
        if (pubSub != null && pubSub.isSubscribed()) {
            pubSub.subscribe(channel);
        }
    }

    public static void publish(String channel, String message) {
        try (Jedis jedis = RedisManager.get().getResource()) {
            jedis.publish(channel, message);
        }
    }

    public static RedisManager getRedisManager() {
        return RedisManager.get();
    }
}
