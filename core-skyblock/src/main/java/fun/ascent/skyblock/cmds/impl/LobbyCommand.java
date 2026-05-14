package fun.ascent.skyblock.cmds.impl;

import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.util.ProxyTransfer;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import net.minestom.server.command.builder.Command;

import static fun.ascent.common.StringUtility.text;

public class LobbyCommand extends Command {

    public LobbyCommand() {
        super("lobby", "l");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            
            String targetServer = ServerLookup.findAnyByPrefix("lobby");
            if (targetServer == null) {
                player.sendMessage(text("<red>The Main Lobby is currently unavailable!"));
                return;
            }
            
            player.sendMessage(text("<green>Sending you to the Main Lobby..."));
            if (player.getActiveProfile() != null) {
                ProfileManager.saveProfile(player.getActiveProfile().profileID);
            }
            ProxyTransfer.send(player, targetServer);
        });
    }
}
