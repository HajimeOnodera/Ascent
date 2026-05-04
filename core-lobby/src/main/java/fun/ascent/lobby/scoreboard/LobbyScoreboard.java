package fun.ascent.lobby.scoreboard;

import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.MinecraftServer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.util.CC.c;

public class LobbyScoreboard {

    private final Player player;
    private final Sidebar sidebar;
    private int lastLineCount = 0;
    private int tickCount = 0;

    private static final String[] TITLE_ANIMATION = {
            "&e&lASCENT",
            "&e&lASCENT",
            "&e&lASCENT",
            "&e&lASCENT",
            "&e&lASCENT",
            "&f&lA&e&lSCENT",
            "&e&lA&f&lS&e&lCENT",
            "&e&lAS&f&lC&e&lENT",
            "&e&lASC&f&lE&e&lNT",
            "&e&lASCE&f&lN&e&lT",
            "&e&lASCEN&f&lT",
            "&e&lASCENT",
            "&e&lASCENT",
            "&e&lASCENT"
    };

    public LobbyScoreboard(Player player) {
        this.player = player;
        this.sidebar = new Sidebar(c(TITLE_ANIMATION[0]));
        this.sidebar.addViewer(player);
    }

    public void update() {
        tickCount++;
        sidebar.setTitle(c(TITLE_ANIMATION[tickCount % TITLE_ANIMATION.length]));

        List<String> renderedLines = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        renderedLines.add("&7" + LocalDate.now().format(formatter) + " &8L1");
        renderedLines.add("");
        renderedLines.add("&fRank: &7Default");
        renderedLines.add("&fAchievements: &e0");
        renderedLines.add("&fAscent Level: &b0");
        renderedLines.add(" ");
        renderedLines.add("&fLobby: &a1");
        renderedLines.add("&fPlayers: &a" + MinecraftServer.getConnectionManager().getOnlinePlayers().size());
        renderedLines.add("  ");
        renderedLines.add("&fFriends Online: &a0");
        renderedLines.add("&fGuild Online: &7No guild!");
        renderedLines.add("   ");
        renderedLines.add("&eplay.ascent.fun");

        int score = renderedLines.size();

        for (int i = 0; i < renderedLines.size(); i++) {
            String lineId = "§7" + Integer.toHexString(i) + "§r";
            String content = renderedLines.get(i);

            if (i < lastLineCount) {
                sidebar.updateLineContent(lineId, c(content));
            } else {
                sidebar.createLine(
                        new Sidebar.ScoreboardLine(lineId, c(content), score));
            }
            score--;
        }

        for (int i = renderedLines.size(); i < lastLineCount; i++) {
            String lineId = "§7" + Integer.toHexString(i) + "§r";
            sidebar.removeLine(lineId);
        }

        lastLineCount = renderedLines.size();
    }

    public void destroy() {
        sidebar.removeViewer(player);
    }
}
