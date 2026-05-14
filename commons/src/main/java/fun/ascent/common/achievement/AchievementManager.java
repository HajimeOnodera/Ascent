package fun.ascent.common.achievement;

import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import fun.ascent.database.PlayerRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AchievementManager {

    private static final String BASE_PATH = "achievements";
    private static final UUID GLOBAL_UUID = new UUID(0, 1); // Use a distinct UUID for global data

    public static void unlock(Player player, AchievementCategory category, String achievementName) {
        UUID uuid = player.getUuid();
        String catName = category.name().toLowerCase();
        String achKey = achievementName.toLowerCase().replace(" ", "_");

        Document achievements = PlayerRepository.getSection(uuid, BASE_PATH);
        if (achievements == null) achievements = new Document();

        Document categories = achievements.get("categories", new Document());
        Document catData = categories.get(catName, new Document());
        List<String> list = catData.getList("list", String.class, new ArrayList<>());

        if (list.contains(achKey)) return;

        AchievementCategory.AchievementDef def = null;
        for (AchievementCategory.AchievementDef ad : category.getAchievements()) {
            if (ad.name().equalsIgnoreCase(achievementName)) {
                def = ad;
                break;
            }
        }
        if (def == null) return;

        list.add(achKey);
        catData.put("list", list);
        catData.put("completed", list.size());
        
        int currentCatPoints = catData.getInteger("points", 0);
        catData.put("points", currentCatPoints + def.points());
        
        categories.put(catName, catData);
        achievements.put("categories", categories);

        int totalCompleted;
        Object completedObj = achievements.get("completed");
        if (completedObj instanceof Number n) {
            totalCompleted = n.intValue() + 1;
        } else {
            totalCompleted = list.size();
        }

        int currentTotalPoints = 0;
        Object totalPointsObj = achievements.get("points");
        if (totalPointsObj instanceof Number n) {
            currentTotalPoints = n.intValue();
        }

        int totalPoints = currentTotalPoints + def.points();
        achievements.put("completed", totalCompleted);
        achievements.put("points", totalPoints);

        PlayerRepository.setSection(uuid, BASE_PATH, achievements);

        User user = UserManager.getUser(uuid);
        if (user != null) {
            user.setAchievementPoints(totalPoints);
            UserManager.saveUser(user);
        }

        // Update global unlock stats
        incrementGlobalStat("unlocks." + achKey);

        broadcastUnlock(player, def);
    }

    public static void registerFirstJoin(Player player) {
        Document achievements = PlayerRepository.getSection(player.getUuid(), BASE_PATH);
        // If they have no achievements at all, it's their first time being registered in the system
        if (achievements == null || achievements.isEmpty() || achievements.getInteger("completed", 0) == 0) {
            incrementGlobalStat("total_players");
        }
    }

    private static synchronized void incrementGlobalStat(String path) {
        Document global = PlayerRepository.getSection(GLOBAL_UUID, "stats");
        if (global == null) global = new Document();
        
        String[] parts = path.split("\\.");
        Document current = global;
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (!current.containsKey(part)) {
                current.put(part, new Document());
            }
            current = (Document) current.get(part);
        }
        
        String key = parts[parts.length - 1];
        int val = 0;
        if (current.containsKey(key)) {
            Object o = current.get(key);
            if (o instanceof Number n) val = n.intValue();
        }
        current.put(key, val + 1);
        
        PlayerRepository.setSection(GLOBAL_UUID, "stats", global);
    }

    public static double getUnlockPercentage(String achievementName) {
        String achKey = achievementName.toLowerCase().replace(" ", "_");
        Document global = PlayerRepository.getSection(GLOBAL_UUID, "stats");
        if (global == null) return 0.0;

        int totalPlayers = 0;
        if (global.containsKey("total_players")) {
            Object o = global.get("total_players");
            if (o instanceof Number n) totalPlayers = n.intValue();
        }
        
        if (totalPlayers == 0) return 0.0;

        Document unlocks = (Document) global.get("unlocks");
        if (unlocks == null) return 0.0;
        
        int unlockCount = 0;
        if (unlocks.containsKey(achKey)) {
            Object o = unlocks.get(achKey);
            if (o instanceof Number n) unlockCount = n.intValue();
        }

        double percent = ((double) unlockCount / totalPlayers) * 100.0;
        return Math.min(100.0, percent);
    }

    private static void broadcastUnlock(Player player, AchievementCategory.AchievementDef def) {
        Component message = Component.text()
                .append(Component.text(">> ", NamedTextColor.GREEN))
                .append(Component.text("Achievement Unlocked: ", NamedTextColor.GREEN))
                .append(Component.text(def.name(), NamedTextColor.GOLD))
                .append(Component.text(" <<", NamedTextColor.GREEN))
                .build();

        double percentage = getUnlockPercentage(def.name());
        String percentStr = String.format("%.2f", percentage);

        Component hoverText = Component.text()
                .append(Component.text(def.name(), NamedTextColor.GREEN))
                .append(Component.newline())
                .append(Component.text(def.description(), NamedTextColor.GRAY))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("Reward:", NamedTextColor.WHITE))
                .append(Component.newline())
                .append(Component.text("+" + def.points() + " Achievement Points", NamedTextColor.YELLOW))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("Unlocked by " + percentStr + "% of players!", NamedTextColor.GRAY))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("Achievement unlocked!", NamedTextColor.GREEN))
                .build();

        player.sendMessage(message
                .hoverEvent(HoverEvent.showText(hoverText))
                .clickEvent(ClickEvent.runCommand("/achievements")));
    }
}
