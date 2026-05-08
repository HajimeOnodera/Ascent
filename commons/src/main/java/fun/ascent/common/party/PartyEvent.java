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
            case "PartyAcceptInviteEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyLeaveRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyDisbandRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyTransferRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyKickRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyPromoteRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyDemoteRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyPlayerSwitchedServerEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyWarpRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyListRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyHijackRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyChatMessageEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, String.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), "");
            }
            case "PartyPlayerSwitchedServerResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
            }
            case "PartyChatMessageResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, String.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), "");
            }
            case "PartyInviteResponseEvent" -> {
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
            case "PartyDisbandResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, String.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), "");
            }
            case "PartyLeaderTransferResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyMemberJoinResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(FullParty.create(UUID.randomUUID(), UUID.randomUUID()), UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyMemberKickResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyInviteExpiredResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(FullParty.create(UUID.randomUUID(), UUID.randomUUID()), UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyMemberLeaveResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
            }
            case "PartyPromotionResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class, FullParty.Role.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), UUID.randomUUID(), FullParty.Role.MEMBER);
            }
            case "PartyWarpResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
            }
            case "PartyInviteEvent" -> {
                var pendingParty = PendingParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(PendingParty.class);
                return (PartyEvent) constructor.newInstance(pendingParty);
            }
            case "PartyPlayerDisconnectEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyPlayerRejoinEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyMemberDisconnectedResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, long.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), 300L);
            }
            case "PartyMemberRejoinedResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
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
