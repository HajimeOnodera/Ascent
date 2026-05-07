package fun.ascent.skyblock.cmds;

import fun.ascent.skyblock.entity.mob.command.SpawnMobCommand;
import fun.ascent.skyblock.hotm.command.HotmCommand;
import fun.ascent.skyblock.item.command.ItemCommand;
import fun.ascent.skyblock.item.command.SetReforgeCommand;
import fun.ascent.skyblock.menus.command.GuiCommand;
import fun.ascent.skyblock.minion.command.MinionCommand;
import fun.ascent.skyblock.player.level.XpCommand;
import fun.ascent.skyblock.player.skill.command.SkillsCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import org.reflections.Reflections;

import java.util.Set;

public class CommandHandler {

    public static CommandManager commandManager;

    public static void initialise() {
        commandManager = MinecraftServer.getCommandManager();
        register(new SpawnMobCommand());
        register(new ItemCommand());
        register(new SetReforgeCommand());
        register(new SkillsCommand());
        register(new MinionCommand());
        register(new XpCommand());
        register(new GuiCommand());
        register(new HotmCommand());

        Reflections reflections = new Reflections("fun.ascent.skyblock.cmds.impl");
        Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> cmd : commands) {
            try {
                register(cmd.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.err.println("[Skyblock] Failed to register CMD: " + cmd.getSimpleName());
                e.printStackTrace();
            }
        }
    }

    public static void register(Command command) {
        commandManager.register(command);
    }
}