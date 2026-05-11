package fun.ascent.lobby.scoreboard;

import fun.ascent.common.StringUtility;
import fun.ascent.common.user.UserManager;
import fun.ascent.database.FriendLookup;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.MinecraftServer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static fun.ascent.common.StringUtility.text;

public class LobbyScoreboard {

    private final Player player;
    private final Sidebar sidebar;
    private int lastLineCount = 0;
    private int tickCount = 0;

    // Cached friend UUIDs – refreshed every ~10 seconds to avoid constant MongoDB queries
    private List<UUID> cachedFriendUuids = List.of();
    private long friendCacheExpiry = 0;
    private static final long FRIEND_CACHE_MS = 10_000;

    private static final String[] TITLE_ANIMATION = {
            "<yellow><bold>ASCENT",
            "<yellow><bold>ASCENT",
            "<yellow><bold>ASCENT",
            "<yellow><bold>ASCENT",
            "<yellow><bold>ASCENT",
            "<white><bold>A<yellow><bold>SCENT",
            "<yellow><bold>A<white><bold>S<yellow><bold>CENT",
            "<yellow><bold>AS<white><bold>C<yellow><bold>ENT",
            "<yellow><bold>ASC<white><bold>E<yellow><bold>NT",
            "<yellow><bold>ASCE<white><bold>N<yellow><bold>T",
            "<yellow><bold>ASCEN<white><bold>T",
            "<yellow><bold>ASCENT",
            "<yellow><bold>ASCENT",
            "<yellow><bold>ASCENT"
    };

    public LobbyScoreboard(Player player) {
        this.player = player;
        this.sidebar = new Sidebar(text(TITLE_ANIMATION[0]));
        this.sidebar.addViewer(player);
    }

    public void update() {
        tickCount++;
        sidebar.setTitle(text(TITLE_ANIMATION[tickCount % TITLE_ANIMATION.length]));

        List<String> renderedLines = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        renderedLines.add("<gray>" + LocalDate.now().format(formatter) + " <dark_gray>L1");
        renderedLines.add("");
        renderedLines.add("<white>Rank: <gray>" + StringUtility.toNormalCase(UserManager.getUser(player.getUuid()).getRank().name()));
        renderedLines.add("<white>Achievements: <yellow>" + UserManager.getUser(player.getUuid()).getAchievementPoints());
        renderedLines.add("<white>Ascent Level: <aqua>" + UserManager.getUser(player.getUuid()).getLevel());
        renderedLines.add(" ");
        renderedLines.add("<white>Lobby: <green>1");
        renderedLines.add("<white>Players: <green>" + MinecraftServer.getConnectionManager().getOnlinePlayers().size());
        renderedLines.add("  ");

        // Refresh cached friend list every 10 seconds
        long now = System.currentTimeMillis();
        if (now >= friendCacheExpiry) {
            cachedFriendUuids = FriendLookup.getFriendUuids(player.getUuid());
            friendCacheExpiry = now + FRIEND_CACHE_MS;
        }

        Set<UUID> onlineUuids = MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream().map(Player::getUuid).collect(Collectors.toSet());
        long friendsOnline = cachedFriendUuids.stream().filter(onlineUuids::contains).count();

        renderedLines.add("<white>Friends Online: <green>" + friendsOnline);
        renderedLines.add("<white>Guild Online: <gray>No guild!");
        renderedLines.add("   ");
        renderedLines.add("<yellow>play.ascent.fun");

        int score = renderedLines.size();

        for (int i = 0; i < renderedLines.size(); i++) {
            String lineId = "<gray>" + Integer.toHexString(i) + "<reset>";
            String content = renderedLines.get(i);

            if (i < lastLineCount) {
                sidebar.updateLineContent(lineId, text(content));
            } else {
                sidebar.createLine(
                        new Sidebar.ScoreboardLine(lineId, text(content), score));
            }
            score--;
        }

        for (int i = renderedLines.size(); i < lastLineCount; i++) {
            String lineId = "<gray>" + Integer.toHexString(i) + "<reset>";
            sidebar.removeLine(lineId);
        }

        lastLineCount = renderedLines.size();
    }

    public void destroy() {
        sidebar.removeViewer(player);
    }
}

