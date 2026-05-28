package fun.ascent.skyblock.cmds.impl;

import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.util.ProxyTransfer;
import fun.ascent.skyblock.dungeon.DungeonServiceRegistry;
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
            executeDungeon(player, "FLOOR_7");
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String input = context.get(floorArg);
            executeDungeon(player, input);
        }, floorArg);
    }

    private void executeDungeon(SkyblockPlayer player, String floorInput) {
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");

        if (serverType.equalsIgnoreCase("DUNGEON")) {
            if (DungeonServiceRegistry.get() != null) {
                DungeonServiceRegistry.get().startDungeon(player, floorInput);
            } else {
                player.sendMessage("§cDungeon system is not initialized on this server.");
            }
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
            player.sendMessage("§aTest dungeon. Teleporting...");
            ProxyTransfer.send(player, targetServer);
        }
    }
}
