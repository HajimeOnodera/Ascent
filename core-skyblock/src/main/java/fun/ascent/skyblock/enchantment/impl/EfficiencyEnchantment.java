package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.Map;

public final class EfficiencyEnchantment implements Enchantment {
    private static final int[] MINING_SPEED = {10, 20, 30, 40, 50, 60, 70};

    @Override
    public String getDescription(int level) {
        return "Grants +" + MINING_SPEED[level - 1] + " Mining Speed.";
    }

    @Override
    public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 10, 2, 15, 3, 20, 4, 25, 5, 30);
    }

    @Override
    public List<EnchantmentCategory> getCategories() {
        return List.of(EnchantmentCategory.TOOL);
    }

    @Override
    public int getBookshelfRequirement() {
        return 0;
    }
}
