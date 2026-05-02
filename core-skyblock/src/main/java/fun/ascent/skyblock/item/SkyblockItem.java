package fun.ascent.skyblock.item;

import fun.ascent.skyblock.item.gemstone.GemstoneSlot;
import fun.ascent.skyblock.item.gemstone.GemstoneStatTable;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;


import java.util.*;

public class SkyblockItem {

    public static final String ITEM_ID_TAG = "item_id";

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private static final Stats[] LORE_STAT_ORDER = {
            Stats.DAMAGE, Stats.HEALTH, Stats.DEFENSE, Stats.TRUE_DEFENSE,
            Stats.STRENGTH, Stats.INTELLIGENCE, Stats.CRIT_CHANCE, Stats.CRIT_DAMAGE,
            Stats.BONUS_ATTACK_SPEED, Stats.ABILITY_DAMAGE, Stats.FEROCITY, Stats.SPEED,
            Stats.HEALTH_REGEN, Stats.VITALITY, Stats.MENDING, Stats.SWING_RANGE,
            Stats.MAGIC_FIND, Stats.PET_LUCK, Stats.SEA_CREATURE_CHANCE,
            Stats.DOUBLE_HOOK_CHANCE, Stats.FISHING_SPEED, Stats.TROPHY_FISH_CHANCE,
            Stats.TREASURE_CHANCE, Stats.BREAKING_POWER, Stats.MINING_SPEED,
            Stats.MINING_FORTUNE, Stats.ORE_FORTUNE, Stats.BLOCK_FORTUNE,
            Stats.GEMSTONE_FORTUNE, Stats.DWARVEN_METAL_FORTUNE, Stats.PRISTINE,
            Stats.FARMING_FORTUNE, Stats.BONUS_PEST_CHANCE, Stats.FORAGING_FORTUNE,
            Stats.COLD_RESISTANCE, Stats.HEAT_RESISTANCE, Stats.RESPIRATION,
    };

    // Shot cooldown thresholds for shortbows based on attack speed
    private static String getShortbowCooldown(double attackSpeed) {
        if (attackSpeed >= 100) return "0.25s";
        if (attackSpeed >= 67) return "0.3s";
        if (attackSpeed >= 43) return "0.35s";
        if (attackSpeed >= 25) return "0.4s";
        if (attackSpeed >= 12) return "0.45s";
        return "0.5s";
    }

    private final String itemId;
    private final String displayName;
    private final Material material;
    private final ItemType itemType;
    private final Rarity rarity;
    private final Map<Stats, Double> baseStats;
    private final List<String> description;
    private final List<ItemAbility> abilities;
    private final boolean enchantable;
    private final boolean reforgeable;
    private final boolean glowing;
    private final String skinValue;
    private final List<GemstoneSlot> gemstoneSlots;
    private final boolean recombobulated;

    // Lore fields
    private final Map<String, Integer> enchantments;
    private final String ultimateEnchant;
    private final int ultimateEnchantLevel;
    private final String runeType;
    private final int runeLevel;
    private final boolean consumable;
    private final boolean dungeon;
    private final boolean soulbound;
    private final boolean coopSoulbound;
    private final String dyeName;
    private final boolean bookOfStats;
    private final int kills;

    private SkyblockItem(Builder builder) {
        this.itemId = builder.itemId;
        this.displayName = builder.displayName;
        this.material = builder.material;
        this.itemType = builder.itemType;
        this.rarity = builder.rarity;
        this.baseStats = Collections.unmodifiableMap(new EnumMap<>(builder.baseStats));
        this.description = List.copyOf(builder.description);
        this.abilities = List.copyOf(builder.abilities);
        this.enchantable = builder.enchantable;
        this.reforgeable = builder.reforgeable;
        this.glowing = builder.glowing;
        this.skinValue = builder.skinValue;
        this.gemstoneSlots = List.copyOf(builder.gemstoneSlots);
        this.recombobulated = builder.recombobulated;
        this.enchantments = Collections.unmodifiableMap(new LinkedHashMap<>(builder.enchantments));
        this.ultimateEnchant = builder.ultimateEnchant;
        this.ultimateEnchantLevel = builder.ultimateEnchantLevel;
        this.runeType = builder.runeType;
        this.runeLevel = builder.runeLevel;
        this.consumable = builder.consumable;
        this.dungeon = builder.dungeon;
        this.soulbound = builder.soulbound;
        this.coopSoulbound = builder.coopSoulbound;
        this.dyeName = builder.dyeName;
        this.bookOfStats = builder.bookOfStats;
        this.kills = builder.kills;
    }

    public ItemStack buildItemStack() {
        return buildItemStack(null);
    }

    public ItemStack buildItemStack(Player player) {
        Rarity effectiveRarity = recombobulated && rarity.getNextRarity() != null
                ? rarity.getNextRarity() : rarity;

        // Display name
//        String namePrefix = reforge != null ? reforge.getName() + " " : "";
//        Component displayComponent = LEGACY.deserialize(effectiveRarity.getColorCode() + namePrefix + displayName)
//                .decoration(TextDecoration.ITALIC, false);

        List<Component> loreComponents = new ArrayList<>();
        for (String line : buildLoreStrings(effectiveRarity, player)) {
            loreComponents.add(LEGACY.deserialize(line).decoration(TextDecoration.ITALIC, false));
        }

        ItemStack item = ItemStack.of(material)
    }
}

