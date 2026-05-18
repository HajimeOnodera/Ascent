package fun.ascent.skyblock.player.skill.unlock;

import fun.ascent.skyblock.player.SkyblockPlayer;

public class XpBoostUnlock extends SkillUnlock {

    private final int amount;

    public XpBoostUnlock(int amount) {
        this.amount = amount;
    }

    @Override
    public String display() {
        return "<dark_gray>+<aqua>" + amount + " SkyBlock XP";
    }

    @Override
    public void apply(SkyblockPlayer player) {
        if (player.getActiveProfileData() == null) return;
        player.getActiveProfileData().addSkyblockXp(amount);
    }
}
