package fun.ascent.common.restart;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static fun.ascent.common.StringUtility.text;

public class RestartManager {

    @Getter
    public enum RestartType {
        SCHEDULED_REBOOT("<aqua>Scheduled Reboot"),
        GAME_UPDATE("<green>Game Update");

        private final String displayName;

        RestartType(String displayName) {
            this.displayName = displayName;
        }
    }

    private static Task restartTask;
    private static final AtomicInteger secondsLeft = new AtomicInteger(-1);
    private static RestartType currentType;
    
    @Getter
    private static int exitCode = 0;

    public static void startRestart(RestartType type, int seconds) {
        if (restartTask != null) {
            restartTask.cancel();
        }

        currentType = type;
        secondsLeft.set(seconds);

        restartTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            int remaining = secondsLeft.getAndDecrement();

            if (remaining <= 0) {
                shutdown();
                return;
            }

            // Broadcast title and message at specific intervals
            if (remaining <= 10 || remaining % 30 == 0 || (remaining <= 60 && remaining % 15 == 0)) {
                broadcastRestart(remaining);
            }
        }).repeat(TaskSchedule.seconds(1)).schedule();
    }

    private static void broadcastRestart(int seconds) {
        String timeStr = formatTime(seconds);
        Component titleMain = text("<red><bold>SERVER RESTART");
        Component titleSub = text("<gray>This server is restarting for a " + currentType.getDisplayName() + "<gray> in <yellow>" + timeStr);

        Title title = Title.title(titleMain, titleSub, Title.Times.times(Duration.ofMillis(200), Duration.ofSeconds(2), Duration.ofMillis(200)));

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            player.showTitle(title);
            player.sendMessage(text("<red><b>[RESTART]</b> <gray>This server is restarting for a " + currentType.getDisplayName() + "<gray> in <yellow>" + timeStr + "<gray>!"));
        }
    }

    private static void shutdown() {
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            player.kick(text("<red>Server is restarting for " + currentType.getDisplayName() + "!\n<gray>Please rejoin in a moment."));
        }
        
        // Signal that we want a restart
        exitCode = 3;
        
        // Stop the server cleanly. This will cause MinecraftServer.start() to return in Main.
        MinecraftServer.stopCleanly();
    }

    private static String formatTime(int seconds) {
        if (seconds < 60) return seconds + "s";
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        if (remainingSeconds == 0) return minutes + "m";
        return minutes + "m " + remainingSeconds + "s";
    }
}
