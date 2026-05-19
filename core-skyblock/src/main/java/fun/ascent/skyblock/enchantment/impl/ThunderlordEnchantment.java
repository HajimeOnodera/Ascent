package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;
import java.util.List;
import java.util.Map;

public final class ThunderlordEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        int dmg = level * 100;
        return "Strikes lightning every 3 consecutive hits on the same monster, dealing " + dmg + " magic damage.";
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
        return 15;
    }
}
