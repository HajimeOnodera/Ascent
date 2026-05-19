package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;
import java.util.List;
import java.util.Map;

public final class CleaveEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        int pct = level * 3;
        return "Deals " + pct + "% of your damage to all monsters within 3 blocks of your target.";
    }

    @Override
    public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 10, 2, 15, 3, 20, 4, 25, 5, 30);
    }

    @Override
    public List<EnchantmentCategory> getCategories() {
        return List.of(EnchantmentCategory.SWORD);
    }

    @Override
    public int getBookshelfRequirement() {
        return 12;
    }
}
