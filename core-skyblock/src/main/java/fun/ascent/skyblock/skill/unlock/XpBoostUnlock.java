package fun.ascent.skyblock.skill.unlock;

import fun.ascent.skyblock.player.SkyblockPlayer;

public class XpBoostUnlock extends SkillUnlock {

    private final int amount;

    public XpBoostUnlock(int amount) {
        this.amount = amount;
    }

    @Override
    public String display() {
        return "§8+§b" + amount + " SkyBlock XP";
    }

    @Override
    public void apply(SkyblockPlayer player) {
        // TODO integrate with SkyBlock XP system when added
    }
}