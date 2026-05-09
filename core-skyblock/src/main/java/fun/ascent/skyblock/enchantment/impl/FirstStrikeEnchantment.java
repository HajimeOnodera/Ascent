package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.Map;

public final class FirstStrikeEnchantment implements Enchantment {
    private static final int[] DAMAGE_BONUS = {25, 50, 75, 100, 125};

    @Override
    public String getDescription(int level) {
        return "Deal +" + DAMAGE_BONUS[level - 1] + "% damage to mobs that have not taken damage yet.";
    }

    @Override
    public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 15, 2, 24, 3, 33, 4, 42, 5, 51);
    }

    @Override
    public List<EnchantmentCategory> getCategories() {
        return List.of(EnchantmentCategory.SWORD);
    }

    @Override
    public int getBookshelfRequirement() {
        return 10;
    }
}
