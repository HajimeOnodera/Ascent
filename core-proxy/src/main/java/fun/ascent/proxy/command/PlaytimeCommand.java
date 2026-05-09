package fun.ascent.proxy.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fun.ascent.proxy.manager.PlaytimeTracker;

import static fun.ascent.common.StringUtility.text;

public class PlaytimeCommand implements SimpleCommand {

    private static final long REQUIRED_PLAYTIME_MS = 100L * 60 * 60 * 1000; // 100 hours

    public PlaytimeCommand() {}

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            return;
        }

        long totalMs = PlaytimeTracker.getTotalPlaytimeMs(player.getUniqueId());

        if (totalMs < REQUIRED_PLAYTIME_MS) {
            player.sendMessage(text("<red>You don't have enough playtime to use this command, try again later!"));
            return;
        }

        long totalMinutes = totalMs / (1000 * 60);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        player.sendMessage(text("<green>You have <aqua>" + hours + " hours</aqua> and <aqua>"
                + String.format("%02d", minutes) + " minutes</aqua> playtime!"));
    }
}
