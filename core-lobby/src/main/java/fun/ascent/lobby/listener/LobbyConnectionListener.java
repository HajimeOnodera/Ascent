package fun.ascent.lobby.listener;

import fun.ascent.common.achievement.AchievementCategory;
import fun.ascent.common.achievement.AchievementManager;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.UserManager;
import fun.ascent.lobby.world.LobbyWorld;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
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

            AchievementManager.registerFirstJoin(player);
            AchievementManager.unlock(player, AchievementCategory.GENERAL, "Achievement Get! Ascent Server!");

            Rank rank = UserManager.getUser(player.getUuid()).getRank();
            if (rank.isEqualOrHigherThan(Rank.VIP)) {
                player.setAllowFlying(true);
                player.setFlying(true);
                
                Component joinMsg;
                Component displayName = UserManager.getDisplayName(player.getUuid());
                
                if (rank.isEqualOrHigherThan(Rank.MVP_PLUS_PLUS)) {
                    joinMsg = text("<aqua>><red>><green>> ")
                            .append(displayName)
                            .append(text(" <gold>joined the lobby! "))
                            .append(text("<green><<red><<aqua><"));
                } else {
                    joinMsg = displayName.append(text(" <gold>joined the lobby!"));
                }
                
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage(joinMsg));
            }
        });
    }
}
