package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.Map;

public final class CriticalEnchantment implements Enchantment {
    private static final int[] CRIT_DAMAGE = {10, 20, 30, 40, 50, 70, 100};

    @Override
    public String getDescription(int level) {
        return "Increases Crit Damage by " + CRIT_DAMAGE[level - 1] + "%.";
    }

    @Override
    public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 10, 2, 20, 3, 30, 4, 40, 5, 50);
    }

    @Override
    public List<EnchantmentCategory> getCategories() {
        return List.of(EnchantmentCategory.SWORD);
    }

    @Override
    public int getBookshelfRequirement() {
        return 4;
    }
}
