package fun.ascent.skyblock.player.scoreboard;

import fun.ascent.skyblock.calendar.Calendar;
import fun.ascent.skyblock.dungeon.DungeonServiceRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.region.Region;
import fun.ascent.skyblock.world.region.RegionManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.scoreboard.Sidebar;
import fun.ascent.skyblock.quest.ActiveQuest;
import fun.ascent.skyblock.quest.Quest;
import fun.ascent.skyblock.quest.QuestData;
import fun.ascent.skyblock.quest.QuestProgress;
import fun.ascent.skyblock.quest.LocationAssociatedQuest;
import net.minestom.server.coordinate.Pos;

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
        blocks.add(new ObjectiveBlock());
        blocks.add(new FooterBlock());
    }

    public void update() {
        tickCount++;
        sidebar.setTitle(MiniMessage.miniMessage().deserialize(TITLE_ANIMATION[tickCount % TITLE_ANIMATION.length]));

        List<String> renderedLines = new ArrayList<>();

        for (ScoreboardBlock block : blocks) {
            try {
                List<String> lines = block.render(player);
                if (lines != null) {
                    renderedLines.addAll(lines);
                }
            } catch (Exception e) {
                System.err.println("Error in block " + block.getClass().getSimpleName());
                e.printStackTrace();
            }
        }

        while (renderedLines.size() > 15) {
            renderedLines.removeLast();
        }

        int score = renderedLines.size();
        for (int i = 0; i < renderedLines.size(); i++) {
            String lineId = "§" + Integer.toHexString(i) + "§r";
            String content = renderedLines.get(i);
            if (content == null || content.isEmpty()) content = " ";

            if (i < lastLineCount) {
                sidebar.updateLineContent(lineId, MiniMessage.miniMessage().deserialize(content));
            } else {
                sidebar.createLine(new Sidebar.ScoreboardLine(lineId, MiniMessage.miniMessage().deserialize(content), score));
            }
            score--;
        }

        for (int i = renderedLines.size(); i < lastLineCount; i++) {
            sidebar.removeLine("§" + Integer.toHexString(i) + "§r");
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
            return List.of("<gray>" + LocalDate.now().format(formatter) + " <dark_gray>megaC1R</dark_gray>", "");
        }
    }

    public static class CalendarBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            Calendar.SkyBlockTime time = Calendar.getCachedTime();
            if (time == null) return List.of("<gray>Loading...");
            return List.of(
                    "<white> " + time.getMonthName() + " " + time.getOrdinalDay() + "</white>",
                    "<gray> " + time.getHourFormatted() + "</gray>"
            );
        }
    }

    public static class LocationBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            String locStr = "<gray> ⏣</gray> <gray>None";
            try {
                String dName = null;
                if (DungeonServiceRegistry.get() != null) {
                    dName = DungeonServiceRegistry.get().getDungeonName(player.getUuid(), player.getInstance());
                }
                if (dName != null) {
                    locStr = dName;
                } else {
                    Region region = RegionManager.getRegion(player.getInstance(), player.getPosition());
                    if (region != null) {
                        locStr = "<gray> ⏣</gray> " + region.getType().toString();
                    }
                }
            } catch (Exception ignored) {}
            return List.of(locStr, "");
        }
    }

    public static class EconomyBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            double coins = 0;
            double bits = 0;
            try {
                if (player.getActiveProfileData() != null) {
                    coins = player.getActiveProfileData().playerCoins;
                    bits = player.getActiveProfileData().playerBits;
                }
            } catch (Exception ignored) {}
            
            List<String> lines = new ArrayList<>();
            lines.add("<white>Purse:</white> <gold>" + String.format("%.1f", coins));
            if (bits > 0) {
                lines.add("<white>Bits:</white> <aqua>" + String.format("%,.0f", bits));
            }
            lines.add("");
            return lines;
        }
    }

    public static class ObjectiveBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            if (player.getActiveProfileData() == null) return List.of();
            QuestData questData = player.getActiveProfileData().getQuestData();
            if (questData == null || questData.getActiveQuests() == null || questData.getActiveQuests().isEmpty()) {
                return List.of();
            }

            ActiveQuest activeQuest = questData.getActiveQuests().getFirst();
            Quest quest = QuestData.getQuestClass(activeQuest.getQuestID());
            if (quest == null) return List.of();

            List<String> lines = new ArrayList<>();

            if (quest instanceof LocationAssociatedQuest locationQuest) {
                String arrow = getArrow(player.getPosition(), locationQuest.getLocation());
                lines.add("<white>Objective " + arrow + "</white>");
            } else {
                lines.add("<white>Objective</white>");
            }

            lines.add("<yellow>" + quest.getName() + "</yellow>");

            if (quest instanceof QuestProgress progressQuest) {
                lines.add(" <gray>(</gray><yellow>" + activeQuest.getProgress() + "</yellow><gray>/</gray><green>" + progressQuest.getMaxProgress() + "</green><gray>)</gray>");
            }

            lines.add("");
            return lines;
        }

        private String getArrow(Pos from, Pos to) {
            if (from.x() == to.x() && from.y() == to.y() && from.z() == to.z()) {
                return "•";
            }

            Pos lookingAt = from.withLookAt(to);
            float targetYaw = lookingAt.yaw();

            float currentYaw = from.yaw();
            float yawDifference = targetYaw - currentYaw;

            while (yawDifference > 180) yawDifference -= 360;
            while (yawDifference < -180) yawDifference += 360;

            float relativeAngle = yawDifference < 0 ? yawDifference + 360 : yawDifference;

            relativeAngle += 22.5f;
            if (relativeAngle >= 360) {
                relativeAngle -= 360;
            }

            int direction = (int) (relativeAngle / 45.0f);

            String color = from.distance(to) > 20 ? "<yellow><bold>" : "<green><bold>";
            String closeTag = "</bold></" + (from.distance(to) > 20 ? "yellow" : "green") + ">";
            return switch (direction) {
                case 0 -> color + "⬆" + closeTag;
                case 1 -> color + "⬈" + closeTag;
                case 2 -> color + "➡" + closeTag;
                case 3 -> color + "⬊" + closeTag;
                case 4 -> color + "⬇" + closeTag;
                case 5 -> color + "⬋" + closeTag;
                case 6 -> color + "⬅" + closeTag;
                case 7 -> color + "⬉" + closeTag;
                default -> "•";
            };
        }
    }

    public static class FooterBlock implements ScoreboardBlock {
        @Override
        public List<String> render(SkyblockPlayer player) {
            return List.of("<yellow>ascent.eu</yellow>");
        }
    }
}
