package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.Map;

public final class ProtectionEnchantment implements Enchantment {
    @Override
    public String getDescription(int level) {
        return "Grants +" + (level * 4) + " Defense.";
    }

    @Override
    public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 10, 2, 15, 3, 20, 4, 25, 5, 30);
    }

    @Override
    public List<EnchantmentCategory> getCategories() {
        return List.of(EnchantmentCategory.ARMOR);
    }

    @Override
    public int getBookshelfRequirement() {
        return 0;
    }
}
