package fun.ascent.skyblock.dungeon.commands;

import fun.ascent.skyblock.dungeon.DungeonFloor;
import fun.ascent.skyblock.dungeon.DungeonInstance;
import fun.ascent.skyblock.dungeon.DungeonManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class DungeonCommand extends Command {

    public DungeonCommand() {
        super("dungeon");

        var floorArg = ArgumentType.String("floor");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            DungeonInstance dungeon = DungeonManager.get().createDungeon(DungeonFloor.FLOOR_7);
            DungeonManager.get().addPlayer(player, dungeon);
            player.sendMessage("§aGenerated Floor 7 dungeon. Teleporting...");
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String input = context.get(floorArg);
            DungeonFloor floor = DungeonFloor.fromString(input);
            if (floor == null) {
                player.sendMessage("§cUnknown floor: " + input);
                return;
            }
            DungeonInstance dungeon = DungeonManager.get().createDungeon(floor);
            DungeonManager.get().addPlayer(player, dungeon);
            player.sendMessage("§aTest dungeon " + floor.displayName() + ". Teleporting...");
        }, floorArg);
    }
}
