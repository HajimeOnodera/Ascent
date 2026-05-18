package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.player.skill.SkillType;

public class XpUnlock extends CollectionUnlock {
    private final SkillType skillType;
    private final double amount;

    public XpUnlock(SkillType skillType, double amount) {
        this.skillType = skillType;
        this.amount = amount;
    }

    @Override
    public String getDisplay() {
        return "§9+" + (int)amount + " " + skillType.getDisplayName() + " XP";
    }

    @Override
    public void apply(SkyblockProfile profile) {
        profile.profilePlayers.forEach(pp -> {
            if (pp.skyblockPlayer != null) {
                SkillRegistry.grantXp(pp.skyblockPlayer, skillType, amount);
            }
        });
    }
}
