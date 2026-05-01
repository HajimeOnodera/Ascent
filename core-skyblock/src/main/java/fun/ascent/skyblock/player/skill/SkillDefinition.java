package fun.ascent.skyblock.player.skill;

import net.minestom.server.item.Material;

import java.util.List;

public abstract class SkillDefinition {

    private static final int[] SKILL_XP_TABLE = {
            50, 125, 200, 300, 500, 750, 1000, 1500, 2000, 3500,
            5000, 7500, 10000, 15000, 20000, 30000, 50000, 75000, 100000, 200000,
            300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000, 1100000, 1200000,
            1300000, 1400000, 1500000, 1600000, 1700000, 1800000, 1900000, 2000000, 2100000, 2200000,
            2300000, 2400000, 2500000, 2600000, 2750000, 2900000, 3100000, 3400000, 3700000, 4000000,
            4300000, 4600000, 4900000, 5200000, 5500000, 5800000, 6100000, 6400000, 6700000, 7000000
    };

    private static final int[] RUNECRAFTING_XP_TABLE = {
            50, 100, 125, 160, 200, 250, 315, 400, 500, 625,
            785, 1000, 1250, 1565, 2000, 2500, 3125, 4000, 5000, 6250,
            7850, 9800, 12250, 15300, 19100
    };

    private static final int[] COIN_REWARD_TABLE = {
            100, 250, 500, 750, 1000, 2000, 3000, 4000, 5000, 7500,
            10000, 15000, 20000, 25000, 30000, 40000, 50000, 65000, 80000, 100000,
            125000, 150000, 175000, 200000, 225000, 250000, 275000, 300000, 325000, 350000,
            375000, 400000, 425000, 450000, 475000, 500000, 550000, 600000, 650000, 700000,
            750000, 800000, 850000, 900000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000,
            1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000
    };

    public abstract String name();

    public abstract Material icon();

    public abstract List<String> description();

    public abstract SkillReward[] rewards();

    public int maxLevel() {
        return rewards().length;
    }

    public void validate() {
        SkillReward[] rewards = rewards();
        if (rewards == null || rewards.length == 0) {
            throw new IllegalStateException(name() + " must define at least one reward");
        }

        for (int i = 0; i < rewards.length; i++) {
            SkillReward reward = rewards[i];
            int expectedLevel = i + 1;

            if (reward == null) {
                throw new IllegalStateException(name() + " has a null reward at level " + expectedLevel);
            }
            if (reward.level() != expectedLevel) {
                throw new IllegalStateException(name() + " reward table skips level " + expectedLevel);
            }
        }
    }

    public SkillReward rewardAt(int level) {
        if (level < 1 || level > maxLevel()) return null;
        return rewards()[level - 1];
    }

    public int levelFor(double totalXp) {
        int level = 0;
        double accumulated = 0;

        for (SkillReward reward : rewards()) {
            accumulated += reward.xpRequired();
            if (totalXp >= accumulated) {
                level++;
            } else {
                break;
            }
        }

        return level;
    }

    public double xpIntoCurrentLevel(double totalXp) {
        double accumulated = 0;
        for (SkillReward reward : rewards()) {
            double next = accumulated + reward.xpRequired();
            if (totalXp < next) {
                return totalXp - accumulated;
            }
            accumulated = next;
        }
        return 0;
    }

    protected static int[] standardTable() {
        return SKILL_XP_TABLE.clone();
    }

    protected static int[] runecraftingTable() {
        return RUNECRAFTING_XP_TABLE.clone();
    }

    protected static int[] carpentryTable() {
        int[] table = new int[50];
        System.arraycopy(SKILL_XP_TABLE, 0, table, 0, table.length);
        return table;
    }

    protected static int skyBlockXpReward(int level) {
        if (level <= 10) return 5;
        if (level <= 25) return 10;
        if (level <= 50) return 20;
        return 30;
    }

    protected static int coinReward(int level) {
        if (level < 1 || level > COIN_REWARD_TABLE.length) {
            throw new IllegalArgumentException("No coin reward configured for level " + level);
        }
        return COIN_REWARD_TABLE[level - 1];
    }
}
