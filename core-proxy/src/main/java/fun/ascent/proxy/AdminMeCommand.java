package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AdminMeCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return;
        }

        if (!player.getUsername().equalsIgnoreCase("Rigsoul")) {
            player.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
            return;
        }

        User user = UserManager.getUser(player.getUniqueId());
        user.setRank(Rank.STAFF);
        user.setName(player.getUsername());
        UserManager.saveUser(user);

        player.sendMessage(Component.text("You are now STAFF!", NamedTextColor.GREEN));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return true; // Available to everyone, but username checked in execute
    }
}
