package fun.ascent.proxy;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.party.events.response.*;
import fun.ascent.common.service.FromServiceChannels;
import fun.ascent.common.service.redis.ServiceToClient;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

import static fun.ascent.common.StringUtility.text;

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
                sendMessage(player, "<green>Party invite sent to <yellow>" + getPlayerName(e.getInvitee()) + "<green>!");
            } else {
                player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
                player.sendMessage(text("<green>Party invite from <yellow>" + getPlayerName(e.getInviter())));
                
                Component accept = text(" <green><bold>[ACCEPT]")
                        .hoverEvent(text("<yellow>Click to accept party invite"))
                        .clickEvent(ClickEvent.runCommand("/party accept " + getPlayerName(e.getInviter())));
                
                Component deny = text(" <red><bold>[IGNORE]")
                        .hoverEvent(text("<yellow>Click to ignore"));
                
                player.sendMessage(text("<yellow>Click ").append(accept).append(text(" <yellow>or ")).append(deny));
                player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
            }
        } else if (event instanceof PartyMemberJoinResponseEvent e) {
            if (player.getUniqueId().equals(e.getJoiner())) {
                sendMessage(player, "<green>You joined <yellow>" + getPlayerName(e.getInviter()) + "<green>'s party!");
            } else {
                sendMessage(player, "<yellow>" + getPlayerName(e.getJoiner()) + " <green>joined the party!");
            }
        } else if (event instanceof PartyMemberLeaveResponseEvent e) {
            if (player.getUniqueId().equals(e.getLeaver())) {
                sendMessage(player, "<red>You left the party.");
            } else {
                sendMessage(player, "<yellow>" + getPlayerName(e.getLeaver()) + " <red>left the party.");
            }
        } else if (event instanceof PartyDisbandResponseEvent e) {
            sendMessage(player, "<red>Party disbanded: <yellow>" + e.getReason());
        } else if (event instanceof PartyMemberKickResponseEvent e) {
            if (player.getUniqueId().equals(e.getKicked())) {
                sendMessage(player, "<red>You were kicked from the party!");
            } else {
                sendMessage(player, "<yellow>" + getPlayerName(e.getKicked()) + " <red>was kicked from the party.");
            }
        } else if (event instanceof PartyLeaderTransferResponseEvent e) {
            if (player.getUniqueId().equals(e.getNewLeader())) {
                sendMessage(player, "<green>You are now the party leader!");
            } else if (player.getUniqueId().equals(e.getOldLeader())) {
                sendMessage(player, "<yellow>You transferred leadership to <yellow>" + getPlayerName(e.getNewLeader()) + "<yellow>!");
            } else {
                sendMessage(player, "<yellow>" + getPlayerName(e.getOldLeader()) + " <yellow>transferred leadership to <yellow>" + getPlayerName(e.getNewLeader()) + "<yellow>!");
            }
        } else if (event instanceof PartyPromotionResponseEvent e) {
            if (player.getUniqueId().equals(e.getPromoted())) {
                sendMessage(player, "<green>You were promoted to <yellow>" + e.getNewRole() + "<green>!");
            } else {
                sendMessage(player, "<yellow>" + getPlayerName(e.getPromoted()) + " <green>was promoted to <yellow>" + e.getNewRole() + "<green>!");
            }
        } else if (event instanceof PartyChatMessageResponseEvent e) {
            if (!player.getUniqueId().equals(e.getSender())) {
                String senderName = getPlayerName(e.getSender());
                player.sendMessage(text("<light_purple>Party > <yellow>" + senderName + "<white>: " + e.getMessage()));
            }
        } else if (event instanceof PartyListResponseEvent e) {
            if (player.getUniqueId().equals(e.getRequester())) {
                player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
                player.sendMessage(text("<gold>Party Members:"));
                for (PartyListResponseEvent.MemberInfo member : e.getMembers()) {
                    String status = member.online() ? "<green>●" : "<red>●";
                    String roleColor = switch (member.role()) {
                        case LEADER -> "<red>";
                        case MODERATOR -> "<yellow>";
                        default -> "<gray>";
                    };
                    player.sendMessage(text(status + " " + roleColor + member.name() + " <gray>(" + member.role() + ")"));
                }
                player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
            }
        } else if (event instanceof PartyInviteExpiredResponseEvent e) {
            if (player.getUniqueId().equals(e.getInviter())) {
                sendMessage(player, "<red>Your party invite to <yellow>" + getPlayerName(e.getInvitee()) + " <red>expired.");
            } else {
                sendMessage(player, "<red>Party invite from <yellow>" + getPlayerName(e.getInviter()) + " <red>expired.");
            }
        } else if (event instanceof PartyWarpResponseEvent e) {
            sendMessage(player, "<green>Warping party members...");
        } else if (event instanceof PartyMemberDisconnectedResponseEvent e) {
            sendMessage(player, "<yellow>" + getPlayerName(e.getPlayer()) + " <red>disconnected. (5 min timeout)");
        } else if (event instanceof PartyMemberRejoinedResponseEvent e) {
            sendMessage(player, "<yellow>" + getPlayerName(e.getPlayer()) + " <green>reconnected to the party!");
        } else if (event instanceof PartyPlayerSwitchedServerResponseEvent e) {
            sendMessage(player, "<yellow>" + getPlayerName(e.getPlayer()) + " <green>switched servers.");
        }
    }

    private String getPlayerName(UUID uuid) {
        return proxy.getPlayer(uuid).map(Player::getUsername).orElse("Unknown");
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
        player.sendMessage(text(message));
        player.sendMessage(text("<blue><strikethrough>-----------------------------------------------------"));
    }
}

