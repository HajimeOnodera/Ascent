package fun.ascent.skyblock.player.level;

import net.minestom.server.item.ItemStack;
import fun.ascent.skyblock.player.SkyblockPlayer;

import java.util.List;

public abstract class SkyBlockLevelUnlock {
    public abstract UnlockType type();
    public abstract ItemStack.Builder getItemDisplay(SkyblockPlayer player, int level);
    public abstract List<String> getDisplay(SkyblockPlayer player, int level);

    public enum UnlockType {
        STATISTIC,
        CUSTOM
    }
}
