package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RankCommand implements SimpleCommand {
    private final ProxyServer proxy;

    public RankCommand(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!invocation.source().hasPermission("ascent.admin.rank")) {
            invocation.source().sendMessage(Component.text("No permission.").color(NamedTextColor.RED));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 2) {
            invocation.source().sendMessage(Component.text("Usage: /rank <player> <rank>").color(NamedTextColor.RED));
            return;
        }

        String targetName = args[0];
        String rankName = args[1].toUpperCase();

        proxy.getPlayer(targetName).ifPresentOrElse(target -> {
            try {
                Rank rank = Rank.valueOf(rankName);
                User user = UserManager.getUser(target.getUniqueId());
                user.setRank(rank);
                UserManager.saveUser(user);

                invocation.source().sendMessage(Component.text("Set rank of ")
                        .append(Component.text(targetName).color(NamedTextColor.YELLOW))
                        .append(Component.text(" to "))
                        .append(rank.getFormattedPrefix())
                        .color(NamedTextColor.GREEN));
                
                target.sendMessage(Component.text("Your rank has been updated to ")
                        .append(rank.getFormattedPrefix())
                        .color(NamedTextColor.GREEN));
            } catch (IllegalArgumentException e) {
                invocation.source().sendMessage(Component.text("Invalid rank. Available: " + 
                        Arrays.stream(Rank.values()).map(Enum::name).collect(Collectors.joining(", ")))
                        .color(NamedTextColor.RED));
            }
        }, () -> invocation.source().sendMessage(Component.text("Player not found.").color(NamedTextColor.RED)));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 1) {
            return proxy.getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.stream(Rank.values()).map(Enum::name).collect(Collectors.toList());
        }
        return List.of();
    }
}
