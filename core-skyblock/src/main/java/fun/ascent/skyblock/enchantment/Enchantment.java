package fun.ascent.skyblock.enchantment;

import java.util.List;
import java.util.Map;

public interface Enchantment {
    String getDescription(int level);
    Map<Integer, Integer> getLevelCosts();
    List<EnchantmentCategory> getCategories();
    int getBookshelfRequirement();

    default int getMinLevel() {
        return getLevelCosts().keySet().stream().min(Integer::compare).orElse(1);
    }

    default int getMaxLevel() {
        return getLevelCosts().keySet().stream().max(Integer::compare).orElse(1);
    }
}