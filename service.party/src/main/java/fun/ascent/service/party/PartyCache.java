package fun.ascent.service.party;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.party.PendingParty;
import fun.ascent.common.party.events.*;
import fun.ascent.common.party.events.response.*;
import fun.ascent.common.service.FromServiceChannels;
import fun.ascent.common.service.redis.ServiceToServerManager;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PartyCache {
    private static final Map<UUID, FullParty> cachedParties = new ConcurrentHashMap<>();
    private static final Map<UUID, UUID> playerPartyMap = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> disconnectedPlayers = new ConcurrentHashMap<>();
    private static final long INVITE_EXPIRATION_MS = 5 * 60 * 1000; // 5 minutes
    private static final long DISCONNECT_TIMEOUT_MS = 5 * 60 * 1000; // 5 minutes
    private static ScheduledExecutorService expirationScheduler;

    public static void startExpirationChecker() {
        if (expirationScheduler != null) {
            return;
        }
        expirationScheduler = Executors.newSingleThreadScheduledExecutor();
        expirationScheduler.scheduleAtFixedRate(PartyCache::checkExpiredInvites, 30, 30, TimeUnit.SECONDS);
        expirationScheduler.scheduleAtFixedRate(PartyCache::checkDisconnectedPlayers, 30, 30, TimeUnit.SECONDS);
    }

    public static void stopExpirationChecker() {
        if (expirationScheduler != null) {
            expirationScheduler.shutdown();
            expirationScheduler = null;
        }
    }

    private static void checkExpiredInvites() {
        try {
            PartyDatabase db = new PartyDatabase(null);
            List<PendingParty> allInvites = db.getAllPendingInvites();
            long now = System.currentTimeMillis();

            for (PendingParty invite : allInvites) {
                org.bson.Document doc = PartyDatabase.pendingInvitesCollection.find(
                    new org.bson.Document("_id", invite.getResultPartyUUID().toString())
                ).first();
                if (doc != null) {
                    Long timestamp = doc.getLong("timestamp");
                    if (timestamp != null && now - timestamp > INVITE_EXPIRATION_MS) {
                        db.removePendingInvite(invite.getResultPartyUUID());
                        sendEvent(new PartyInviteExpiredResponseEvent(
                            FullParty.create(invite.getLeader(), invite.getInvitee()),
                            invite.getLeader(),
                            invite.getInvitee()
                        ));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to check expired party invites: " + e.getMessage());
        }
    }

    private static void checkDisconnectedPlayers() {
        try {
            long now = System.currentTimeMillis();
            List<UUID> toRemove = new ArrayList<>();

            for (Map.Entry<UUID, Long> entry : disconnectedPlayers.entrySet()) {
                if (now - entry.getValue() > DISCONNECT_TIMEOUT_MS) {
                    UUID player = entry.getKey();
                    FullParty party = getPartyByMember(player);
                    if (party != null) {
                        handleDisconnectTimeout(player, party);
                    }
                    toRemove.add(player);
                }
            }

            for (UUID player : toRemove) {
                disconnectedPlayers.remove(player);
            }
        } catch (Exception e) {
            System.err.println("Failed to check disconnected players: " + e.getMessage());
        }
    }

    private static void handleDisconnectTimeout(UUID player, FullParty party) {
        FullParty.Member member = party.getMembers().stream()
            .filter(m -> m.getUuid().equals(player))
            .findFirst().orElse(null);

        if (member != null) {
            boolean isLeader = member.getRole() == FullParty.Role.LEADER;
            if (isLeader) {
                // If leader disconnects for too long, disband party
                disbandParty(party, "Party leader disconnected");
            } else {
                // Otherwise just remove the member
                removeMemberFromParty(party, player);
                sendEvent(new PartyMemberDisconnectTimeoutResponseEvent(party, player, true));
            }
        }
    }

    public static FullParty getParty(UUID partyUuid) {
        FullParty cached = cachedParties.get(partyUuid);
        if (cached != null) {
            return cached;
        }

        PartyDatabase db = new PartyDatabase(null);
        FullParty party = db.getParty(partyUuid);
        if (party != null) {
            cachedParties.put(partyUuid, party);
            for (FullParty.Member member : party.getMembers()) {
                playerPartyMap.put(member.getUuid(), partyUuid);
            }
        }
        return party;
    }

    public static FullParty getPartyByMember(UUID memberUuid) {
        UUID partyUuid = playerPartyMap.get(memberUuid);
        if (partyUuid != null) {
            return getParty(partyUuid);
        }

        PartyDatabase db = new PartyDatabase(null);
        FullParty party = db.getPartyByMember(memberUuid);
        if (party != null) {
            cachedParties.put(party.getUuid(), party);
            playerPartyMap.put(memberUuid, party.getUuid());
        }
        return party;
    }

    public static boolean isPlayerInParty(UUID playerUuid) {
        return getPartyByMember(playerUuid) != null;
    }

    public static void handleInvite(PartyInviteEvent event) {
        PendingParty invite = (PendingParty) event.getParty();
        PartyDatabase db = new PartyDatabase(null);

        // Check if inviter is already in a party
        FullParty inviterParty = getPartyByMember(invite.getLeader());
        if (inviterParty == null) {
            // Create a new party with just the leader
            inviterParty = FullParty.create(invite.getLeader(), invite.getLeader());
            inviterParty.getMembers().removeIf(m -> !m.getUuid().equals(invite.getLeader()));
            persistParty(inviterParty);
        }

        // Check if invitee is already in a party
        if (isPlayerInParty(invite.getInvitee())) {
            sendErrorToPlayer(invite.getLeader(), "That player is already in a party!");
            return;
        }

        // Check for existing pending invite
        if (db.hasPendingInvite(invite.getInvitee(), invite.getLeader())) {
            sendErrorToPlayer(invite.getLeader(), "You have already invited that player!");
            return;
        }

        db.addPendingInvite(invite);
        sendEvent(new PartyInviteResponseEvent(invite));
    }

    public static void handleAcceptInvite(PartyAcceptInviteEvent event) {
        UUID accepter = event.getAccepter();
        UUID inviter = event.getInviter();

        PartyDatabase db = new PartyDatabase(null);

        // Find the pending invite
        List<PendingParty> invites = db.getPendingInvitesFor(accepter);
        PendingParty matchingInvite = invites.stream()
            .filter(i -> i.getLeader().equals(inviter))
            .findFirst().orElse(null);

        if (matchingInvite == null) {
            sendErrorToPlayer(accepter, "You don't have a party invite from that player!");
            return;
        }

        // Remove the invite
        db.removePendingInvite(matchingInvite.getResultPartyUUID());

        // Check if accepter is already in a party
        if (isPlayerInParty(accepter)) {
            sendErrorToPlayer(accepter, "You are already in a party!");
            return;
        }

        // Get or create the party
        FullParty party = getPartyByMember(inviter);
        if (party == null) {
            party = FullParty.create(inviter, accepter);
        } else {
            // Add new member to existing party
            party.getMembers().add(new FullParty.Member(accepter, FullParty.Role.MEMBER, true));
        }

        persistParty(party);
        playerPartyMap.put(accepter, party.getUuid());

        sendEvent(new PartyMemberJoinResponseEvent(party, inviter, accepter));
    }

    public static void handleLeaveRequest(PartyLeaveRequestEvent event) {
        UUID leaver = event.getLeaver();
        FullParty party = getPartyByMember(leaver);

        if (party == null) {
            sendErrorToPlayer(leaver, "You are not in a party!");
            return;
        }

        FullParty.Member member = party.getFromUuid(leaver);
        boolean isLeader = member.getRole() == FullParty.Role.LEADER;

        if (isLeader) {
            // If leader leaves, disband the party
            disbandParty(party, "Party leader left");
        } else {
            // Remove member from party
            removeMemberFromParty(party, leaver);
            sendEvent(new PartyMemberLeaveResponseEvent(party, leaver));
        }
    }

    public static void handleDisbandRequest(PartyDisbandRequestEvent event) {
        UUID leader = event.getLeader();
        FullParty party = getPartyByMember(leader);

        if (party == null) {
            sendErrorToPlayer(leader, "You are not in a party!");
            return;
        }

        FullParty.Member member = party.getFromUuid(leader);
        if (member.getRole() != FullParty.Role.LEADER) {
            sendErrorToPlayer(leader, "Only the party leader can disband the party!");
            return;
        }

        disbandParty(party, "Party disbanded by leader");
    }

    public static void handleKickRequest(PartyKickRequestEvent event) {
        UUID kicker = event.getKicker();
        UUID target = event.getTarget();

        FullParty party = getPartyByMember(kicker);
        if (party == null) {
            sendErrorToPlayer(kicker, "You are not in a party!");
            return;
        }

        // Check if kicker has permission
        FullParty.Member kickerMember = party.getFromUuid(kicker);
        if (kickerMember.getRole() == FullParty.Role.MEMBER) {
            sendErrorToPlayer(kicker, "Only the party leader or moderators can kick members!");
            return;
        }

        // Check if target is in the party
        if (!isPlayerInParty(target) || !party.getUuid().equals(getPartyByMember(target).getUuid())) {
            sendErrorToPlayer(kicker, "That player is not in your party!");
            return;
        }

        // Can't kick the leader
        FullParty.Member targetMember = party.getFromUuid(target);
        if (targetMember.getRole() == FullParty.Role.LEADER) {
            sendErrorToPlayer(kicker, "You cannot kick the party leader!");
            return;
        }

        // Can't kick someone with same or higher rank
        if (targetMember.getRole().ordinal() <= kickerMember.getRole().ordinal()) {
            sendErrorToPlayer(kicker, "You cannot kick someone with the same or higher rank!");
            return;
        }

        removeMemberFromParty(party, target);
        playerPartyMap.remove(target);

        sendEvent(new PartyMemberKickResponseEvent(party, kicker, target));
    }

    public static void handleTransferRequest(PartyTransferRequestEvent event) {
        UUID leader = event.getLeader();
        UUID target = event.getTarget();

        FullParty party = getPartyByMember(leader);
        if (party == null) {
            sendErrorToPlayer(leader, "You are not in a party!");
            return;
        }

        FullParty.Member leaderMember = party.getFromUuid(leader);
        if (leaderMember.getRole() != FullParty.Role.LEADER) {
            sendErrorToPlayer(leader, "Only the party leader can transfer leadership!");
            return;
        }

        if (!isPlayerInParty(target) || !party.getUuid().equals(getPartyByMember(target).getUuid())) {
            sendErrorToPlayer(leader, "That player is not in your party!");
            return;
        }

        FullParty.Member targetMember = party.getFromUuid(target);
        leaderMember.setRole(FullParty.Role.MODERATOR);
        targetMember.setRole(FullParty.Role.LEADER);

        persistParty(party);

        sendEvent(new PartyLeaderTransferResponseEvent(party, leader, target));
    }

    public static void handlePromoteRequest(PartyPromoteRequestEvent event) {
        UUID promoter = event.getPromoter();
        UUID target = event.getTarget();

        FullParty party = getPartyByMember(promoter);
        if (party == null) {
            sendErrorToPlayer(promoter, "You are not in a party!");
            return;
        }

        FullParty.Member promoterMember = party.getFromUuid(promoter);
        if (promoterMember.getRole() != FullParty.Role.LEADER) {
            sendErrorToPlayer(promoter, "Only the party leader can promote members!");
            return;
        }

        if (!isPlayerInParty(target) || !party.getUuid().equals(getPartyByMember(target).getUuid())) {
            sendErrorToPlayer(promoter, "That player is not in your party!");
            return;
        }

        FullParty.Member targetMember = party.getFromUuid(target);
        FullParty.Role newRole;
        if (targetMember.getRole() == FullParty.Role.MEMBER) {
            newRole = FullParty.Role.MODERATOR;
        } else {
            sendErrorToPlayer(promoter, "That player cannot be promoted further!");
            return;
        }

        targetMember.setRole(newRole);
        persistParty(party);

        sendEvent(new PartyPromotionResponseEvent(party, promoter, target, newRole));
    }

    public static void handleDemoteRequest(PartyDemoteRequestEvent event) {
        UUID demoter = event.getDemoter();
        UUID target = event.getTarget();

        FullParty party = getPartyByMember(demoter);
        if (party == null) {
            sendErrorToPlayer(demoter, "You are not in a party!");
            return;
        }

        FullParty.Member demoterMember = party.getFromUuid(demoter);
        if (demoterMember.getRole() != FullParty.Role.LEADER) {
            sendErrorToPlayer(demoter, "Only the party leader can demote members!");
            return;
        }

        if (!isPlayerInParty(target) || !party.getUuid().equals(getPartyByMember(target).getUuid())) {
            sendErrorToPlayer(demoter, "That player is not in your party!");
            return;
        }

        FullParty.Member targetMember = party.getFromUuid(target);
        FullParty.Role newRole;
        if (targetMember.getRole() == FullParty.Role.MODERATOR) {
            newRole = FullParty.Role.MEMBER;
        } else {
            sendErrorToPlayer(demoter, "That player cannot be demoted further!");
            return;
        }

        targetMember.setRole(newRole);
        persistParty(party);

        sendEvent(new PartyPromotionResponseEvent(party, demoter, target, newRole));
    }

    public static void handleWarpRequest(PartyWarpRequestEvent event) {
        UUID leader = event.getLeader();

        FullParty party = getPartyByMember(leader);
        if (party == null) {
            sendErrorToPlayer(leader, "You are not in a party!");
            return;
        }

        FullParty.Member leaderMember = party.getFromUuid(leader);
        if (leaderMember.getRole() != FullParty.Role.LEADER && leaderMember.getRole() != FullParty.Role.MODERATOR) {
            sendErrorToPlayer(leader, "Only the party leader or moderators can warp the party!");
            return;
        }

        // Get online/offline status from all servers via response event
        sendEvent(new PartyWarpOverviewResponseEvent(party, leader, 
            party.getParticipants(), // Online list will be filled by proxy
            List.of()  // Offline list will be filled by proxy
        ));

        sendEvent(new PartyWarpResponseEvent(party, leader));
    }

    public static void handleChatMessage(PartyChatMessageEvent event) {
        UUID sender = event.getSender();
        String message = event.getMessage();

        FullParty party = getPartyByMember(sender);
        if (party == null) {
            sendErrorToPlayer(sender, "You are not in a party!");
            return;
        }

        sendEvent(new PartyChatMessageResponseEvent(party, sender, message));
    }

    public static void handleListRequest(PartyListRequestEvent event) {
        UUID player = event.getPlayer();

        FullParty party = getPartyByMember(player);
        if (party == null) {
            sendErrorToPlayer(player, "You are not in a party!");
            return;
        }

        // Member info will be filled by the proxy with player names
        List<PartyListResponseEvent.MemberInfo> memberInfos = new ArrayList<>();
        for (FullParty.Member member : party.getMembers()) {
            memberInfos.add(new PartyListResponseEvent.MemberInfo(
                member.getUuid(),
                "Unknown", // Name will be filled by proxy
                member.getRole(),
                true // Online status will be filled by proxy
            ));
        }

        sendEvent(new PartyListResponseEvent(party, player, memberInfos));
    }

    public static void handlePlayerDisconnect(PartyPlayerDisconnectEvent event) {
        UUID player = event.getPlayer();
        FullParty party = getPartyByMember(player);

        if (party != null) {
            disconnectedPlayers.put(player, System.currentTimeMillis());
            FullParty.Member member = party.getFromUuid(player);
            member.setJoined(false);
            persistParty(party);

            sendEvent(new PartyMemberDisconnectedResponseEvent(party, player, 300)); // 5 minute timeout
        }
    }

    public static void handlePlayerRejoin(PartyPlayerRejoinEvent event) {
        UUID player = event.getPlayer();
        disconnectedPlayers.remove(player);

        FullParty party = getPartyByMember(player);
        if (party != null) {
            FullParty.Member member = party.getMembers().stream()
                .filter(m -> m.getUuid().equals(player))
                .findFirst().orElse(null);
            if (member != null) {
                member.setJoined(true);
                persistParty(party);
            }

            sendEvent(new PartyMemberRejoinedResponseEvent(party, player));
        }
    }

    public static void handlePlayerSwitchedServer(PartyPlayerSwitchedServerEvent event) {
        UUID player = event.getPlayer();
        FullParty party = getPartyByMember(player);

        if (party != null) {
            sendEvent(new PartyPlayerSwitchedServerResponseEvent(party, player));
        }
    }

    private static void disbandParty(FullParty party, String reason) {
        // Remove all members from party map
        for (FullParty.Member member : party.getMembers()) {
            playerPartyMap.remove(member.getUuid());
        }

        // Delete from cache and database
        cachedParties.remove(party.getUuid());
        PartyDatabase db = new PartyDatabase(null);
        db.deleteParty(party.getUuid());

        // Send disband event
        sendEvent(new PartyDisbandResponseEvent(party, party.getLeader().getUuid(), reason));
    }

    private static void removeMemberFromParty(FullParty party, UUID memberUuid) {
        party.getMembers().removeIf(m -> m.getUuid().equals(memberUuid));
        playerPartyMap.remove(memberUuid);

        if (party.getMembers().size() <= 1) {
            // If only one member left, disband the party
            disbandParty(party, "Not enough members");
        } else {
            persistParty(party);
        }
    }

    private static void persistParty(FullParty party) {
        PartyDatabase db = new PartyDatabase(null);
        db.saveParty(party);
        cachedParties.put(party.getUuid(), party);
    }

    private static void sendEvent(PartyEvent event) {
        JSONObject message = new JSONObject();
        message.put("eventType", event.getClass().getSimpleName());
        message.put("eventData", event.getSerializer().serialize(event));
        message.put("participants", event.getParticipants());

        ServiceToServerManager.sendToAllServers(FromServiceChannels.PROPAGATE_PARTY_EVENT, message);
    }

    private static void sendErrorToPlayer(UUID playerUUID, String message) {
        sendMessageToPlayer(playerUUID, "§9§m-----------------------------------------------------\n§c" + message + "\n§9§m-----------------------------------------------------");
    }

    private static void sendMessageToPlayer(UUID playerUUID, String message) {
        JSONObject messageData = new JSONObject();
        messageData.put("playerUUID", playerUUID.toString());
        messageData.put("message", message);

        ServiceToServerManager.sendToAllServers(FromServiceChannels.SEND_MESSAGE, messageData);
    }
}
