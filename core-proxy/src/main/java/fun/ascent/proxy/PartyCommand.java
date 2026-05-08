package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

public class PartyCommand implements SimpleCommand {
    private final ProxyServer proxy;

    public PartyCommand(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            return;
        }

        String[] args = invocation.arguments();
        if (args.length == 0) {
            showHelp(player);
            return;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "invite", "i" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /party invite <player>"));
                } else {
                    PartyManager.invitePlayer(player, args[1], proxy);
                }
            }
            case "accept", "a", "join", "j" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /party accept <player>"));
                } else {
                    PartyManager.acceptInvite(player, args[1], proxy);
                }
            }
            case "leave", "l" -> PartyManager.leaveParty(player);
            case "disband" -> PartyManager.disbandParty(player);
            case "kick", "k" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /party kick <player>"));
                } else {
                    PartyManager.kickPlayer(player, args[1], proxy);
                }
            }
            case "transfer", "t" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /party transfer <player>"));
                } else {
                    PartyManager.transferLeadership(player, args[1], proxy);
                }
            }
            case "promote", "p" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /party promote <player>"));
                } else {
                    PartyManager.promotePlayer(player, args[1], proxy);
                }
            }
            case "demote", "d" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /party demote <player>"));
                } else {
                    PartyManager.demotePlayer(player, args[1], proxy);
                }
            }
            case "warp", "w" -> PartyManager.warpParty(player);
            case "list" -> PartyManager.listParty(player);
            case "chat", "c" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /party chat <message>"));
                } else {
                    String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                    PartyManager.sendChatMessage(player, message);
                }
            }
            case "help", "h" -> showHelp(player);
            default -> {
                if (sub.startsWith("/")) {
                    String message = String.join(" ", args);
                    PartyManager.sendChatMessage(player, message);
                } else {
                    PartyManager.invitePlayer(player, sub, proxy);
                }
            }
        }
    }

    private void showHelp(Player player) {
        player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
        player.sendMessage(Component.text("§6Party Commands:"));
        player.sendMessage(Component.text("§e/party invite <player> §7- Invite a player to your party"));
        player.sendMessage(Component.text("§e/party accept <player> §7- Accept a party invite"));
        player.sendMessage(Component.text("§e/party leave §7- Leave your current party"));
        player.sendMessage(Component.text("§e/party disband §7- Disband your party (leader only)"));
        player.sendMessage(Component.text("§e/party kick <player> §7- Kick a player from your party"));
        player.sendMessage(Component.text("§e/party transfer <player> §7- Transfer party leadership"));
        player.sendMessage(Component.text("§e/party promote <player> §7- Promote a party member to moderator"));
        player.sendMessage(Component.text("§e/party demote <player> §7- Demote a party moderator to member"));
        player.sendMessage(Component.text("§e/party warp §7- Warp all party members to your server"));
        player.sendMessage(Component.text("§e/party list §7- List all party members"));
        player.sendMessage(Component.text("§e/party chat <message> §7- Send a message to party chat"));
        player.sendMessage(Component.text("§e/p <message> §7- Quick party chat (alias)"));
        player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
    }
}
