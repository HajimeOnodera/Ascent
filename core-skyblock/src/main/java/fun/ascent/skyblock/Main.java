package fun.ascent.skyblock;

import fun.ascent.skyblock.config.ServerConfig;
import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.calendar.Calendar;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.XpCommand;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.scoreboard.ScoreboardManager;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.player.skill.command.SkillsCommand;
import fun.ascent.skyblock.player.skill.listener.SkillListeners;
import fun.ascent.skyblock.proxy.ProxyTransferCommand;
import fun.ascent.skyblock.world.WorldManager;
import net.minestom.server.MinecraftServer;

public class Main {

    public static void main(String[] args) {
        ServerConfig config = ServerConfig.load();
        MinecraftServer server = MinecraftServer.init(config.auth());
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        WorldManager.initialise();
        EventManager.initialise();
        SkyblockNPCManager.init();
        Calendar.startTimeUpdates();
        ScoreboardManager.init();
        ProfileManager.initialise();

        SkillRegistry.init();
        SkillListeners.register();
        MinecraftServer.getCommandManager().register(new SkillsCommand());
        MinecraftServer.getCommandManager().register(new XpCommand());
        MinecraftServer.getCommandManager().register(new fun.ascent.skyblock.menus.command.GuiCommand());
        MinecraftServer.getCommandManager().register(new ProxyTransferCommand("lobby", "hub"));

        System.out.println("[Skyblock] Starting the Server on " + config.host() + ":" + config.port());
        server.start(config.host(), config.port());
        System.out.println("[Skyblock] Started the Server");
    }
}
