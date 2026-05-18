package fun.ascent.skyblock.player.level.causes;

import lombok.Getter;
import fun.ascent.skyblock.player.level.abstr.SkyBlockLevelCauseAbstr;
import fun.ascent.skyblock.player.SkyblockPlayer;

@Getter
public class MuseumLevelCause extends SkyBlockLevelCauseAbstr {
    private final String rewardName;

    public MuseumLevelCause(String rewardName) {
        this.rewardName = rewardName;
    }

    @Override
    public double xpReward() {
        return 15;
    }

    @Override
    public boolean shouldDisplayMessage(SkyblockPlayer player) {
        return false;
    }
}
