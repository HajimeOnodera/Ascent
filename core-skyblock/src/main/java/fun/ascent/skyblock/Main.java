package fun.ascent.skyblock;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.island.IslandManager;
import fun.ascent.skyblock.island.IslandSystemListener;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.actionbar.ActionBarManager;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.player.combat.CombatListener;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.scoreboard.ScoreboardManager;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.player.skill.listener.SkillListeners;
import fun.ascent.skyblock.config.ServerConfig;
import fun.ascent.skyblock.minion.service.MinionManager;
import fun.ascent.skyblock.auction.AuctionRegistry;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.blocks.BlockManager;
import fun.ascent.skyblock.cmds.CommandHandler;
import fun.ascent.skyblock.crafting.RecipeRegistry;
import fun.ascent.skyblock.dungeon.DungeonManager;
import fun.ascent.skyblock.entity.mob.EntityRegistry;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.items.ItemDefinitions;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.listeners.ProfileListener;
import fun.ascent.skyblock.listeners.SkyblockChatListener;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.calendar.Calendar;
import fun.ascent.skyblock.shop.ShopRegistry;
import fun.ascent.skyblock.world.region.RegionListener;
import fun.ascent.skyblock.world.region.RegionManager;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fun.ascent.common.Ascent;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void initCore(ServerConfig config) {
        Ascent.initialize();

        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        ItemDefinitions.init();
        ItemRegistry.init();
        Reforge.init();

        EventManager.initialise();
        SkyblockNPCManager.init();
        BlockManager.initialize();
        Calendar.startTimeUpdates();
        ScoreboardManager.init();
        ActionBarManager.init();
        MinionManager.init();
        SkillRegistry.init();
        SkillListeners.register();
        CollectionRegistry.init();
        RecipeRegistry.init();
        CommandHandler.initialise();
        EntityRegistry.scanAndRegister("fun.ascent.skyblock.entity.mob.mobs");
        fun.ascent.skyblock.player.fishing.FishingModule.init();
        CombatListener.register();
        SkyblockChatListener.register();
        RegionManager.initialize();
        BazaarRegistry.initialise();
        AuctionRegistry.initialise();
        ShopRegistry.initialise();
        RegionListener.register(MinecraftServer.getGlobalEventHandler());
        ProfileListener.register(MinecraftServer.getGlobalEventHandler());
        IslandSystemListener.register(MinecraftServer.getGlobalEventHandler());
        
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
        if (serverType.equalsIgnoreCase("ISLAND")) {
            IslandManager.runVacantLoop();
        }
        
        DungeonManager.get().initialize();

        LOGGER.info("Core SkyBlock initialized successfully!");
    }

    public static void shutdown() {
        LOGGER.info("Shutting down SkyBlock Core...");
        ProfileManager.saveAllProfiles();
        MinecraftServer.stopCleanly();
    }

    public static void main(String[] args) {
        ServerConfig config = ServerConfig.load();
        
        // Initialize Minestom
        MinecraftServer server = MinecraftServer.init(config.auth());
        
        // Initialize Core SkyBlock
        initCore(config);
        
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
        if (serverType.equalsIgnoreCase("HUB")) {
            fun.ascent.skyblock.world.WorldHandler.initialise();
            fun.ascent.skyblock.entity.mob.ZonePopulationTicker.start();
            try {
                Class<?> hubManagerCls = Class.forName("fun.ascent.skyblock.hub.HubManager");
                hubManagerCls.getMethod("init").invoke(null);
            } catch (ClassNotFoundException e) {
                LOGGER.info("HubManager not found on classpath, skipping hub-specific NPC/world initialization.");
            } catch (Exception e) {
                LOGGER.error("Failed to initialize HubManager via reflection", e);
            }
        }
        
        LOGGER.info("Starting SkyBlock server (Type: {}) on {}:{}", serverType.toUpperCase(), config.host(), config.port());
        server.start(config.host(), config.port());
        
        // Registration
        String serverName = System.getenv().getOrDefault("ASCENT_SERVER_NAME", "skyblock-" + serverType.toLowerCase());
        String ADVERTISE_HOST = System.getenv().getOrDefault("ASCENT_ADVERTISE_HOST", "127.0.0.1");
        fun.ascent.common.redis.PingService.start(serverName, ADVERTISE_HOST, config.port());
    }
}

