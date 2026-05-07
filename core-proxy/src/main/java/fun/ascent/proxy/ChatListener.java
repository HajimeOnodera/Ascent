package fun.ascent.proxy;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import fun.ascent.common.user.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ChatListener {
    private final com.velocitypowered.api.proxy.ProxyServer proxy;

    public ChatListener(com.velocitypowered.api.proxy.ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!event.getResult().isAllowed()) return;

        // Cancel the original event so we can send our formatted message
        event.setResult(PlayerChatEvent.ChatResult.denied());

        Component displayName = UserManager.getDisplayName(player.getUniqueId());
        Component message = Component.text(": ").color(NamedTextColor.GRAY)
                .append(Component.text(event.getMessage()).color(NamedTextColor.WHITE));

        proxy.sendMessage(displayName.append(message));
    }
}
