package fun.ascent.skyblock.player.level.unlocks;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.level.CustomLevelAward;
import fun.ascent.skyblock.player.level.SkyBlockLevelUnlock;
import fun.ascent.skyblock.player.SkyblockPlayer;

import java.util.List;

@Getter
public class CustomLevelUnlock extends SkyBlockLevelUnlock {
    private final CustomLevelAward award;
    public CustomLevelUnlock(CustomLevelAward award) {
        this.award = award;
    }

    @Override
    public UnlockType type() {
        return UnlockType.CUSTOM;
    }

    @Override
    public ItemStack.Builder getItemDisplay(SkyblockPlayer player, int level) {
        return ItemStackCreator.getStack(award.getDisplay(), Material.GOLDEN_APPLE, 1,
                "§8Level " + level);
    }

    @Override
    public List<String> getDisplay(SkyblockPlayer player, int level) {
        return List.of(award.getDisplay());
    }
}
