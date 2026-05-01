package fun.ascent.skyblock;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.npc.impl.SkyblockNPCManager;
import fun.ascent.skyblock.calendar.Calendar;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.scoreboard.ScoreboardManager;
import fun.ascent.skyblock.skill.SkillRegistry;
import fun.ascent.skyblock.skill.command.SkillsCommand;
import fun.ascent.skyblock.skill.listener.SkillListeners;
import fun.ascent.skyblock.world.WorldManager;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

public class Main {

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init(new Auth.Offline());
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        WorldManager.initialise();
        EventManager.initialise();
        SkyblockNPCManager.init();
        Calendar.startTimeUpdates();
        ScoreboardManager.init();

        SkillRegistry.init();
        SkillListeners.register();
        MinecraftServer.getCommandManager().register(new SkillsCommand());

        System.out.println("[Skyblock] Starting the Server");
        server.start("0.0.0.0", 25565);
        System.out.println("[Skyblock] Started the Server");
    }
}