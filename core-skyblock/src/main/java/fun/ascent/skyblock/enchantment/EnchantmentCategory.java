package fun.ascent.skyblock.enchantment;

import fun.ascent.skyblock.item.ItemType;
import java.util.Arrays;
import java.util.List;

public enum EnchantmentCategory {
    SWORD(ItemType.SWORD, ItemType.LONGSWORD, ItemType.FISHING_WEAPON, ItemType.GAUNTLET),
    ARMOR(ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS),
    TOOL(ItemType.PICKAXE, ItemType.DRILL, ItemType.SHOVEL, ItemType.HOE),
    FISHING_ROD(ItemType.FISHING_ROD),
    BOW(ItemType.BOW);

    private final List<ItemType> types;

    EnchantmentCategory(ItemType... types) {
        this.types = Arrays.asList(types);
    }

    public boolean matches(ItemType type) {
        return types.contains(type);
    }

    public static List<EnchantmentCategory> forItemType(ItemType type) {
        return Arrays.stream(values()).filter(cat -> cat.matches(type)).toList();
    }
}