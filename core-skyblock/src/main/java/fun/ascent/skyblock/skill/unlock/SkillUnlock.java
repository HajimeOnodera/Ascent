package fun.ascent.skyblock.skill.unlock;

import fun.ascent.skyblock.player.SkyblockPlayer;

public abstract class SkillUnlock {

    public abstract String display();

    public abstract void apply(SkyblockPlayer player);
}