package fun.ascent.proxy.listener;

import fun.ascent.proxy.manager.*;
import fun.ascent.proxy.config.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.friends.events.response.*;
import fun.ascent.common.service.FromServiceChannels;
import fun.ascent.common.service.redis.ServiceToClient;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.UUID;

import static fun.ascent.common.StringUtility.text;

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
            sendMessage(player, "<green>Friend request sent to <yellow>" + e.getTargetName() + "<green>!");
        } else if (event instanceof FriendRequestReceivedResponseEvent e) {
            player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
            player.sendMessage(text("<green>Friend request from <yellow>" + e.getSenderName()));
            
            Component accept = text(" <green><bold>[ACCEPT]")
                    .hoverEvent(text("<yellow>Click to accept friend request"))
                    .clickEvent(ClickEvent.runCommand("/f accept " + e.getSenderName()));
            
            Component deny = text(" <red><bold>[DENY]")
                    .hoverEvent(text("<yellow>Click to deny friend request"))
                    .clickEvent(ClickEvent.runCommand("/f deny " + e.getSenderName()));
            
            player.sendMessage(text("<yellow>Click ").append(accept).append(text(" <yellow>or ")).append(deny));
            player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
        } else if (event instanceof FriendAddedResponseEvent e) {
            String otherName = player.getUniqueId().equals(e.getPlayer1()) ? e.getPlayer2Name() : e.getPlayer1Name();
            sendMessage(player, "<green>You are now friends with <yellow>" + otherName + "<green>!");
        } else if (event instanceof FriendDeniedResponseEvent e) {
            sendMessage(player, "<yellow>" + e.getDenierName() + " <red>denied your friend request.");
        } else if (event instanceof FriendRemovedResponseEvent e) {
            if (player.getUniqueId().equals(e.getRemover())) {
                sendMessage(player, "<red>Removed friend.");
            } else {
                sendMessage(player, "<yellow>" + e.getRemoverName() + " <red>removed you from their friends list.");
            }
        } else if (event instanceof FriendJoinNotificationEvent e) {
            player.sendMessage(text("<green>Friend > " + e.getFriendName() + " <gray>joined."));
        } else if (event instanceof FriendLeaveNotificationEvent e) {
            player.sendMessage(text("<green>Friend > " + e.getFriendName() + " <gray>left."));
        }
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
        player.sendMessage(text(message));
        player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
    }
}

