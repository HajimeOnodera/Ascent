package fun.ascent.proxy;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.friends.events.response.*;
import fun.ascent.common.service.FromServiceChannels;
import fun.ascent.common.service.redis.ServiceToClient;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class RedisPropagateFriendEvent implements ServiceToClient {
    private final ProxyServer proxy;

    public RedisPropagateFriendEvent(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.PROPAGATE_FRIEND_EVENT;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        String eventType = message.getString("eventType");
        String eventData = message.getString("eventData");
        JSONArray participantsArray = message.getJSONArray("participants");

        List<UUID> participants = participantsArray.toList().stream()
                .map(obj -> UUID.fromString(obj.toString()))
                .toList();

        FriendEvent event = parseEvent(eventType, eventData);
        if (event == null) return new JSONObject().put("success", false);

        for (UUID participantUUID : participants) {
            proxy.getPlayer(participantUUID).ifPresent(player -> handleEventForPlayer(player, event));
        }

        return new JSONObject().put("success", true);
    }

    private FriendEvent parseEvent(String eventType, String eventData) {
        try {
            FriendEvent templateEvent = FriendEvent.findFromType(eventType);
            return (FriendEvent) templateEvent.getSerializer().deserialize(eventData);
        } catch (Exception e) {
            return null;
        }
    }

    private void handleEventForPlayer(Player player, FriendEvent event) {
        if (event instanceof FriendRequestSentResponseEvent e) {
            sendMessage(player, "§aFriend request sent to §e" + e.getTargetName() + "§a!");
        } else if (event instanceof FriendRequestReceivedResponseEvent e) {
            player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
            player.sendMessage(Component.text("§aFriend request from §e" + e.getSenderName()));
            
            Component accept = Component.text(" §a§l[ACCEPT]")
                    .hoverEvent(Component.text("§eClick to accept friend request"))
                    .clickEvent(ClickEvent.runCommand("/f accept " + e.getSenderName()));
            
            Component deny = Component.text(" §c§l[DENY]")
                    .hoverEvent(Component.text("§eClick to deny friend request"))
                    .clickEvent(ClickEvent.runCommand("/f deny " + e.getSenderName()));
            
            player.sendMessage(Component.text("§eClick ").append(accept).append(Component.text(" §eor ")).append(deny));
            player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
        } else if (event instanceof FriendAddedResponseEvent e) {
            String otherName = player.getUniqueId().equals(e.getPlayer1()) ? e.getPlayer2Name() : e.getPlayer1Name();
            sendMessage(player, "§aYou are now friends with §e" + otherName + "§a!");
        } else if (event instanceof FriendDeniedResponseEvent e) {
            sendMessage(player, "§e" + e.getDenierName() + " §cdenied your friend request.");
        } else if (event instanceof FriendRemovedResponseEvent e) {
            if (player.getUniqueId().equals(e.getRemover())) {
                sendMessage(player, "§cRemoved friend.");
            } else {
                sendMessage(player, "§e" + e.getRemoverName() + " §cremoved you from their friends list.");
            }
        } else if (event instanceof FriendJoinNotificationEvent e) {
            player.sendMessage(Component.text("§aFriend > " + e.getFriendName() + " §7joined."));
        } else if (event instanceof FriendLeaveNotificationEvent e) {
            player.sendMessage(Component.text("§aFriend > " + e.getFriendName() + " §7left."));
        }
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
        player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
    }
}
