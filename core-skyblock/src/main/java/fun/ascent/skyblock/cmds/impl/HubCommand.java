package fun.ascent.skyblock.cmds.impl;

import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.util.ProxyTransfer;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;

import static fun.ascent.common.StringUtility.text;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            
            String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
            if (serverType.equalsIgnoreCase("ISLAND")) {
                String targetServer = ServerLookup.findAnyByPrefix("skyblock");
                if (targetServer == null) {
                    player.sendMessage(text("<red>SkyBlock Hub is currently offline!"));
                    return;
                }
                player.sendMessage(text("<green>Sending you to SkyBlock Hub..."));
                ProxyTransfer.send(player, targetServer);
                return;
            }

            if (WorldHandler.getLobby() == null) {
                player.sendMessage(text("<red>SkyBlock Hub is currently unavailable!"));
                return;
            }
            
            player.sendMessage(text("<green>Teleporting to SkyBlock Hub..."));
            player.setInstance(WorldHandler.getLobby(), WorldHandler.getLobbySpawn());
            player.setGameMode(GameMode.SURVIVAL);
        });
    }
}
