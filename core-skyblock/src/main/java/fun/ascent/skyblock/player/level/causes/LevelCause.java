package fun.ascent.skyblock.player.level.causes;

import lombok.Getter;
import fun.ascent.skyblock.player.level.abstr.CauseEmblem;
import fun.ascent.skyblock.player.level.abstr.SkyBlockLevelCauseAbstr;
import fun.ascent.skyblock.player.SkyblockPlayer;

@Getter
public class LevelCause extends SkyBlockLevelCauseAbstr implements CauseEmblem {
    private final int level;

    public LevelCause(int level) {
        this.level = level;
    }

    @Override
    public double xpReward() {
        return 0;
    }

    @Override
    public String getEmblemRequiresMessage() {
        return "Requires SkyBlock Level " + level;
    }

    @Override
    public String emblemEisplayName() {
        return "SkyBlock Level " + level;
    }

    @Override
    public boolean shouldDisplayMessage(SkyblockPlayer player) {
        return false;
    }
}
