package fun.ascent.skyblock.player.level.causes;

import fun.ascent.skyblock.player.level.abstr.SkyBlockLevelCauseAbstr;
import fun.ascent.skyblock.player.SkyblockPlayer;

public class NewAccessoryLevelCause extends SkyBlockLevelCauseAbstr {
    public final String itemId;

    public NewAccessoryLevelCause(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public double xpReward() {
        return 1;
    }

    @Override
    public boolean shouldDisplayMessage(SkyblockPlayer player) {
        return true;
    }
}
