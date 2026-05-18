package fun.ascent.skyblock.player.level.causes;

import lombok.Getter;
import fun.ascent.skyblock.player.level.abstr.CauseEmblem;
import fun.ascent.skyblock.player.level.abstr.SkyBlockLevelCauseAbstr;
import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.player.SkyblockPlayer;

@Getter
public class SkillLevelCause extends SkyBlockLevelCauseAbstr implements CauseEmblem {
    private final SkillType category;
    private final int level;

    public SkillLevelCause(SkillType category, int level) {
        this.category = category;
        this.level = level;
    }

    @Override
    public double xpReward() {
        return 10;
    }

    @Override
    public String getEmblemRequiresMessage() {
        return "Requires " + category.getDisplayName() + " Skill " + level;
    }

    @Override
    public String emblemEisplayName() {
        return category.getDisplayName() + " Skill " + level;
    }

    @Override
    public boolean shouldDisplayMessage(SkyblockPlayer player) {
        return false;
    }
}
