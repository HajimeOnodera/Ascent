package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;
import java.util.List;
import java.util.Map;

public final class TripleStrikeEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        int pct = level * 10;
        return "Increases damage dealt by " + pct + "% for the first three hits on a mob.";
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
        return 18;
    }
}
