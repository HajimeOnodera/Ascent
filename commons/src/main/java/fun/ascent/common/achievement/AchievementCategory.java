package fun.ascent.common.achievement;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public enum AchievementCategory {

    GENERAL("General", Material.GRASS_BLOCK, new AchievementDef[]{
            new AchievementDef("Achievement Get! Ascent Server!", "Join the server for the first time.", 5),
            new AchievementDef("Let the world hear your voice!", "Use chat for the first time.", 5),
            new AchievementDef("First Friend", "Add your first friend.", 5),
            new AchievementDef("Social Butterfly", "Have 10 friends.", 10),
            new AchievementDef("Veteran", "Play for 100 hours.", 15),
            new AchievementDef("Loyal Player", "Log in for 30 consecutive days.", 20),
            new AchievementDef("Collector", "Collect 10 unique items.", 10),
            new AchievementDef("Explorer", "Visit every island.", 15),
    }),

    COMBAT("Combat", Material.DIAMOND_SWORD, new AchievementDef[]{
            new AchievementDef("First Kill", "Kill your first mob.", 5),
            new AchievementDef("Wrong Weapon", "Kill a mob with the wrong weapon.", 5),
            new AchievementDef("Monster Hunter", "Kill 100 mobs.", 10),
            new AchievementDef("Slayer", "Kill 1,000 mobs.", 15),
            new AchievementDef("Dragon Slayer", "Defeat the Ender Dragon.", 25),
            new AchievementDef("Wither Bane", "Defeat the Wither.", 25),
            new AchievementDef("Berserker", "Deal 10,000 damage in one hit.", 20),
    }),

    MINING("Mining", Material.DIAMOND_PICKAXE, new AchievementDef[]{
            new AchievementDef("First Mine", "Mine your first block.", 5),
            new AchievementDef("Stone Age", "Mine 100 blocks.", 5),
            new AchievementDef("Miner", "Mine 10,000 blocks.", 10),
            new AchievementDef("Deep Diver", "Reach the Deep Caverns.", 10),
            new AchievementDef("Crystal Hunter", "Find 5 Crystals in Crystal Hollows.", 15),
            new AchievementDef("Heart of the Mountain", "Unlock HOTM Tree.", 20),
    }),

    FARMING("Farming", Material.GOLDEN_HOE, new AchievementDef[]{
            new AchievementDef("First Harvest", "Harvest your first crop.", 5),
            new AchievementDef("Farmer", "Harvest 1,000 crops.", 10),
            new AchievementDef("Agricultural Master", "Harvest 100,000 crops.", 15),
            new AchievementDef("Garden Keeper", "Unlock the Garden.", 15),
            new AchievementDef("Crop King", "Reach Farming level 25.", 20),
    }),

    FISHING("Fishing", Material.FISHING_ROD, new AchievementDef[]{
            new AchievementDef("First Catch", "Catch your first fish.", 5),
            new AchievementDef("Angler", "Catch 100 fish.", 10),
            new AchievementDef("Sea Creature Hunter", "Kill 50 sea creatures.", 15),
            new AchievementDef("Trophy Fisher", "Catch a legendary sea creature.", 25),
    }),

    FORAGING("Foraging", Material.JUNGLE_LOG, new AchievementDef[]{
            new AchievementDef("Lumberjack", "Chop 100 logs.", 5),
            new AchievementDef("Tree Slayer", "Chop 10,000 logs.", 10),
            new AchievementDef("Foraging Expert", "Reach Foraging level 25.", 20),
    }),

    DUNGEONS("Dungeons", Material.WITHER_SKELETON_SKULL, new AchievementDef[]{
            new AchievementDef("Dungeon Crawler", "Complete your first dungeon.", 10),
            new AchievementDef("Floor Clearer", "Complete Floor 7.", 20),
            new AchievementDef("Master Mode", "Complete a Master Mode floor.", 25),
            new AchievementDef("Catacombs Expert", "Reach Catacombs level 30.", 20),
    }),

    SKILLS("Skills & Misc", Material.EXPERIENCE_BOTTLE, new AchievementDef[]{
            new AchievementDef("Jack of All Trades", "Reach level 10 in all skills.", 15),
            new AchievementDef("Master of All", "Reach level 25 in all skills.", 25),
            new AchievementDef("Enchanter", "Enchant 50 items.", 10),
            new AchievementDef("Alchemist", "Brew 100 potions.", 10),
            new AchievementDef("Millionaire", "Earn 1,000,000 coins.", 15),
            new AchievementDef("Billionaire", "Earn 1,000,000,000 coins.", 25),
    });

    private final String displayName;
    private final Material icon;
    private final AchievementDef[] achievements;

    AchievementCategory(String displayName, Material icon, AchievementDef[] achievements) {
        this.displayName = displayName;
        this.icon = icon;
        this.achievements = achievements;
    }

    public int totalPoints() {
        int total = 0;
        for (AchievementDef a : achievements) {
            total += a.points();
        }
        return total;
    }

    public static int totalAchievementCount() {
        int total = 0;
        for (AchievementCategory cat : values()) {
            total += cat.achievements.length;
        }
        return total;
    }

    public static int totalPointsAll() {
        int total = 0;
        for (AchievementCategory cat : values()) {
            total += cat.totalPoints();
        }
        return total;
    }

    public record AchievementDef(String name, String description, int points) {}
}
