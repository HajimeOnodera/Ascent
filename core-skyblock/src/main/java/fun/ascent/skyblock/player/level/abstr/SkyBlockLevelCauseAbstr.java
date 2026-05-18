package fun.ascent.skyblock.player.level.abstr;

import fun.ascent.skyblock.player.SkyblockPlayer;

public abstract class SkyBlockLevelCauseAbstr {
    public abstract double xpReward();
    public abstract boolean shouldDisplayMessage(SkyblockPlayer player);
}
