package fun.ascent.lobby.command;

import fun.ascent.common.user.Rank;
import fun.ascent.common.user.UserManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import static fun.ascent.common.StringUtility.text;

public class FlyCommand extends Command {

    public FlyCommand() {
        super("fly");

        setCondition((sender, _) -> {
            if (!(sender instanceof Player player)) return false;
            Rank rank = UserManager.getUser(player.getUuid()).getRank();
            return rank.isEqualOrHigherThan(Rank.VIP);
        });

        setDefaultExecutor((sender, _) -> {
            Player player = (Player) sender;
            boolean isFlying = player.isAllowFlying();
            
            player.setAllowFlying(!isFlying);
            player.setFlying(!isFlying);
            
            if (!isFlying) {
                player.sendMessage(text("<green>Flight has been enabled!"));
            } else {
                player.sendMessage(text("<red>Flight has been disabled!"));
            }
        });
    }
}
