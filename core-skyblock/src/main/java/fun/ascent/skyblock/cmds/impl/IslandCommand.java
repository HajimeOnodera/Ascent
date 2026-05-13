package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.island.Island;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import net.minestom.server.command.builder.Command;

public class IslandCommand extends Command {

    public IslandCommand() {
        super("island","is");
        setDefaultExecutor((sender,_) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            SkyblockProfile profile = player.getActiveProfile();
            if(profile == null){
                player.sendMessage("§cYou don't have an active profile!");
                return;
            }
            Island island = profile.island;
            if (island == null) {
                player.sendMessage("§cIsland system error!");
                return;
            }

            if (island.isLoaded()) {
                player.setInstance(island.getInstance(), profile.getSpawnPos());
                player.sendMessage("§aTeleporting to your island!");
            } else {
                player.sendMessage("§eYour island is loading, please wait...");
                island.load().thenAccept(instance -> {
                    if (instance != null) {
                        player.setInstance(instance, profile.getSpawnPos());
                        player.sendMessage("§aIsland loaded! Teleporting...");
                    } else {
                        player.sendMessage("§cFailed to load island!");
                    }
                });
            }
        });
    }
}
