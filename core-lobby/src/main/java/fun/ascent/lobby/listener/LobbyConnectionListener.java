package fun.ascent.lobby.listener;

import fun.ascent.common.user.Rank;
import fun.ascent.common.user.UserManager;
import fun.ascent.lobby.world.LobbyWorld;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import static fun.ascent.common.StringUtility.text;

public class LobbyConnectionListener {

    public static void register(GlobalEventHandler handler, LobbyWorld world) {
        handler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(world.instance());
            event.getPlayer().setRespawnPoint(world.spawn());
        });

        handler.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            player.setDisplayName(UserManager.getDisplayName(player.getUuid()));
            
            if (!event.isFirstSpawn()) {
                return;
            }

            player.teleport(world.spawn());
            player.sendMessage(text("<yellow>Welcome to <gold>Ascent<yellow>! Pick a server to begin."));

            if (UserManager.getUser(player.getUuid()).getRank().isEqualOrHigherThan(Rank.VIP)) {
                player.setAllowFlying(true);
                player.setFlying(true);
            }
        });
    }
}
