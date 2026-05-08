package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;

import static fun.ascent.common.StringUtility.text;

public class AdminMeCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(text("<red>This command can only be used by players."));
            return;
        }

        if (!player.getUsername().equalsIgnoreCase("Rigsoul")) {
            player.sendMessage(text("<red>You do not have permission to use this command."));
            return;
        }

        User user = UserManager.getUser(player.getUniqueId());
        user.setRank(Rank.STAFF);
        user.setName(player.getUsername());
        UserManager.saveUser(user);

        player.sendMessage(text("<green>You are now STAFF!"));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return true; // Available to everyone, but username checked in execute
    }
}
