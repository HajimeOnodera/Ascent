package fun.ascent.skyblock.minion.profile;

import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public interface MinionProfile {
    MinionType type();

    Material icon();

    Material outputMaterial();

    Block baseBlock();

    Block generatedBlock();

    Block idealLayoutBlock();

    Block secondaryGeneratedBlock();

    EntityType mobEntityType();

    String texture();

    Color armorColor();

    String placementDescription();

    String layoutHint();

    String idealLayoutText();

    int tierOneStorageSlots();

    default List<ItemStack> createHarvestDrops() {
        return switch (type()) {
            case WHEAT -> List.of(
                    fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.WHEAT, 2),
                    fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.WHEAT_SEEDS, 1)
            );
            case MELON -> List.of(fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.MELON_SLICE, 3));
            case PUMPKIN -> List.of(fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.PUMPKIN, 1));
            case CARROT -> List.of(fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.CARROT, 2));
            case POTATO -> List.of(fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.POTATO, 2));
            case SUGARCANE -> List.of(fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.SUGAR_CANE, 2));
            case CACTUS -> List.of(fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.CACTUS, 1));
            case COCOA -> List.of(fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(Material.COCOA_BEANS, 3));
            default -> List.of(fun.ascent.skyblock.item.ItemRegistry.createSkyblockOrVanillaStack(outputMaterial(), 1));
        };
    }

    default Material heldTool(int tier) {
        return switch (type().getActionKind()) {
            case MINING -> {
                if (tier >= 11) {
                    yield Material.DIAMOND_PICKAXE;
                }
                if (tier >= 9) {
                    yield Material.GOLDEN_PICKAXE;
                }
                if (tier >= 6) {
                    yield Material.IRON_PICKAXE;
                }
                if (tier >= 3) {
                    yield Material.STONE_PICKAXE;
                }
                yield Material.WOODEN_PICKAXE;
            }
            case CROP -> {
                if (tier >= 11) {
                    yield Material.DIAMOND_HOE;
                }
                if (tier >= 9) {
                    yield Material.GOLDEN_HOE;
                }
                if (tier >= 6) {
                    yield Material.IRON_HOE;
                }
                if (tier >= 3) {
                    yield Material.STONE_HOE;
                }
                yield Material.WOODEN_HOE;
            }
            case TREE -> {
                if (tier >= 11) {
                    yield Material.DIAMOND_AXE;
                }
                if (tier >= 9) {
                    yield Material.GOLDEN_AXE;
                }
                if (tier >= 6) {
                    yield Material.IRON_AXE;
                }
                if (tier >= 3) {
                    yield Material.STONE_AXE;
                }
                yield Material.WOODEN_AXE;
            }
            case MOB -> {
                if (tier >= 11) {
                    yield Material.DIAMOND_SWORD;
                }
                if (tier >= 9) {
                    yield Material.GOLDEN_SWORD;
                }
                if (tier >= 6) {
                    yield Material.IRON_SWORD;
                }
                if (tier >= 3) {
                    yield Material.STONE_SWORD;
                }
                yield Material.WOODEN_SWORD;
            }
            case FISHING -> Material.FISHING_ROD;
        };
    }

    default Material upgradeMaterial() {
        return switch (type()) {
            case MELON, PUMPKIN -> icon();
            case FISHING -> outputMaterial();
            default -> icon();
        };
    }
}
