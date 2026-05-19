package fun.ascent.skyblock.dungeon.commands;

import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.util.ProxyTransfer;
import fun.ascent.skyblock.dungeon.DungeonFloor;
import fun.ascent.skyblock.dungeon.DungeonInstance;
import fun.ascent.skyblock.dungeon.DungeonManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class DungeonCommand extends Command {

    public DungeonCommand() {
        super("dungeon");

        var floorArg = ArgumentType.String("floor");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            startDungeon(player, DungeonFloor.FLOOR_7);
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String input = context.get(floorArg);
            DungeonFloor floor = DungeonFloor.fromString(input);
            if (floor == null) {
                player.sendMessage("§cUnknown floor: " + input);
                return;
            }
            startDungeon(player, floor);
        }, floorArg);
    }

    private void startDungeon(SkyblockPlayer player, DungeonFloor floor) {
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");

        if (serverType.equalsIgnoreCase("DUNGEON")) {
            // We're on the dungeon server - create locally
            DungeonInstance dungeon = DungeonManager.get().createDungeon(floor);
            DungeonManager.get().addPlayer(player, dungeon);
            player.sendMessage("§aTest dungeon " + floor.displayName() + ". Teleporting...");
        } else {
            // Transfer to dungeon server
            String targetServer = ServerLookup.findAnyByPrefix("dungeon-");
            if (targetServer == null) {
                player.sendMessage("§cDungeon server is currently offline!");
                return;
            }
            if (player.getActiveProfile() != null) {
                ProfileManager.saveProfile(player.getActiveProfile().profileID);
            }
            player.sendMessage("§aTest dungeon " + floor.displayName() + ". Teleporting...");
            ProxyTransfer.send(player, targetServer);
        }
    }
}
