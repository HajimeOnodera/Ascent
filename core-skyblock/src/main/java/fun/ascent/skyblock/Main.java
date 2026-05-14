package fun.ascent.skyblock;

import fun.ascent.common.Ascent;
import fun.ascent.common.redis.PingService;
import fun.ascent.skyblock.auction.AuctionRegistry;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.blocks.BlockManager;
import fun.ascent.skyblock.cmds.CommandHandler;
import fun.ascent.skyblock.crafting.RecipeRegistry;
import fun.ascent.skyblock.dungeon.DungeonManager;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.items.ItemDefinitions;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.listeners.ProfileListener;
import fun.ascent.skyblock.listeners.SkyblockChatListener;
import fun.ascent.skyblock.menus.shop.ShopRegistry;
import fun.ascent.skyblock.minion.service.MinionManager;
import fun.ascent.skyblock.player.actionbar.ActionBarManager;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.player.combat.CombatListener;
import fun.ascent.skyblock.entity.mob.EntityRegistry;
import fun.ascent.skyblock.entity.mob.ZonePopulationTicker;
import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.calendar.Calendar;
import fun.ascent.skyblock.config.ServerConfig;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.scoreboard.ScoreboardManager;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.player.skill.listener.SkillListeners;
import fun.ascent.skyblock.island.IslandManager;
import fun.ascent.skyblock.world.WorldHandler;
import fun.ascent.skyblock.world.region.RegionListener;
import fun.ascent.skyblock.world.region.RegionManager;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /** Host used by Velocity to reach this container. */
    private static final String ADVERTISE_HOST = System.getenv().getOrDefault("ASCENT_ADVERTISE_HOST", "127.0.0.1");

    static void main() {
        ServerConfig config = ServerConfig.load();
        Ascent.initialize();

        // ── Minestom ────────────────────────────────────────────────────────
        MinecraftServer server = MinecraftServer.init(config.auth());
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        ItemDefinitions.init();
        ItemRegistry.init();
        Reforge.init();
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
        boolean isHub = serverType.equalsIgnoreCase("HUB");

        if (isHub) {
            WorldHandler.initialise();
            ZonePopulationTicker.start();
            LOGGER.info("Initialized SkyBlock HUB features.");
        } else {
            IslandManager.runVacantLoop();
            LOGGER.info("Initialized SkyBlock ISLAND features.");
        }

        EventManager.initialise();
        SkyblockNPCManager.init();
        BlockManager.initialize();
        ShopRegistry.initialise();
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
        CombatListener.register();
        SkyblockChatListener.register();
        RegionManager.initialize();
        BazaarRegistry.initialise();
        AuctionRegistry.initialise();
        RegionListener.register(MinecraftServer.getGlobalEventHandler());
        ProfileListener.register(MinecraftServer.getGlobalEventHandler());
        DungeonManager.get().initialize();


        LOGGER.info("Starting SkyBlock server on {}:{}", config.host(), config.port());
        server.start(config.host(), config.port());
        LOGGER.info("SkyBlock server started successfully!");

        String serverName = System.getenv("ASCENT_SERVER_NAME");
        if (serverName == null || serverName.isBlank() || serverName.equals("skyblock")) {
            String base = (serverName == null || serverName.isBlank()) ? "skyblock" : serverName;
            serverName = base + "-" + java.util.UUID.randomUUID().toString().substring(0, 5);
        }

        // ── Ping (must start AFTER server.start()) ─────────────────────
        PingService.start(serverName, ADVERTISE_HOST, config.port());
    }

    public static void shutdown() {
        IslandManager.saveAll();
        WorldHandler.shutdown();
        MinecraftServer.stopCleanly();
    }
}