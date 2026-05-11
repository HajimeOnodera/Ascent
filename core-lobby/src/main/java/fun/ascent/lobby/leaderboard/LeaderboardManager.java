package fun.ascent.lobby.leaderboard;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardManager {

    private final List<Leaderboard> leaderboards = new ArrayList<>();

    public void init(Instance instance) {
        // Define categories
        List<Leaderboard.Category> playerCategories = List.of(
                new Leaderboard.Category("Ascent Level", "profile.level", "<gold>", 
                        val -> val == null ? "1" : String.valueOf(val)),
                new Leaderboard.Category("Achievement Points", "achievements.points", "<yellow>", 
                        val -> val == null ? "0" : String.valueOf(val)),
                new Leaderboard.Category("Top Playtime", "playtime.totalMs", "<aqua>", 
                        val -> {
                            if (val == null) return "0h";
                            long ms = ((Number) val).longValue();
                            long hours = ms / (1000 * 60 * 60);
                            return hours + "h";
                        })
        );

        // Center Wall (Achievement Points)
        Leaderboard centerLB = new Leaderboard("achievements", instance, 
                new Pos(-33.5, 93, 54, 130, 3), 
                new Pos(-33.5, 90.5, 48, 130, 3), playerCategories, true);
        centerLB.setCategoryIndex(1);
        centerLB.spawn();
        leaderboards.add(centerLB);

        // Right Wall (Top Playtime)
        Leaderboard rightLB = new Leaderboard("playtime", instance, 
                new Pos(-38.5, 93, 57, 175, 0), 
                null, playerCategories, false);
        rightLB.setCategoryIndex(2);
        rightLB.spawn();
        leaderboards.add(rightLB);

        // Left Wall (Ascent Level)
        Leaderboard leftLB = new Leaderboard("levels", instance, 
                new Pos(-31, 93, 49.5, 90, 0), 
                null, playerCategories, false);
        leftLB.setCategoryIndex(0);
        leftLB.spawn();
        leaderboards.add(leftLB);
    }

    public void registerListeners(GlobalEventHandler handler) {
        handler.addListener(PlayerEntityInteractEvent.class, event -> {
            for (Leaderboard lb : leaderboards) {
                lb.handleInteract(event.getPlayer(), event.getTarget(), leaderboards);
            }
        });
    }
}
