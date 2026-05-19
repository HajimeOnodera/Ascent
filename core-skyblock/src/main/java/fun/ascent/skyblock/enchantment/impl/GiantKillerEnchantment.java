package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;
import java.util.List;
import java.util.Map;

public final class GiantKillerEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        double mult = level * 0.1;
        double cap = level * 5.0;
        if (level == 5) { mult = 0.6; cap = 30.0; }
        else if (level == 6) { mult = 0.9; cap = 45.0; }
        else if (level == 7) { mult = 1.2; cap = 65.0; }
        return "Increases damage by " + mult + "% for each percent of extra health target has above you, up to " + cap + "%.";
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
        return 8;
    }
}
