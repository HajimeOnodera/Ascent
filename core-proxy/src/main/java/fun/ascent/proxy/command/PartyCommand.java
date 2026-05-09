package fun.ascent.proxy.command;

import fun.ascent.proxy.manager.*;
import fun.ascent.proxy.config.*;
import fun.ascent.proxy.service.*;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.Arrays;

import static fun.ascent.common.StringUtility.text;

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
                    player.sendMessage(text(text("<red>Usage: /party invite <player>")));
                } else {
                    PartyManager.invitePlayer(player, args[1], proxy);
                }
            }
            case "accept", "a", "join", "j" -> {
                if (args.length < 2) {
                    player.sendMessage(text(text("<red>Usage: /party accept <player>")));
                } else {
                    PartyManager.acceptInvite(player, args[1], proxy);
                }
            }
            case "leave", "l" -> PartyManager.leaveParty(player);
            case "disband" -> PartyManager.disbandParty(player);
            case "kick", "k" -> {
                if (args.length < 2) {
                    player.sendMessage(text(text("<red>Usage: /party kick <player>")));
                } else {
                    PartyManager.kickPlayer(player, args[1], proxy);
                }
            }
            case "transfer", "t" -> {
                if (args.length < 2) {
                    player.sendMessage(text(text("<red>Usage: /party transfer <player>")));
                } else {
                    PartyManager.transferLeadership(player, args[1], proxy);
                }
            }
            case "promote", "p" -> {
                if (args.length < 2) {
                    player.sendMessage(text(text("<red>Usage: /party promote <player>")));
                } else {
                    PartyManager.promotePlayer(player, args[1], proxy);
                }
            }
            case "demote", "d" -> {
                if (args.length < 2) {
                    player.sendMessage(text(text("<red>Usage: /party demote <player>")));
                } else {
                    PartyManager.demotePlayer(player, args[1], proxy);
                }
            }
            case "warp", "w" -> PartyManager.warpParty(player);
            case "list" -> PartyManager.listParty(player);
            case "chat", "c" -> {
                if (args.length < 2) {
                    player.sendMessage(text(text("<red>Usage: /party chat <message>")));
                } else {
                    String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
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
        player.sendMessage(text(text("<blue><strikethrough>-----------------------------------------------------")));
        player.sendMessage(text(text("<gold>Party Commands:")));
        player.sendMessage(text(text("<yellow>/party invite <player> <gray>- Invite a player to your party")));
        player.sendMessage(text(text("<yellow>/party accept <player> <gray>- Accept a party invite")));
        player.sendMessage(text(text("<yellow>/party leave <gray>- Leave your current party")));
        player.sendMessage(text(text("<yellow>/party disband <gray>- Disband your party (leader only)")));
        player.sendMessage(text(text("<yellow>/party kick <player> <gray>- Kick a player from your party")));
        player.sendMessage(text(text("<yellow>/party transfer <player> <gray>- Transfer party leadership")));
        player.sendMessage(text(text("<yellow>/party promote <player> <gray>- Promote a party member to moderator")));
        player.sendMessage(text(text("<yellow>/party demote <player> <gray>- Demote a party moderator to member")));
        player.sendMessage(text(text("<yellow>/party warp <gray>- Warp all party members to your server")));
        player.sendMessage(text(text("<yellow>/party list <gray>- List all party members")));
        player.sendMessage(text(text("<yellow>/party chat <message> <gray>- Send a message to party chat")));
        player.sendMessage(text(text("<yellow>/p <message> <gray>- Quick party chat (alias)")));
        player.sendMessage(text(text("<blue><strikethrough>-----------------------------------------------------")));
    }
}

