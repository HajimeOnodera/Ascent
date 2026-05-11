package fun.ascent.lobby.listener;

import fun.ascent.common.achievement.AchievementCategory;
import fun.ascent.common.achievement.AchievementManager;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;
import net.kyori.adventure.text.Component;

import static fun.ascent.common.StringUtility.escapeMiniMessage;
import static fun.ascent.common.StringUtility.text;

public class LobbyChatListener {

    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
            User user = UserManager.getUser(event.getPlayer().getUuid());
            Component displayName = user.getDisplayName();
            
            String messageColor = (user.getRank() == Rank.DEFAULT) ? "<gray>" : "<white>";
            
            Component message = text("<gray>: " + messageColor + escapeMiniMessage(event.getRawMessage()));
            
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage(displayName.append(message)));
            
            AchievementManager.unlock(event.getPlayer(), AchievementCategory.GENERAL, "Let the world hear your voice!");
        });
    }
}
