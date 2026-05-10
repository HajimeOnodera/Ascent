package fun.ascent.proxy.command;

import fun.ascent.proxy.manager.*;
import fun.ascent.proxy.config.*;
import fun.ascent.proxy.service.*;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import static fun.ascent.common.StringUtility.text;

public class FriendCommand implements SimpleCommand {
    private final ProxyServer proxy;

    public FriendCommand(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            return;
        }

        if (FriendManager.ensureAvailable(player)) {
            return;
        }

        String[] args = invocation.arguments();
        if (args.length == 0) {
            showHelp(player);
            return;
        }

        String sub = args[0].toLowerCase();
        String target = args.length > 1 ? args[1] : null;

        switch (sub) {
            case "add" -> {
                if (target == null) showHelp(player);
                else FriendManager.addFriend(player, target, proxy);
            }
            case "accept" -> {
                if (target == null) showHelp(player);
                else FriendManager.acceptRequest(player, target, proxy);
            }
            case "deny" -> {
                if (target == null) showHelp(player);
                else FriendManager.denyRequest(player, target, proxy);
            }
            case "remove" -> {
                if (target == null) showHelp(player);
                else FriendManager.removeFriend(player, target, proxy);
            }
            default -> FriendManager.addFriend(player, sub, proxy);
        }
    }

    private void showHelp(Player player) {
        player.sendMessage(text(text("<blue><strikethrough>-----------------------------------------------------")));
        player.sendMessage(text(text("<gold>Friend Commands:")));
        player.sendMessage(text(text("<yellow>/f add <player> <gray>- Add a friend")));
        player.sendMessage(text(text("<yellow>/f accept <player> <gray>- Accept a friend request")));
        player.sendMessage(text(text("<yellow>/f deny <player> <gray>- Deny a friend request")));
        player.sendMessage(text(text("<yellow>/f remove <player> <gray>- Remove a friend")));
        player.sendMessage(text(text("<blue><strikethrough>-----------------------------------------------------")));
    }
}

