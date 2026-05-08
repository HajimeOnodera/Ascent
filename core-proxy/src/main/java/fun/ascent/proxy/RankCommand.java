package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static fun.ascent.common.StringUtility.escapeMiniMessage;
import static fun.ascent.common.StringUtility.text;

public class RankCommand implements SimpleCommand {
    private final ProxyServer proxy;

    public RankCommand(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            return UserManager.getUser(player.getUniqueId()).getRank().isStaff();
        }
        return false; // Console is blocked
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(text(text("<red>This command can only be used by players.")));
            return;
        }

        User user = UserManager.getUser(player.getUniqueId());
        if (!user.getRank().isStaff()) {
            invocation.source().sendMessage(text(text("<red>No permission.")));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 2) {
            invocation.source().sendMessage(text(text("<red>Usage: /rank <player> <rank>")));
            return;
        }

        String targetName = args[0];
        String rankName = args[1].toUpperCase();

        proxy.getPlayer(targetName).ifPresentOrElse(target -> {
            try {
                Rank rank = Rank.valueOf(rankName);
                User targetUser = UserManager.getUser(target.getUniqueId());
                targetUser.setRank(rank);
                UserManager.saveUser(targetUser);

                invocation.source().sendMessage(text("<green>Set rank of <yellow>" + escapeMiniMessage(targetName) + "<green> to ")
                        .append(rank.getFormattedPrefix()));
                
                target.sendMessage(text(text("<green>Your rank has been updated to ").append(rank.getFormattedPrefix())));
            } catch (IllegalArgumentException e) {
                invocation.source().sendMessage(text("<red>Invalid rank. Available: " +
                        Arrays.stream(Rank.values()).map(Enum::name).collect(Collectors.joining(", "))));
            }
        }, () -> invocation.source().sendMessage(text(text("<red>Player not found."))));
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
