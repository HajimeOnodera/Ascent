package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.dungeon.DungeonServiceRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class DroomCommand extends Command {

    public DroomCommand() {
        super("droom");

        var actionArg = ArgumentType.String("action");
        var nameArg = ArgumentType.String("name");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            if (DungeonServiceRegistry.get() != null) {
                DungeonServiceRegistry.get().handleDroomCommand(player, null, null);
            } else {
                player.sendMessage("§cDungeon system is not initialized on this server.");
            }
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String action = context.get(actionArg).toLowerCase();
            if (DungeonServiceRegistry.get() != null) {
                DungeonServiceRegistry.get().handleDroomCommand(player, action, null);
            } else {
                player.sendMessage("§cDungeon system is not initialized on this server.");
            }
        }, actionArg);

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String action = context.get(actionArg).toLowerCase();
            String name = context.get(nameArg);
            if (DungeonServiceRegistry.get() != null) {
                DungeonServiceRegistry.get().handleDroomCommand(player, action, name);
            } else {
                player.sendMessage("§cDungeon system is not initialized on this server.");
            }
        }, actionArg, nameArg);
    }
}
