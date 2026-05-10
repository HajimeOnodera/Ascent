package fun.ascent.proxy.manager;

import fun.ascent.proxy.config.*;
import fun.ascent.proxy.service.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.party.PendingParty;
import fun.ascent.common.party.events.*;
import fun.ascent.common.protocol.objects.party.SendPartyEventToServiceProtocolObject;
import fun.ascent.common.service.ServiceType;

public class PartyManager {
    private static final ProxyServiceGateway<PartyEvent> PARTY_SERVICE = new ProxyServiceGateway<>(
            ServiceType.PARTY,
            "Party",
            SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage::new
    );

    public static boolean ensureAvailable(Player player) {
        return PARTY_SERVICE.ensureAvailable(player);
    }

    public static void invitePlayer(Player player, String targetName, ProxyServer proxy) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        Player target = PARTY_SERVICE.findOnlinePlayer(proxy, player, targetName, "Couldn't find an online player with that name!")
                .orElse(null);
        if (target == null) return;

        if (target.getUniqueId().equals(player.getUniqueId())) {
            PARTY_SERVICE.sendError(player, "You cannot invite yourself to a party!");
            return;
        }

        PARTY_SERVICE.send(new PartyInviteEvent(PendingParty.create(target.getUniqueId(), player.getUniqueId())));
    }

    public static void acceptInvite(Player player, String inviterName, ProxyServer proxy) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        Player inviter = PARTY_SERVICE.findOnlinePlayer(proxy, player, inviterName, "Couldn't find that player!").orElse(null);
        if (inviter == null) return;

        PARTY_SERVICE.send(new PartyAcceptInviteEvent(player.getUniqueId(), inviter.getUniqueId()));
    }

    public static void leaveParty(Player player) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        PARTY_SERVICE.send(new PartyLeaveRequestEvent(player.getUniqueId()));
    }

    public static void disbandParty(Player player) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        PARTY_SERVICE.send(new PartyDisbandRequestEvent(player.getUniqueId()));
    }

    public static void kickPlayer(Player player, String targetName, ProxyServer proxy) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        Player target = PARTY_SERVICE.findOnlinePlayer(proxy, player, targetName, "Couldn't find that player!").orElse(null);
        if (target == null) return;

        PARTY_SERVICE.send(new PartyKickRequestEvent(player.getUniqueId(), target.getUniqueId()));
    }

    public static void transferLeadership(Player player, String targetName, ProxyServer proxy) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        Player target = PARTY_SERVICE.findOnlinePlayer(proxy, player, targetName, "Couldn't find that player!").orElse(null);
        if (target == null) return;

        PARTY_SERVICE.send(new PartyTransferRequestEvent(player.getUniqueId(), target.getUniqueId()));
    }

    public static void promotePlayer(Player player, String targetName, ProxyServer proxy) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        Player target = PARTY_SERVICE.findOnlinePlayer(proxy, player, targetName, "Couldn't find that player!").orElse(null);
        if (target == null) return;

        PARTY_SERVICE.send(new PartyPromoteRequestEvent(player.getUniqueId(), target.getUniqueId()));
    }

    public static void demotePlayer(Player player, String targetName, ProxyServer proxy) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        Player target = PARTY_SERVICE.findOnlinePlayer(proxy, player, targetName, "Couldn't find that player!").orElse(null);
        if (target == null) return;

        PARTY_SERVICE.send(new PartyDemoteRequestEvent(player.getUniqueId(), target.getUniqueId()));
    }

    public static void warpParty(Player player) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        PARTY_SERVICE.send(new PartyWarpRequestEvent(player.getUniqueId()));
    }

    public static void listParty(Player player) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        PARTY_SERVICE.send(new PartyListRequestEvent(player.getUniqueId()));
    }

    public static void sendChatMessage(Player player, String message) {
        if (PARTY_SERVICE.ensureAvailable(player)) return;
        PARTY_SERVICE.send(new PartyChatMessageEvent(player.getUniqueId(), message));
    }
}
