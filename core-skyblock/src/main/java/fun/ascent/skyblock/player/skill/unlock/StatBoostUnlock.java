package fun.ascent.skyblock.player.skill.unlock;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;

public class StatBoostUnlock extends SkillUnlock {

    private final Stats stat;
    private final int amount;
    private final boolean percentage;

    public StatBoostUnlock(Stats stat, int amount) {
        this(stat, amount, false);
    }

    public StatBoostUnlock(Stats stat, int amount, boolean percentage) {
        this.stat = stat;
        this.amount = amount;
        this.percentage = percentage;
    }

    @Override
    public String display() {
        String suffix = percentage ? "%" : "";
        return "§8+§a" + amount + suffix + " " + stat.getStatColor() + stat.getStatFormattedDisplay();
    }

    @Override
    public void apply(SkyblockPlayer player) {
        if (player.getActiveProfileData() == null) return;
        player.getActiveProfileData().addToStat(stat, amount);
    }
}