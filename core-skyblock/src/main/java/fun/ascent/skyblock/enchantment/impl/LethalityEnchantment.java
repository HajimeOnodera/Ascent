package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;
import java.util.List;
import java.util.Map;

public final class LethalityEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        double reduction = level * 3.0;
        return "Reduces the defense of your target by " + reduction + "% for 4 seconds on hit. Stacks up to 4 times.";
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
        return 6;
    }
}
