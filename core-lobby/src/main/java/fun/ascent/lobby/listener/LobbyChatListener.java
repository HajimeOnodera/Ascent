package fun.ascent.lobby.listener;

import fun.ascent.common.user.UserManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LobbyChatListener {

    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
            Component displayName = UserManager.getDisplayName(event.getPlayer().getUuid());
            Component message = Component.text(": ").color(NamedTextColor.GRAY)
                    .append(Component.text(event.getRawMessage()).color(NamedTextColor.WHITE));
            net.minestom.server.MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage(displayName.append(message)));
        });
    }
}
