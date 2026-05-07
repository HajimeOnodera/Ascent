package fun.ascent.lobby.listener;

import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class LobbyChatListener {

    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
            User user = UserManager.getUser(event.getPlayer().getUuid());
            Component displayName = user.getDisplayName();
            
            TextColor messageColor = (user.getRank() == Rank.DEFAULT) ? NamedTextColor.GRAY : NamedTextColor.WHITE;
            
            Component message = Component.text(": ").color(NamedTextColor.GRAY)
                    .append(Component.text(event.getRawMessage()).color(messageColor));
            
            net.minestom.server.MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage(displayName.append(message)));
        });
    }
}
