package fun.ascent.skyblock.player.skill;

import java.util.EnumMap;

public class PlayerSkillData {

    private static final int PROGRESS_BAR_SEGMENTS = 20;

    private final EnumMap<SkillType, Double> xpMap = new EnumMap<>(SkillType.class);

    public PlayerSkillData() {
        for (SkillType type : SkillType.values()) {
            xpMap.put(type, 0.0);
        }
    }

    public double getRawXp(SkillType type) {
        return xpMap.getOrDefault(type, 0.0);
    }

    public int getLevel(SkillType type) {
        return type.definition().levelFor(getRawXp(type));
    }

    public double getXpIntoCurrentLevel(SkillType type) {
        return type.definition().xpIntoCurrentLevel(getRawXp(type));
    }

    public Integer getNextLevel(SkillType type) {
        int current = getLevel(type);
        int max = type.definition().maxLevel();
        return current >= max ? null : current + 1;
    }

    public double getProgressToNextLevel(SkillType type) {
        Integer next = getNextLevel(type);
        if (next == null) return 1.0;

        SkillReward reward = type.definition().rewardAt(next);
        if (reward == null || reward.xpRequired() <= 0) return 0.0;

        double progress = getXpIntoCurrentLevel(type) / reward.xpRequired();
        return Math.max(0.0, Math.min(1.0, progress));
    }

    public String progressBar(SkillType type) {
        double progress = getProgressToNextLevel(type);
        int filled = (int) Math.floor(progress * PROGRESS_BAR_SEGMENTS);

        String complete = "§2§m" + "─".repeat(Math.min(filled, PROGRESS_BAR_SEGMENTS));
        String remaining = "§7§m" + "─".repeat(Math.max(PROGRESS_BAR_SEGMENTS - filled, 0));
        return complete + remaining + "§r";
    }

    public void setRawXp(SkillType type, double xp) {
        xpMap.put(type, Math.max(0.0, xp));
    }

    public void addXp(SkillType type, double xp) {
        setRawXp(type, getRawXp(type) + xp);
    }
}
