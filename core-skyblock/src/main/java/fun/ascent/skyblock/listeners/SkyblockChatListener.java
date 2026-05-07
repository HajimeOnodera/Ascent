package fun.ascent.skyblock.listeners;

import fun.ascent.common.user.UserManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SkyblockChatListener {

    public static void register() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
            Component displayName = UserManager.getDisplayName(event.getPlayer().getUuid());
            Component message = Component.text(": ").color(NamedTextColor.GRAY)
                    .append(Component.text(event.getRawMessage()).color(NamedTextColor.WHITE));
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage(displayName.append(message)));
        });
    }
}
