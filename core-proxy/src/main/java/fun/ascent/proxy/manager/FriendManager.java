package fun.ascent.proxy.manager;

import fun.ascent.proxy.service.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.friends.events.FriendAcceptRequestEvent;
import fun.ascent.common.friends.events.FriendAddRequestEvent;
import fun.ascent.common.friends.events.FriendDenyRequestEvent;
import fun.ascent.common.friends.events.FriendRemoveRequestEvent;
import fun.ascent.common.protocol.objects.friend.SendFriendEventToServiceProtocolObject;
import fun.ascent.common.service.ServiceType;

public class FriendManager {
    private static final ProxyServiceGateway<FriendEvent> FRIEND_SERVICE = new ProxyServiceGateway<>(
            ServiceType.FRIEND,
            "Friend",
            SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage::new
    );

    public static boolean ensureAvailable(Player player) {
        return FRIEND_SERVICE.ensureAvailable(player);
    }

    public static void addFriend(Player player, String targetName, ProxyServer proxy) {
        if (FRIEND_SERVICE.ensureAvailable(player)) return;
        Player target = FRIEND_SERVICE.findOnlinePlayer(proxy, player, targetName, "Couldn't find an online player with that name! (Offline support coming soon)")
                .orElse(null);
        if (target == null) return;

        if (target.getUniqueId().equals(player.getUniqueId())) {
            FRIEND_SERVICE.sendError(player, "You cannot add yourself as a friend!");
            return;
        }

        FRIEND_SERVICE.send(new FriendAddRequestEvent(player.getUniqueId(), target.getUniqueId(), player.getUsername(), target.getUsername()));
    }

    public static void acceptRequest(Player player, String senderName, ProxyServer proxy) {
        if (FRIEND_SERVICE.ensureAvailable(player)) return;
        Player sender = FRIEND_SERVICE.findOnlinePlayer(proxy, player, senderName, "Couldn't find that player!").orElse(null);
        if (sender == null) return;

        FRIEND_SERVICE.send(new FriendAcceptRequestEvent(player.getUniqueId(), sender.getUniqueId(), player.getUsername(), sender.getUsername()));
    }

    public static void denyRequest(Player player, String senderName, ProxyServer proxy) {
        if (FRIEND_SERVICE.ensureAvailable(player)) return;
        Player sender = FRIEND_SERVICE.findOnlinePlayer(proxy, player, senderName, "Couldn't find that player!").orElse(null);
        if (sender == null) return;

        FRIEND_SERVICE.send(new FriendDenyRequestEvent(player.getUniqueId(), sender.getUniqueId(), player.getUsername()));
    }

    public static void removeFriend(Player player, String targetName, ProxyServer proxy) {
        if (FRIEND_SERVICE.ensureAvailable(player)) return;
        Player target = FRIEND_SERVICE.findOnlinePlayer(proxy, player, targetName, "Couldn't find that player!").orElse(null);
        if (target == null) return;

        FRIEND_SERVICE.send(new FriendRemoveRequestEvent(player.getUniqueId(), target.getUniqueId(), player.getUsername(), target.getUsername()));
    }
}
