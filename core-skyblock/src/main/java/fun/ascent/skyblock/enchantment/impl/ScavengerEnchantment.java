package fun.ascent.skyblock.enchantment.impl;

import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentCategory;
import java.util.List;
import java.util.Map;

public final class ScavengerEnchantment implements Enchantment {
    @Override public String getDescription(int level) {
        return "Gain §a" + (level * 0.3) + " coins§7 per combat level of the slain mob.";
    }
    @Override public Map<Integer, Integer> getLevelCosts() {
        return Map.of(1, 12, 2, 22, 3, 32, 4, 42, 5, 52);
    }
    @Override public List<EnchantmentCategory> getCategories() { return List.of(EnchantmentCategory.SWORD); }
    @Override public int getBookshelfRequirement() { return 6; }
}
