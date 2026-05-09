package fun.ascent.skyblock.enchantment;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.SkyblockItem;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public final class EnchantingItemResolver {

    private EnchantingItemResolver() {}

    public static SkyblockItem resolve(ItemStack stack) {
        String itemId = ItemNBT.getItemId(stack);
        if (itemId != null) {
            SkyblockItem registered = ItemRegistry.getItem(itemId);
            if (registered != null) {
                return registered;
            }
            if (itemId.startsWith("vanilla_")) {
                return createVanillaTemplate(stack);
            }
        }
        return createVanillaTemplate(stack);
    }

    private static SkyblockItem createVanillaTemplate(ItemStack stack) {
        ItemType itemType = inferItemType(stack.material());
        if (itemType == ItemType.NONE) {
            return null;
        }

        String itemId = "vanilla_" + stack.material().name().toLowerCase();
        String displayName = StringUtility.toNormalCase(stack.material().name());

        return SkyblockItem.builder(itemId, stack.material(), Rarity.COMMON)
                .displayName(displayName)
                .itemType(itemType)
                .build();
    }

    private static ItemType inferItemType(Material material) {
        String name = material.name();
        if (name.endsWith("_SWORD")) return ItemType.SWORD;
        if (name.endsWith("_HELMET")) return ItemType.HELMET;
        if (name.endsWith("_CHESTPLATE")) return ItemType.CHESTPLATE;
        if (name.endsWith("_LEGGINGS")) return ItemType.LEGGINGS;
        if (name.endsWith("_BOOTS")) return ItemType.BOOTS;
        if (name.endsWith("_PICKAXE")) return ItemType.PICKAXE;
        if (name.endsWith("_SHOVEL")) return ItemType.SHOVEL;
        if (name.endsWith("_HOE")) return ItemType.HOE;
        if (name.endsWith("_AXE")) return ItemType.AXE;
        if (material == Material.BOW) return ItemType.BOW;
        if (material == Material.FISHING_ROD) return ItemType.FISHING_ROD;
        return ItemType.NONE;
    }
}
