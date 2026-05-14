package fun.ascent.skyblock.island;

import fun.ascent.common.redis.PingService;
import fun.ascent.skyblock.Main;
import fun.ascent.skyblock.cmds.CommandHandler;
import fun.ascent.skyblock.config.ServerConfig;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IslandServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(IslandServer.class);
    private static final String ADVERTISE_HOST = System.getenv().getOrDefault("ASCENT_ADVERTISE_HOST", "127.0.0.1");

    static void main(String[] args) {
        ServerConfig config = ServerConfig.load();
        
        // Initialize Minestom
        MinecraftServer server = MinecraftServer.init(config.auth());
        
        // Initialize Core SkyBlock
        Main.initCore(config);
        
        // Island Specific Initialization
        IslandManager.runVacantLoop();
        fun.ascent.skyblock.island.listener.IslandJoinListener.register(MinecraftServer.getGlobalEventHandler());
        
        CommandHandler.register(new IslandCommand());
        
        LOGGER.info("Starting SkyBlock ISLAND server on {}:{}", config.host(), config.port());
        server.start(config.host(), config.port());
        
        // Registration
        String serverName = System.getenv().getOrDefault("ASCENT_SERVER_NAME", "skyblock-island");
        PingService.start(serverName, ADVERTISE_HOST, config.port());
    }
}
