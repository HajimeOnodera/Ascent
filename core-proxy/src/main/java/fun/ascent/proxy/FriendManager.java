package fun.ascent.proxy;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.friends.FriendEvent;
import fun.ascent.common.friends.events.*;

import fun.ascent.common.service.ProxyService;
import fun.ascent.common.service.ServiceType;
import fun.ascent.common.protocol.objects.friend.SendFriendEventToServiceProtocolObject;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class FriendManager {
    private static final ProxyService friendService = new ProxyService(ServiceType.FRIEND);

    public static void addFriend(Player player, String targetName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        UUID targetUUID = proxy.getPlayer(targetName).map(Player::getUniqueId).orElse(null);
        if (targetUUID == null) {
            sendError(player, "Couldn't find an online player with that name! (Offline support coming soon)");
            return;
        }

        if (targetUUID.equals(player.getUniqueId())) {
            sendError(player, "You cannot add yourself as a friend!");
            return;
        }

        FriendAddRequestEvent event = new FriendAddRequestEvent(player.getUniqueId(), targetUUID, player.getUsername(), targetName);
        sendEventToService(event, player);
    }

    public static void acceptRequest(Player player, String senderName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        UUID senderUUID = proxy.getPlayer(senderName).map(Player::getUniqueId).orElse(null);
        if (senderUUID == null) {
            sendError(player, "Couldn't find that player!");
            return;
        }

        FriendAcceptRequestEvent event = new FriendAcceptRequestEvent(player.getUniqueId(), senderUUID, player.getUsername(), senderName);
        sendEventToService(event, player);
    }

    public static void denyRequest(Player player, String senderName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        UUID senderUUID = proxy.getPlayer(senderName).map(Player::getUniqueId).orElse(null);
        if (senderUUID == null) {
            sendError(player, "Couldn't find that player!");
            return;
        }

        FriendDenyRequestEvent event = new FriendDenyRequestEvent(player.getUniqueId(), senderUUID, player.getUsername());
        sendEventToService(event, player);
    }

    public static void removeFriend(Player player, String targetName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        UUID targetUUID = proxy.getPlayer(targetName).map(Player::getUniqueId).orElse(null);
        if (targetUUID == null) {
            sendError(player, "Couldn't find that player!");
            return;
        }

        FriendRemoveRequestEvent event = new FriendRemoveRequestEvent(player.getUniqueId(), targetUUID, player.getUsername(), targetName);
        sendEventToService(event, player);
    }

    private static boolean isServiceOnline(Player player) {
        ServiceRegistryManager registry = CoreProxy.getServiceRegistry();
        if (registry == null || registry.isServiceOnline(ServiceType.FRIEND)) {
            sendServiceOfflineError(player);
            return true;
        }
        return false;
    }

    private static void sendEventToService(FriendEvent event, Player player) {
        var message = new SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage(event);
        friendService.handleRequest(message);
    }

    private static void sendError(Player player, String message) {
        player.sendMessage(Component.text("§c" + message));
    }

    private static void sendServiceOfflineError(Player player) {
        player.sendMessage(Component.text("§cThe Friend service is currently offline. Please try again later."));
    }
}
