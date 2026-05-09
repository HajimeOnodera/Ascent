package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.Map;

public final class FortuneEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        return "Grants +" + (level * 10) + " Mining Fortune.";
    }

    @Override
    public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 10, 2, 20, 3, 30, 4, 40, 5, 50);
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
