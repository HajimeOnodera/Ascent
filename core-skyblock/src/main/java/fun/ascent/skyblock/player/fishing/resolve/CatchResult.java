package fun.ascent.skyblock.player.fishing.resolve;

import net.minestom.server.item.ItemStack;

public record CatchResult(
        CatchCategory category,
        ItemStack rewardStack,
        double fishingXp,
        String displayName
) {}
