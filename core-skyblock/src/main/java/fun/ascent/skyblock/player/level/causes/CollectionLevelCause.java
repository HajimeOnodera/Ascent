package fun.ascent.skyblock.player.level.causes;

import lombok.Getter;
import fun.ascent.skyblock.player.level.abstr.CauseEmblem;
import fun.ascent.skyblock.player.level.abstr.SkyBlockLevelCauseAbstr;
import fun.ascent.skyblock.player.SkyblockPlayer;

@Getter
public class CollectionLevelCause extends SkyBlockLevelCauseAbstr implements CauseEmblem {
    private final String itemId;
    private final int level;

    public CollectionLevelCause(String itemId, int level) {
        this.itemId = itemId;
        this.level = level;
    }

    @Override
    public double xpReward() {
        return 5;
    }

    @Override
    public String getEmblemRequiresMessage() {
        return "Requires " + itemId + " Collection " + level;
    }

    @Override
    public String emblemEisplayName() {
        return itemId + " Collection " + level;
    }

    @Override
    public boolean shouldDisplayMessage(SkyblockPlayer player) {
        return false;
    }
}
