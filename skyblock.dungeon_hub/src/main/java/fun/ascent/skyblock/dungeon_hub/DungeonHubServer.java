package fun.ascent.skyblock.dungeon_hub;

import fun.ascent.common.redis.PingService;
import fun.ascent.skyblock.Main;
import fun.ascent.skyblock.config.ServerConfig;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DungeonHubServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DungeonHubServer.class);
    private static final String ADVERTISE_HOST = System.getenv().getOrDefault("ASCENT_ADVERTISE_HOST", "127.0.0.1");

    public static void main(String[] args) {
        ServerConfig config = ServerConfig.load();
        MinecraftServer server = MinecraftServer.init(config.auth());
        Main.initCore();
        DungeonHubManager.init();

        LOGGER.info("Starting Dungeon Hub server on {}:{}", config.host(), config.port());
        server.start(config.host(), config.port());

        String serverName = System.getenv().getOrDefault("ASCENT_SERVER_NAME", "dungeon-hub");
        PingService.start(serverName, ADVERTISE_HOST, config.port());
    }
}
