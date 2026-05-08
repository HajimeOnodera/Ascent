package fun.ascent.proxy;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.party.PendingParty;
import fun.ascent.common.party.events.*;
import fun.ascent.common.service.ProxyService;
import fun.ascent.common.service.ServiceType;
import fun.ascent.common.protocol.objects.party.SendPartyEventToServiceProtocolObject;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class PartyManager {
    private static final ProxyService partyService = new ProxyService(ServiceType.PARTY);

    public static void invitePlayer(Player player, String targetName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        Player target = proxy.getPlayer(targetName).orElse(null);
        if (target == null) {
            sendError(player, "Couldn't find an online player with that name!");
            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            sendError(player, "You cannot invite yourself to a party!");
            return;
        }

        PendingParty pendingParty = PendingParty.create(target.getUniqueId(), player.getUniqueId());
        PartyInviteEvent event = new PartyInviteEvent(pendingParty);
        sendEventToService(event);
    }

    public static void acceptInvite(Player player, String inviterName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        Player inviter = proxy.getPlayer(inviterName).orElse(null);
        if (inviter == null) {
            sendError(player, "Couldn't find that player!");
            return;
        }

        PartyAcceptInviteEvent event = new PartyAcceptInviteEvent(player.getUniqueId(), inviter.getUniqueId());
        sendEventToService(event);
    }

    public static void leaveParty(Player player) {
        if (isServiceOnline(player)) return;
        PartyLeaveRequestEvent event = new PartyLeaveRequestEvent(player.getUniqueId());
        sendEventToService(event);
    }

    public static void disbandParty(Player player) {
        if (isServiceOnline(player)) return;
        PartyDisbandRequestEvent event = new PartyDisbandRequestEvent(player.getUniqueId());
        sendEventToService(event);
    }

    public static void kickPlayer(Player player, String targetName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        Player target = proxy.getPlayer(targetName).orElse(null);
        UUID targetUUID = target != null ? target.getUniqueId() : null;

        if (targetUUID == null) {
            sendError(player, "Couldn't find that player!");
            return;
        }

        PartyKickRequestEvent event = new PartyKickRequestEvent(player.getUniqueId(), targetUUID);
        sendEventToService(event);
    }

    public static void transferLeadership(Player player, String targetName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        Player target = proxy.getPlayer(targetName).orElse(null);
        if (target == null) {
            sendError(player, "Couldn't find that player!");
            return;
        }

        PartyTransferRequestEvent event = new PartyTransferRequestEvent(player.getUniqueId(), target.getUniqueId());
        sendEventToService(event);
    }

    public static void promotePlayer(Player player, String targetName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        Player target = proxy.getPlayer(targetName).orElse(null);
        if (target == null) {
            sendError(player, "Couldn't find that player!");
            return;
        }

        PartyPromoteRequestEvent event = new PartyPromoteRequestEvent(player.getUniqueId(), target.getUniqueId());
        sendEventToService(event);
    }

    public static void demotePlayer(Player player, String targetName, ProxyServer proxy) {
        if (isServiceOnline(player)) return;
        Player target = proxy.getPlayer(targetName).orElse(null);
        if (target == null) {
            sendError(player, "Couldn't find that player!");
            return;
        }

        PartyDemoteRequestEvent event = new PartyDemoteRequestEvent(player.getUniqueId(), target.getUniqueId());
        sendEventToService(event);
    }

    public static void warpParty(Player player) {
        if (isServiceOnline(player)) return;
        PartyWarpRequestEvent event = new PartyWarpRequestEvent(player.getUniqueId());
        sendEventToService(event);
    }

    public static void listParty(Player player) {
        if (isServiceOnline(player)) return;
        PartyListRequestEvent event = new PartyListRequestEvent(player.getUniqueId());
        sendEventToService(event);
    }

    public static void sendChatMessage(Player player, String message) {
        if (isServiceOnline(player)) return;
        PartyChatMessageEvent event = new PartyChatMessageEvent(player.getUniqueId(), message);
        sendEventToService(event);
    }

    private static boolean isServiceOnline(Player player) {
        ServiceRegistryManager registry = CoreProxy.getServiceRegistry();
        if (registry == null || registry.isServiceOnline(ServiceType.PARTY)) {
            sendServiceOfflineError(player);
            return true;
        }
        return false;
    }

    private static void sendEventToService(PartyEvent event) {
        var message = new SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage(event);
        partyService.handleRequest(message);
    }

    private static void sendError(Player player, String message) {
        player.sendMessage(Component.text("§c" + message));
    }

    private static void sendServiceOfflineError(Player player) {
        player.sendMessage(Component.text("§cThe Party service is currently offline. Please try again later."));
    }
}
