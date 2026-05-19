package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;
import java.util.List;
import java.util.Map;

public final class EnderSlayerEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        int bonus = level * 12;
        return "Increases melee damage dealt to Endermen, Endermites, and Ender Dragons by " + bonus + "%.";
    }

    @Override
    public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 15, 2, 20, 3, 25, 4, 30, 5, 35);
    }

    @Override
    public List<EnchantmentCategory> getCategories() {
        return List.of(EnchantmentCategory.SWORD);
    }

    @Override
    public int getBookshelfRequirement() {
        return 15;
    }
}
