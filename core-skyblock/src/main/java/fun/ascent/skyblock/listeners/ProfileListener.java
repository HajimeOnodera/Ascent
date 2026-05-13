package fun.ascent.skyblock.listeners;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class ProfileListener {
    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerDisconnectEvent.class, event -> {
            if (event.getPlayer() instanceof SkyblockPlayer player) {
                if (player.getActiveProfile() != null) {
                    System.out.println("[Skyblock] Saving profile for " + player.getUsername());
                    ProfileManager.saveProfile(player.getActiveProfile().profileID);
                }
            }
        });
    }
}
