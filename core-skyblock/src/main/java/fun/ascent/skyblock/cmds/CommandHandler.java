package fun.ascent.skyblock.cmds;

import fun.ascent.skyblock.entity.mob.command.SpawnMobCommand;
import fun.ascent.skyblock.item.command.ItemCommand;
import fun.ascent.skyblock.minion.command.MinionCommand;
import fun.ascent.skyblock.player.level.XpCommand;
import fun.ascent.skyblock.player.skill.command.SkillsCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;

public class CommandHandler {

    public static CommandManager commandManager;

    public static void initialise(){
        commandManager = MinecraftServer.getCommandManager();
        register(new SpawnMobCommand());
        register(new ItemCommand());
        register(new SkillsCommand());
        register(new MinionCommand());
        register(new XpCommand());

    }

    public static void register(Command command){
        commandManager.register(command);
    }

}
