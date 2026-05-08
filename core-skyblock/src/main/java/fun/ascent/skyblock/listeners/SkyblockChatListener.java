package fun.ascent.skyblock.listeners;

import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerChatEvent;
import net.kyori.adventure.text.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.escapeMiniMessage;
import static fun.ascent.common.StringUtility.text;

public class SkyblockChatListener {


    public static List<String> messages = new ArrayList<>();

    public static String SB_CHAT_LOG ="https://discord.com/api/webhooks/1502192865424576603/C_nt5jP_HrRpBklB-Z1U8Q2rOZwLUkiKXDt6_HJFN_WrUaUXZssWOLhfp6GHq-3j32rg";
    public static void register() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
            User user = UserManager.getUser(event.getPlayer().getUuid());
            Component displayName = user.getDisplayName();

            String messageColor = (user.getRank() == Rank.DEFAULT) ? "<gray>" : "<white>";

            Component message = text("<gray>: " + messageColor + escapeMiniMessage(event.getRawMessage()));
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage(displayName.append(message)));

            String messageContent = "[" + MinecraftServer.getServer().getPort() + "] " +
                    event.getPlayer().getUsername() + ": " + event.getRawMessage();

            messages.add(messageContent);
            System.out.println("Put Message " + messages.size());
            if(messages.size() >= 100){
                System.out.println("Sending Messages");
                StringBuilder msgBuilder = new StringBuilder();
                messages.forEach((msg) -> {
                    msgBuilder.append(msg);
                    msgBuilder.append("\\n");
                });
                String jsonPayload = "{\"content\": \"" + msgBuilder + "\"}";
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
                    messages.clear();
                } catch (Exception e) {
                    System.out.println("ERROR WHILE SENDING WEBHOOK, PLEASE CHECK");
                    System.out.println(e.getMessage());
                    System.out.println(jsonPayload);
                }
            }
        });
    }
}
