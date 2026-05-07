package fun.ascent.common;

import fun.ascent.common.redis.RedisConfig;
import fun.ascent.common.redis.RedisManager;
import fun.ascent.common.user.UserManager;
import fun.ascent.common.service.redis.ServerOutboundMessage;

public class Ascent {
    public static void initialize() {
        // Initialize Redis
        if (!RedisManager.isInitialized()) {
            RedisManager.connect(RedisConfig.fromEnv());
        }

        // Initialize Outbound Messages
        ServerOutboundMessage.init();

        // Initialize MongoDB / UserManager
        String mongoUri = System.getenv().getOrDefault("MONGODB_URI", "mongodb://127.0.0.1:27017");
        UserManager.init(mongoUri);

        System.out.println("[Ascent] System initialized (Redis, MongoDB, Outbound Messages)");
    }
}
