package fun.ascent.skyblock.cmds;

import fun.ascent.skyblock.bazaar.cmd.BZCommand;
import fun.ascent.skyblock.cmds.impl.DroomCommand;
import fun.ascent.skyblock.cmds.impl.DungeonCommand;
import fun.ascent.skyblock.entity.mob.command.SpawnMobCommand;
import fun.ascent.skyblock.hotm.command.HotmCommand;
import fun.ascent.skyblock.item.command.GetItemDataCommand;
import fun.ascent.skyblock.item.command.ItemCommand;
import fun.ascent.skyblock.item.command.SetReforgeCommand;
import fun.ascent.skyblock.menus.command.GuiCommand;
import fun.ascent.skyblock.minion.command.MinionCommand;
import fun.ascent.skyblock.player.level.XpCommand;
import fun.ascent.skyblock.player.level.command.LevelsCommand;
import fun.ascent.skyblock.player.level.command.SetSbLevelCommand;
import fun.ascent.skyblock.player.skill.command.SkillsCommand;
import fun.ascent.skyblock.player.stats.command.StatCommand;
import fun.ascent.common.command.RestartCommand;
import fun.ascent.skyblock.shop.command.ShopCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Modifier;
import java.util.Set;

public class CommandHandler {

    public static CommandManager commandManager;

    public static void initialise() {
        commandManager = MinecraftServer.getCommandManager();
        register(new SpawnMobCommand());
        register(new ItemCommand());
        register(new BZCommand());
        register(new SetReforgeCommand());
        register(new SkillsCommand());
        register(new MinionCommand());
        register(new XpCommand());
        register(new LevelsCommand());
        register(new SetSbLevelCommand());
        register(new GuiCommand());
        register(new HotmCommand());
        register(new StatCommand());
        register(new GetItemDataCommand());
        register(new RestartCommand());
        register(new DungeonCommand());
        register(new DroomCommand());
        register(new ShopCommand());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("fun.ascent.skyblock.cmds.impl", CommandHandler.class.getClassLoader()))
                .addClassLoaders(CommandHandler.class.getClassLoader()));
        Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> cmd : commands) {
            if (Modifier.isAbstract(cmd.getModifiers()) || cmd.isInterface()) continue;
            if (cmd.isAnonymousClass() || cmd.isLocalClass() || (cmd.isMemberClass() && !Modifier.isStatic(cmd.getModifiers()))) continue;
            if (!cmd.getName().startsWith("fun.ascent")) continue;
            try {
                register(cmd.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.err.println("[Skyblock] Failed to register CMD: " + cmd.getSimpleName());
                e.printStackTrace();
            }
        }
    }

    public static void register(Command command) {
        try {
            if (commandManager.commandExists(command.getName())) {
                return;
            }
            commandManager.register(command);
        } catch (Exception e) {
            System.err.println("[Skyblock] Warning: Could not register command '" + command.getName() + "': " + e.getMessage());
        }
    }
}
