package fun.ascent.lobby.scoreboard;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.MinecraftServer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LobbyScoreboard {

    private final Player player;
    private final Sidebar sidebar;
    private int lastLineCount = 0;
    private int tickCount = 0;

    private static final String[] TITLE_ANIMATION = {
            "<yellow><bold>ASCENT</bold></yellow>",
            "<yellow><bold>ASCENT</bold></yellow>",
            "<yellow><bold>ASCENT</bold></yellow>",
            "<yellow><bold>ASCENT</bold></yellow>",
            "<yellow><bold>ASCENT</bold></yellow>",
            "<white><bold>A</bold></white><yellow><bold>SCENT</bold></yellow>",
            "<yellow><bold>A</bold></yellow><white><bold>S</bold></white><yellow><bold>CENT</bold></yellow>",
            "<yellow><bold>AS</bold></yellow><white><bold>C</bold></white><yellow><bold>ENT</bold></yellow>",
            "<yellow><bold>ASC</bold></yellow><white><bold>E</bold></white><yellow><bold>NT</bold></yellow>",
            "<yellow><bold>ASCE</bold></yellow><white><bold>N</bold></white><yellow><bold>T</bold></yellow>",
            "<yellow><bold>ASCEN</bold></yellow><white><bold>T</bold></white>",
            "<yellow><bold>ASCENT</bold></yellow>",
            "<yellow><bold>ASCENT</bold></yellow>",
            "<yellow><bold>ASCENT</bold></yellow>"
    };

    public LobbyScoreboard(Player player) {
        this.player = player;
        this.sidebar = new Sidebar(MiniMessage.miniMessage().deserialize(TITLE_ANIMATION[0]));
        this.sidebar.addViewer(player);
    }

    public void update() {
        tickCount++;
        sidebar.setTitle(MiniMessage.miniMessage().deserialize(TITLE_ANIMATION[tickCount % TITLE_ANIMATION.length]));

        List<String> renderedLines = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        renderedLines.add("<gray>" + LocalDate.now().format(formatter) + " <dark_gray>L1</dark_gray></gray>");
        renderedLines.add("");
        renderedLines.add("<white>Rank:</white> <gray>Default</gray>");
        renderedLines.add("<white>Achievements:</white> <yellow>0</yellow>");
        renderedLines.add("<white>Ascent Level:</white> <aqua>0</aqua>");
        renderedLines.add(" ");
        renderedLines.add("<white>Lobby:</white> <green>1</green>");
        renderedLines.add("<white>Players:</white> <green>" + MinecraftServer.getConnectionManager().getOnlinePlayers().size() + "</green>");
        renderedLines.add("  ");
        renderedLines.add("<white>Friends Online:</white> <green>0</green>");
        renderedLines.add("<white>Guild Online:</white> <gray>No guild!</gray>");
        renderedLines.add("   ");
        renderedLines.add("<yellow>play.ascent.fun</yellow>");

        int score = renderedLines.size();

        for (int i = 0; i < renderedLines.size(); i++) {
            String lineId = "§7" + Integer.toHexString(i) + "§r";
            String content = renderedLines.get(i);

            if (i < lastLineCount) {
                sidebar.updateLineContent(lineId, MiniMessage.miniMessage().deserialize(content));
            } else {
                sidebar.createLine(
                        new Sidebar.ScoreboardLine(lineId, MiniMessage.miniMessage().deserialize(content), score));
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
