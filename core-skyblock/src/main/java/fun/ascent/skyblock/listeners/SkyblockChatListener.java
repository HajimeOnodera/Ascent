package fun.ascent.skyblock.listeners;

import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

public class SkyblockChatListener {


    public static String SB_CHAT_LOG ="https://discord.com/api/webhooks/1502192865424576603/C_nt5jP_HrRpBklB-Z1U8Q2rOZwLUkiKXDt6_HJFN_WrUaUXZssWOLhfp6GHq-3j32rg";
    public static void register() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
            User user = UserManager.getUser(event.getPlayer().getUuid());
            Component displayName = user.getDisplayName();

            TextColor messageColor = (user.getRank() == Rank.DEFAULT) ? NamedTextColor.GRAY : NamedTextColor.WHITE;

            TextComponent message = Component.text(": ").color(NamedTextColor.GRAY)
                    .append(Component.text(event.getRawMessage()).color(messageColor));
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage(displayName.append(message)));

            String messageContent = "["+MinecraftServer.getServer().getPort() + "] " + event.getPlayer().getUsername() + ": " + event.getRawMessage();

            String jsonPayload = "{\"content\":\"" + messageContent + "\"}";
            try {
                URL url = URI.create(SB_CHAT_LOG).toURL();
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.addRequestProperty("Content-Type", "application/json");
                conn.addRequestProperty("User-Agent", "Java-Webhook");
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                os.write(jsonPayload.getBytes());
                os.flush();
                conn.getInputStream().close();
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("ERROR WHILE SENDING WEBHOOK, PLEASE CHECK");
                System.out.println(e.getMessage());
            }
        });
    }
}
