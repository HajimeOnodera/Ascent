package fun.ascent.skyblock.player.skill.event;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.skill.SkillType;
import net.minestom.server.event.Event;

public record SkillXpGainEvent(SkyblockPlayer player, SkillType skillType, double xpGained, double oldXp, double newXp,
                               int oldLevel, int newLevel) implements Event {

    public boolean leveledUp() {
        return newLevel > oldLevel;
    }
}
