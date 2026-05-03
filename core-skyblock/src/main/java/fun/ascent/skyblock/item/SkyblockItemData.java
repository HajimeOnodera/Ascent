package fun.ascent.skyblock.item;

import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public record SkyblockItemData(
        String id,
        String name,
        Material material,
        Rarity rarity,
        ItemType itemType,
        String skinValue,
        double npcSellPrice,
        Map<String, Double> stats,
        List<String> gemstoneSlotTypes,
        String soulbound,
        boolean glowing,
        boolean unstackable,
        String color,
        String description,
        String itemModel
) {
    public boolean hasSkin() {
        return skinValue != null && !skinValue.isEmpty();
    }

    public boolean isSoulbound() {
        return soulbound != null;
    }

    public boolean hasStats() {
        return stats != null && !stats.isEmpty();
    }
}
