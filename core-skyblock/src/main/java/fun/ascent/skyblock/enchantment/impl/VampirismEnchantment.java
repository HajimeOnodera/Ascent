package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;
import java.util.List;
import java.util.Map;

public final class VampirismEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        return "Heals you for " + level + "% of your missing health upon killing an enemy.";
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
        return 14;
    }
}
