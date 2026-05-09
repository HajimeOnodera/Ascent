package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.Map;

public final class GrowthEnchantment implements Enchantment {
    private static final int[] HEALTH_BONUS = {15, 30, 45, 60, 75, 90, 105};

    @Override
    public String getDescription(int level) {
        return "Grants +" + HEALTH_BONUS[level - 1] + " Health.";
    }

    @Override
    public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 10, 2, 20, 3, 30, 4, 40, 5, 50);
    }

    @Override
    public List<EnchantmentCategory> getCategories() {
        return List.of(EnchantmentCategory.ARMOR);
    }

    @Override
    public int getBookshelfRequirement() {
        return 8;
    }
}
