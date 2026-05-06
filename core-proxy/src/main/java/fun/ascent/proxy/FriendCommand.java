package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

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
            default -> {
                // Default to add friend if only name is provided
                FriendManager.addFriend(player, sub, proxy);
            }
        }
    }

    private void showHelp(Player player) {
        player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
        player.sendMessage(Component.text("§6Friend Commands:"));
        player.sendMessage(Component.text("§e/f add <player> §7- Add a friend"));
        player.sendMessage(Component.text("§e/f accept <player> §7- Accept a friend request"));
        player.sendMessage(Component.text("§e/f deny <player> §7- Deny a friend request"));
        player.sendMessage(Component.text("§e/f remove <player> §7- Remove a friend"));
        player.sendMessage(Component.text("§9§m-----------------------------------------------------"));
    }
}
