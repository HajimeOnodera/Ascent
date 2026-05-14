package fun.ascent.skyblock.player.scoreboard;

import fun.ascent.skyblock.calendar.Calendar;
import fun.ascent.skyblock.dungeon.DungeonInstance;
import fun.ascent.skyblock.dungeon.DungeonManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.location.SkyblockLocation;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.scoreboard.Sidebar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdvancedScoreboard {

    public interface ScoreboardBlock {
        List<String> render(SkyblockPlayer player);
    }

    private static final String[] TITLE_ANIMATION = {
            "<yellow><bold>SKYBLOCK</bold></yellow>",
            "<yellow><bold>SKYBLOCK</bold></yellow>",
            "<yellow><bold>SKYBLOCK</bold></yellow>",
            "<yellow><bold>SKYBLOCK</bold></yellow>",
            "<yellow><bold>SKYBLOCK</bold></yellow>",
            "<white><bold>S</bold></white><yellow><bold>KYBLOCK</bold></yellow>",
            "<yellow><bold>S</bold></yellow><white><bold>K</bold></white><yellow><bold>YBLOCK</bold></yellow>",
            "<yellow><bold>SK</bold></yellow><white><bold>Y</bold></white><yellow><bold>BLOCK</bold></yellow>",
            "<yellow><bold>SKY</bold></yellow><white><bold>B</bold></white><yellow><bold>LOCK</bold></yellow>",
            "<yellow><bold>SKYB</bold></yellow><white><bold>L</bold></white><yellow><bold>OCK</bold></yellow>",
            "<yellow><bold>SKYBL</bold></yellow><white><bold>O</bold></white><yellow><bold>CK</bold></yellow>",
            "<yellow><bold>SKYBLO</bold></yellow><white><bold>C</bold></white><yellow><bold>K</bold></yellow>",
            "<yellow><bold>SKYBLOC</bold></yellow><white><bold>K</bold></white>",
            "<yellow><bold>SKYBLOCK</bold></yellow>",
            "<yellow><bold>SKYBLOCK</bold></yellow>",
            "<yellow><bold>SKYBLOCK</bold></yellow>"
    };

    private final SkyblockPlayer player;
    private final Sidebar sidebar;
    private final List<ScoreboardBlock> blocks = new ArrayList<>();
    private int lastLineCount = 0;
    private int tickCount = 0;

    public AdvancedScoreboard(SkyblockPlayer player) {
        this.player = player;
        this.sidebar = new Sidebar(MiniMessage.miniMessage().deserialize(TITLE_ANIMATION[0]));
        this.sidebar.addViewer(player);

        blocks.add(new HeaderBlock());
        blocks.add(new CalendarBlock());
        blocks.add(new LocationBlock());
        blocks.add(new EconomyBlock());
        blocks.add(new FooterBlock());
    }

    public void update() {
        tickCount++;
        sidebar.setTitle(MiniMessage.miniMessage().deserialize(TITLE_ANIMATION[tickCount % TITLE_ANIMATION.length]));

        List<String> renderedLines = new ArrayList<>();

        for (ScoreboardBlock block : blocks) {
            List<String> blockLines = block.render(player);
            if (blockLines != null && !blockLines.isEmpty()) {
                renderedLines.addAll(blockLines);
            }
        }

        if (renderedLines.size() > 15) {
            renderedLines = renderedLines.subList(0, 15);
        }

        int score = renderedLines.size();

        for (int i = 0; i < renderedLines.size(); i++) {
            String lineId = "§" + Integer.toHexString(i) + "§r";
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
            String lineId = "§" + Integer.toHexString(i) + "§r";
            sidebar.removeLine(lineId);
        }

        lastLineCount = renderedLines.size();
    }

    public void destroy() {
        sidebar.removeViewer(player);
    }

    public static class HeaderBlock implements ScoreboardBlock {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        @Override
        public List<String> render(SkyblockPlayer player) {
            return List.of(
                    "<gray>" + LocalDate.now().format(formatter) + " <dark_gray>megaC1R</dark_gray></gray>",
                    "");
        }
    }

    public static class CalendarBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            Calendar.SkyBlockTime time = Calendar.getCachedTime();
            if (time == null)
                return List.of("<gray>Loading...</gray>", "");

            return List.of(
                    "<white> " + time.getMonthName() + " " + time.getOrdinalDay() + "</white>",
                    "<gray> " + time.getHourFormatted() + "</gray>"
            );
        }
    }

    public static class LocationBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            DungeonInstance dungeon = DungeonManager.get().getDungeon(player.getUuid());
            if (dungeon != null && dungeon.instance().equals(player.getInstance())) {
                return List.of("<gray> ⏣</gray> <red>The Catacombs</red> <gray>(" + dungeon.floor().shortName() + ")</gray>", "");
            }
            SkyblockLocation location = SkyblockLocation.getLocation(player.getInstance(), player.getPosition());
            return List.of("<gray> ⏣</gray> " + location.getName(), "");
        }
    }

    public static class EconomyBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            return List.of(
                    "<white>Purse:</white> <gold>" + player.getActiveProfileData().playerCoins + "</gold>",
                    "");
        }
    }

    public static class FooterBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            return List.of(
                    "<yellow>ascent.eu</yellow>");
        }
    }
}
