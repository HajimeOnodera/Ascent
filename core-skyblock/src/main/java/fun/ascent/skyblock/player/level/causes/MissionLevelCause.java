package fun.ascent.skyblock.player.level.causes;

import lombok.Getter;
import fun.ascent.skyblock.player.level.abstr.SkyBlockLevelCauseAbstr;
import fun.ascent.skyblock.player.SkyblockPlayer;

@Getter
public class MissionLevelCause extends SkyBlockLevelCauseAbstr {
    private final String missionKey;

    public MissionLevelCause(String missionKey) {
        this.missionKey = missionKey;
    }

    @Override
    public double xpReward() {
        return 10;
    }

    @Override
    public boolean shouldDisplayMessage(SkyblockPlayer player) {
        return false;
    }
}
