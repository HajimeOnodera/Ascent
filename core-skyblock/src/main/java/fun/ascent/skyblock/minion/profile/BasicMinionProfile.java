package fun.ascent.skyblock.minion.profile;

import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

public record BasicMinionProfile(
        MinionType type,
        Material icon,
        Material outputMaterial,
        Block baseBlock,
        Block generatedBlock,
        Block idealLayoutBlock,
        Block secondaryGeneratedBlock,
        EntityType mobEntityType,
        String texture,
        Color armorColor,
        String placementDescription,
        String layoutHint,
        String idealLayoutText,
        int tierOneStorageSlots
) implements MinionProfile {
}
