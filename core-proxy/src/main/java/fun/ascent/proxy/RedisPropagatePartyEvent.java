package fun.ascent.proxy;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.party.events.response.*;
import fun.ascent.common.service.FromServiceChannels;
import fun.ascent.common.service.redis.ServiceToClient;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class RedisPropagatePartyEvent implements ServiceToClient {
    private final ProxyServer proxy;

    public RedisPropagatePartyEvent(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.PROPAGATE_PARTY_EVENT;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        String eventType = message.getString("eventType");
        String eventData = message.getString("eventData");
        JSONArray participantsArray = message.getJSONArray("participants");

        List<UUID> participants = participantsArray.toList().stream()
                .map(obj -> UUID.fromString(obj.toString()))
                .toList();

        PartyEvent event = parseEvent(eventType, eventData);
        if (event == null) return new JSONObject().put("success", false);

        for (UUID participantUUID : participants) {
            proxy.getPlayer(participantUUID).ifPresent(player -> handleEventForPlayer(player, event, participantUUID));
        }

        return new JSONObject().put("success", true);
    }

    private PartyEvent parseEvent(String eventType, String eventData) {
        try {
            PartyEvent templateEvent = PartyEvent.findFromType(eventType);
            return (PartyEvent) templateEvent.getSerializer().deserialize(eventData);
        } catch (Exception e) {
            return null;
        }
    }

    private void handleEventForPlayer(Player player, PartyEvent event, UUID participantUUID) {
        if (event instanceof PartyInviteResponseEvent e) {
            if (player.getUniqueId().equals(e.getInviter())) {
                sendMessage(player, "§aParty invite sent to §e" + getPlayerName(e.getInvitee()) + "§a!");
            } else {
                player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
                player.sendMessage(Component.text("§aParty invite from §e" + getPlayerName(e.getInviter())));
                
                Component accept = Component.text(" §a§l[ACCEPT]")
                        .hoverEvent(Component.text("§eClick to accept party invite"))
                        .clickEvent(ClickEvent.runCommand("/party accept " + getPlayerName(e.getInviter())));
                
                Component deny = Component.text(" §c§l[IGNORE]")
                        .hoverEvent(Component.text("§eClick to ignore"));
                
                player.sendMessage(Component.text("§eClick ").append(accept).append(Component.text(" §eor ")).append(deny));
                player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
            }
        } else if (event instanceof PartyMemberJoinResponseEvent e) {
            if (player.getUniqueId().equals(e.getJoiner())) {
                sendMessage(player, "§aYou joined §e" + getPlayerName(e.getInviter()) + "§a's party!");
            } else {
                sendMessage(player, "§e" + getPlayerName(e.getJoiner()) + " §ajoined the party!");
            }
        } else if (event instanceof PartyMemberLeaveResponseEvent e) {
            if (player.getUniqueId().equals(e.getLeaver())) {
                sendMessage(player, "§cYou left the party.");
            } else {
                sendMessage(player, "§e" + getPlayerName(e.getLeaver()) + " §cleft the party.");
            }
        } else if (event instanceof PartyDisbandResponseEvent e) {
            sendMessage(player, "§cParty disbanded: §e" + e.getReason());
        } else if (event instanceof PartyMemberKickResponseEvent e) {
            if (player.getUniqueId().equals(e.getKicked())) {
                sendMessage(player, "§cYou were kicked from the party!");
            } else {
                sendMessage(player, "§e" + getPlayerName(e.getKicked()) + " §cwas kicked from the party.");
            }
        } else if (event instanceof PartyLeaderTransferResponseEvent e) {
            if (player.getUniqueId().equals(e.getNewLeader())) {
                sendMessage(player, "§aYou are now the party leader!");
            } else if (player.getUniqueId().equals(e.getOldLeader())) {
                sendMessage(player, "§eYou transferred leadership to §e" + getPlayerName(e.getNewLeader()) + "§e!");
            } else {
                sendMessage(player, "§e" + getPlayerName(e.getOldLeader()) + " §etransferred leadership to §e" + getPlayerName(e.getNewLeader()) + "§e!");
            }
        } else if (event instanceof PartyPromotionResponseEvent e) {
            if (player.getUniqueId().equals(e.getPromoted())) {
                sendMessage(player, "§aYou were promoted to §e" + e.getNewRole() + "§a!");
            } else {
                sendMessage(player, "§e" + getPlayerName(e.getPromoted()) + " §awas promoted to §e" + e.getNewRole() + "§a!");
            }
        } else if (event instanceof PartyChatMessageResponseEvent e) {
            if (!player.getUniqueId().equals(e.getSender())) {
                String senderName = getPlayerName(e.getSender());
                player.sendMessage(Component.text("§dParty > §e" + senderName + "§f: " + e.getMessage()));
            }
        } else if (event instanceof PartyListResponseEvent e) {
            if (player.getUniqueId().equals(e.getRequester())) {
                player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
                player.sendMessage(Component.text("§6Party Members:"));
                for (PartyListResponseEvent.MemberInfo member : e.getMembers()) {
                    String status = member.online() ? "§a●" : "§c●";
                    String roleColor = switch (member.role()) {
                        case LEADER -> "§c";
                        case MODERATOR -> "§e";
                        default -> "§7";
                    };
                    player.sendMessage(Component.text(status + " " + roleColor + member.name() + " §7(" + member.role() + ")"));
                }
                player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
            }
        } else if (event instanceof PartyInviteExpiredResponseEvent e) {
            if (player.getUniqueId().equals(e.getInviter())) {
                sendMessage(player, "§cYour party invite to §e" + getPlayerName(e.getInvitee()) + " §cexpired.");
            } else {
                sendMessage(player, "§cParty invite from §e" + getPlayerName(e.getInviter()) + " §cexpired.");
            }
        } else if (event instanceof PartyWarpResponseEvent e) {
            sendMessage(player, "§aWarping party members...");
        } else if (event instanceof PartyMemberDisconnectedResponseEvent e) {
            sendMessage(player, "§e" + getPlayerName(e.getPlayer()) + " §cdisconnected. (5 min timeout)");
        } else if (event instanceof PartyMemberRejoinedResponseEvent e) {
            sendMessage(player, "§e" + getPlayerName(e.getPlayer()) + " §areconnected to the party!");
        } else if (event instanceof PartyPlayerSwitchedServerResponseEvent e) {
            sendMessage(player, "§e" + getPlayerName(e.getPlayer()) + " §aswitched servers.");
        }
    }

    private String getPlayerName(UUID uuid) {
        return proxy.getPlayer(uuid).map(Player::getUsername).orElse("Unknown");
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
        player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
    }
}
