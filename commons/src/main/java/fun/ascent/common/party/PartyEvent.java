package fun.ascent.common.party;

import lombok.Getter;
import lombok.NonNull;
import fun.ascent.common.protocol.Serializer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class PartyEvent {
    private final Party party;

    public PartyEvent(Party party) {
        this.party = party;
    }

    public abstract Serializer getSerializer();

    public List<UUID> getParticipants() {
        return party != null ? party.getParticipants() : List.of();
    }

    public static @NonNull PartyEvent findFromType(String className) {
        String[] packageNames = {
                "fun.ascent.common.party.events",
                "fun.ascent.common.party.events.response"
        };

        for (String packageName : packageNames) {
            try {
                Class<?> clazz = Class.forName(packageName + "." + className);
                return createDummyInstance(clazz);
            } catch (Exception e) {
                // Try next package
            }
        }

        throw new RuntimeException("Failed to find party event class: " + className + " in " + Arrays.toString(packageNames));
    }

    private static PartyEvent createDummyInstance(Class<?> clazz) throws Exception {
        String className = clazz.getSimpleName();

        switch (className) {
            case "PartyAcceptInviteEvent", "PartyTransferRequestEvent", "PartyKickRequestEvent",
                 "PartyPromoteRequestEvent", "PartyDemoteRequestEvent", "PartyHijackRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyLeaveRequestEvent", "PartyDisbandRequestEvent", "PartyPlayerSwitchedServerEvent",
                 "PartyWarpRequestEvent", "PartyListRequestEvent", "PartyPlayerDisconnectEvent",
                 "PartyPlayerRejoinEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyChatMessageEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, String.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), "");
            }
            case "PartyPlayerSwitchedServerResponseEvent", "PartyMemberLeaveResponseEvent", "PartyWarpResponseEvent",
                 "PartyMemberRejoinedResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
            }
            case "PartyChatMessageResponseEvent", "PartyDisbandResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, String.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), "");
            }
            case "PartyInviteResponseEvent", "PartyInviteEvent" -> {
                var pendingParty = PendingParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(PendingParty.class);
                return (PartyEvent) constructor.newInstance(pendingParty);
            }
            case "PartyWarpOverviewResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, List.class, List.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), List.of(), List.of());
            }
            case "PartyStartedResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(FullParty.class);
                return (PartyEvent) constructor.newInstance(FullParty.create(UUID.randomUUID(), UUID.randomUUID()));
            }
            case "PartyLeaderTransferResponseEvent", "PartyMemberKickResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyMemberJoinResponseEvent", "PartyInviteExpiredResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(FullParty.create(UUID.randomUUID(), UUID.randomUUID()), UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyPromotionResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class, FullParty.Role.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), UUID.randomUUID(), FullParty.Role.MEMBER);
            }
            case "PartyMemberDisconnectedResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, long.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), 300L);
            }
            case "PartyMemberDisconnectTimeoutResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, boolean.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), false);
            }
            default -> throw new IllegalArgumentException("Unknown party event class: " + className);
        }
    }
}
