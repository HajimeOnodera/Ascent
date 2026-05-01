package fun.ascent.skyblock.skill.event;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.skill.SkillType;
import lombok.Getter;
import net.minestom.server.event.Event;

@Getter
public class SkillXpGainEvent implements Event {

    private final SkyblockPlayer player;
    private final SkillType skillType;
    private final double xpGained;
    private final double oldXp;
    private final double newXp;
    private final int oldLevel;
    private final int newLevel;

    public SkillXpGainEvent(SkyblockPlayer player, SkillType skillType, double xpGained,
                            double oldXp, double newXp, int oldLevel, int newLevel) {
        this.player = player;
        this.skillType = skillType;
        this.xpGained = xpGained;
        this.oldXp = oldXp;
        this.newXp = newXp;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public boolean leveledUp() {
        return newLevel > oldLevel;
    }
}