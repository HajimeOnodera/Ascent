package fun.ascent.lobby.scoreboard;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.timer.TaskSchedule;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static fun.ascent.common.StringUtility.color;

public class LobbyScoreboardManager {
    private static final Map<UUID, LobbyScoreboard> scoreboards = new HashMap<>();

    public static void init() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> remove(event.getPlayer()));

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                LobbyScoreboard scoreboard = scoreboards.get(player.getUuid());
                if (scoreboard == null) {
                    scoreboard = new LobbyScoreboard(player);
                    scoreboards.put(player.getUuid(), scoreboard);
                }
                scoreboard.update();
                updateTablist(player);
            }
        }).repeat(TaskSchedule.tick(4)).schedule();
    }

    public static void remove(Player player) {
        LobbyScoreboard sb = scoreboards.remove(player.getUuid());
        if (sb != null) {
            sb.destroy();
        }
    }

    private static void updateTablist(Player player) {
        player.sendPlayerListHeaderAndFooter(
                color(" &bYou are playing on &e&lPLAY.ASCENT.FUN \n"),
                color("\n &aRanks, Boosters & MORE! &cSTORE.ASCENT.FUN ")
        );
    }
}
