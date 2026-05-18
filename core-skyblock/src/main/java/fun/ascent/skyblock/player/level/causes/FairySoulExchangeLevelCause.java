package fun.ascent.skyblock.player.level.causes;

import lombok.Getter;
import fun.ascent.skyblock.player.level.abstr.SkyBlockLevelCauseAbstr;
import fun.ascent.skyblock.player.SkyblockPlayer;

@Getter
public class FairySoulExchangeLevelCause extends SkyBlockLevelCauseAbstr {
    private final int exchangeCount;

    public FairySoulExchangeLevelCause(int exchangeCount) {
        this.exchangeCount = exchangeCount;
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
