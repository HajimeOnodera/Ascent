package fun.ascent.common.command;

import fun.ascent.common.restart.RestartManager;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.UserManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import static fun.ascent.common.StringUtility.text;

public class RestartCommand extends Command {

    public RestartCommand() {
        super("restart", "reboot");

        setCondition((sender, _) -> {
            if (!(sender instanceof Player player)) return false;
            return UserManager.getUser(player.getUuid()).getRank().isEqualOrHigherThan(Rank.STAFF);
        });

        var typeArg = ArgumentType.Enum("type", RestartManager.RestartType.class);
        var secondsArg = ArgumentType.Integer("seconds");

        addSyntax((sender, context) -> {
            RestartManager.RestartType type = context.get(typeArg);
            int seconds = context.get(secondsArg);
            
            RestartManager.startRestart(type, seconds);
            sender.sendMessage(text("<green>Restart initiated: " + type.getDisplayName() + " in " + seconds + "s"));
        }, typeArg, secondsArg);

        setDefaultExecutor((sender, _) -> sender.sendMessage(text("<red>Usage: /restart <SCHEDULED_REBOOT|GAME_UPDATE> <seconds>")));
    }
}
