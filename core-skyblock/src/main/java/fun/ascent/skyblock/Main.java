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

    public static void initCore() {
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
        fun.ascent.skyblock.player.level.SkyBlockLevelRequirement.loadFromYaml();
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
}

