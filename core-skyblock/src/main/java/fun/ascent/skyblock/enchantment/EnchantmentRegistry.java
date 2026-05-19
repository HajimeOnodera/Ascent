package fun.ascent.skyblock.enchantment;

import fun.ascent.skyblock.enchantment.impl.*;
import fun.ascent.skyblock.item.ItemType;
import lombok.Getter;
import java.util.Arrays;
import java.util.List;

@Getter
public enum EnchantmentRegistry {
    SHARPNESS("Sharpness", new SharpnessEnchantment()),
    CRITICAL("Critical", new CriticalEnchantment()),
    LIFE_STEAL("Life Steal", new LifeStealEnchantment()),
    FIRST_STRIKE("First Strike", new FirstStrikeEnchantment()),
    SCAVENGER("Scavenger", new ScavengerEnchantment()),
    GROWTH("Growth", new GrowthEnchantment()),
    PROTECTION("Protection", new ProtectionEnchantment()),
    FORTUNE("Fortune", new FortuneEnchantment()),
    EFFICIENCY("Efficiency", new EfficiencyEnchantment()),
    GIANT_KILLER("Giant Killer", new GiantKillerEnchantment()),
    EXECUTE("Execute", new ExecuteEnchantment()),
    PROSECUTE("Prosecute", new ProsecuteEnchantment()),
    LETHALITY("Lethality", new LethalityEnchantment()),
    SMITE("Smite", new SmiteEnchantment()),
    ENDER_SLAYER("Ender Slayer", new EnderSlayerEnchantment()),
    CUBISM("Cubism", new CubismEnchantment()),
    SYPHON("Syphon", new SyphonEnchantment()),
    VAMPIRISM("Vampirism", new VampirismEnchantment()),
    TRIPLE_STRIKE("Triple-Strike", new TripleStrikeEnchantment()),
    THUNDERLORD("Thunderlord", new ThunderlordEnchantment()),
    CLEAVE("Cleave", new CleaveEnchantment());

    private final String displayName;
    private final Enchantment enchantment;

    EnchantmentRegistry(String displayName, Enchantment enchantment) {
        this.displayName = displayName;
        this.enchantment = enchantment;
    }

    public String getKey() {
        return name().toLowerCase();
    }

    public static List<EnchantmentRegistry> forItemType(ItemType type) {
        List<EnchantmentCategory> categories = EnchantmentCategory.forItemType(type);
        return Arrays.stream(values())
                .filter(reg -> reg.enchantment.getCategories().stream().anyMatch(categories::contains))
                .toList();
    }

    public static EnchantmentRegistry fromKey(String key) {
        for (EnchantmentRegistry reg : values()) {
            if (reg.getKey().equals(key)) return reg;
        }
        return null;
    }
}
