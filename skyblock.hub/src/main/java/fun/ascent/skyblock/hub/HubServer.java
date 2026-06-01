package fun.ascent.skyblock.hub;

import fun.ascent.common.redis.PingService;
import fun.ascent.skyblock.Main;
import fun.ascent.skyblock.config.ServerConfig;
import fun.ascent.skyblock.world.WorldHandler;
import fun.ascent.skyblock.entity.mob.SpotSpawnerTicker;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HubServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HubServer.class);
    private static final String ADVERTISE_HOST = System.getenv().getOrDefault("ASCENT_ADVERTISE_HOST", "127.0.0.1");

    public static void main(String[] args) {
        ServerConfig config = ServerConfig.load();
        
        // Initialize Minestom
        MinecraftServer server = MinecraftServer.init(config.auth());
        
        // Initialize Core SkyBlock
        Main.initCore();
        
        // Hub Specific Initialization
        WorldHandler.initialise();
        HubManager.init();
        SpotSpawnerTicker.start();
        
        LOGGER.info("Starting SkyBlock HUB server on {}:{}", config.host(), config.port());
        server.start(config.host(), config.port());
        
        // Registration
        String serverName = System.getenv().getOrDefault("ASCENT_SERVER_NAME", "skyblock-hub");
        PingService.start(serverName, ADVERTISE_HOST, config.port());
    }
}
