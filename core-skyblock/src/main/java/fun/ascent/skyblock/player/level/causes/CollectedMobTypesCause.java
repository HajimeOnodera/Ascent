package fun.ascent.skyblock.player.level.causes;

import fun.ascent.skyblock.player.level.abstr.SkyBlockLevelCauseAbstr;
import fun.ascent.skyblock.player.SkyblockPlayer;

public class CollectedMobTypesCause extends SkyBlockLevelCauseAbstr {

    public CollectedMobTypesCause(String mobType) {
    }

    @Override
    public double xpReward() {
        return 1;
    }

    @Override
    public boolean shouldDisplayMessage(SkyblockPlayer player) {
        return false;
    }
}
