package fun.ascent.skyblock;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.actionbar.ActionBarManager;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.player.combat.CombatListener;
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
        CombatListener.register();
        SkyblockChatListener.register();
        RegionManager.initialize();
        BazaarRegistry.initialise();
        AuctionRegistry.initialise();
        RegionListener.register(MinecraftServer.getGlobalEventHandler());
        ProfileListener.register(MinecraftServer.getGlobalEventHandler());
        DungeonManager.get().initialize();

        LOGGER.info("Core SkyBlock initialized successfully!");
    }

    public static void shutdown() {
        MinecraftServer.stopCleanly();
    }
}
