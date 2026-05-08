package fun.ascent.skyblock.item.reforge.types;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.player.stats.Stats;

import java.util.List;
import java.util.Map;

import static fun.ascent.skyblock.player.stats.Stats.*;

public enum ArmorReforge implements Reforge {

    // ─── Non-stone reforges (wiki order) ─────────────────────────────
    Clean(Map.of(
            CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            HEALTH, new RarityStat(5, 7, 10, 15, 20, 25),
            DEFENSE, new RarityStat(5, 7, 10, 15, 20, 25)), null),

    Fierce(Map.of(
            CRITICAL_CHANCE, new RarityStat(2, 3, 4, 5, 6, 8),
            STRENGTH, new RarityStat(2, 4, 6, 8, 10, 12),
            CRITICAL_DAMAGE, new RarityStat(4, 7, 10, 14, 18, 24)), null),

    Heavy(Map.of(
            SPEED, new RarityStat(-1, -1, -1, -1, -1, -1),
            CRITICAL_DAMAGE, new RarityStat(-1, -2, -2, -3, -5, -7),
            DEFENSE, new RarityStat(25, 35, 50, 65, 80, 110)), null, "Extremely"),

    Light(Map.of(
            CRITICAL_CHANCE, new RarityStat(1, 1, 2, 2, 3, 3),
            SPEED, new RarityStat(1, 2, 3, 4, 5, 6),
            ATTACK_SPEED, new RarityStat(1, 2, 3, 4, 5, 6),
            CRITICAL_DAMAGE, new RarityStat(1, 2, 3, 4, 5, 6),
            HEALTH, new RarityStat(5, 7, 10, 15, 20, 25),
            DEFENSE, new RarityStat(1, 2, 3, 4, 5, 6)), null, "Not So"),

    Mythic(Map.of(
            INTELLIGENCE, new RarityStat(20, 25, 30, 40, 50, 60),
            CRITICAL_CHANCE, new RarityStat(1, 2, 3, 4, 5, 6),
            SPEED, new RarityStat(2, 2, 2, 2, 2, 2),
            STRENGTH, new RarityStat(2, 4, 6, 8, 10, 12),
            HEALTH, new RarityStat(2, 4, 6, 8, 10, 12),
            DEFENSE, new RarityStat(2, 3, 6, 8, 10, 12)), null),

    Pure(Map.of(
            INTELLIGENCE, new RarityStat(2, 3, 4, 6, 8, 10),
            CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            SPEED, new RarityStat(1, 1, 1, 1, 1, 1),
            STRENGTH, new RarityStat(2, 3, 4, 6, 8, 10),
            ATTACK_SPEED, new RarityStat(1, 1, 2, 3, 4, 5),
            CRITICAL_DAMAGE, new RarityStat(2, 3, 4, 6, 8, 10),
            HEALTH, new RarityStat(2, 3, 4, 6, 8, 10),
            DEFENSE, new RarityStat(2, 3, 4, 6, 8, 10)), null),

    Smart(Map.of(
            INTELLIGENCE, new RarityStat(20, 40, 60, 80, 100, 120),
            HEALTH, new RarityStat(4, 6, 9, 12, 15, 20),
            DEFENSE, new RarityStat(4, 6, 9, 12, 15, 20)), null),

    Titanic(Map.of(
            HEALTH, new RarityStat(10, 15, 20, 25, 35, 50),
            DEFENSE, new RarityStat(10, 15, 20, 25, 35, 50)), null),

    Wise(Map.of(
            INTELLIGENCE, new RarityStat(25, 50, 75, 100, 125, 150),
            SPEED, new RarityStat(1, 1, 1, 2, 2, 3),
            HEALTH, new RarityStat(6, 8, 10, 12, 15, 20)), null, "Very"),

    // ─── Reforge stone reforges (wiki order) ─────────────────────────
    Ancient(Map.of(
            INTELLIGENCE, new RarityStat(6, 9, 12, 16, 20, 25),
            CRITICAL_CHANCE, new RarityStat(3, 5, 7, 9, 12, 15),
            STRENGTH, new RarityStat(4, 8, 12, 18, 25, 35),
            HEALTH, new RarityStat(7, 7, 7, 7, 7, 7),
            DEFENSE, new RarityStat(7, 7, 7, 7, 7, 7)),
            List.of("<gray>Grants <green>+1 <blue>Crit Damage <gray>per",
                    "<red>Catacombs <gray>level."), true),

    Bustling(Map.of(
            FARMING_FORTUNE, new RarityStat(1, 2, 4, 6, 8, 10)),
            null, true),

    Calcified(Map.of(
            FISHING_SPEED, new RarityStat(1, 2, 3, 5, 7, 9),
            DEFENSE, new RarityStat(3, 5, 7, 10, 13, 16)),
            List.of("<gray>Grants <green>+25 Defense <gray>against",
                    "<aqua>Sea Creatures<gray>."), true),

    Candied(Map.of(
            HEALTH, new RarityStat(1, 2, 4, 6, 8, 10),
            DEFENSE, new RarityStat(1, 1, 2, 3, 4, 5)),
            List.of("<gray>Increases the chance to find candy during the",
                    "<gold>Spooky Festival <gray>by <green>+1%<gray>."), true),

    Cubic(Map.of(
            STRENGTH, new RarityStat(3, 5, 7, 10, 12, 15),
            HEALTH, new RarityStat(5, 7, 10, 15, 20, 25)),
            List.of("<gray>Decreases damage taken from Nether mobs by <green>2%<gray>."), true),

    Dimensional(Map.of(
            MINING_WISDOM, new RarityStat(1, 1, 1, 2, 2, 3),
            MINING_SPEED, new RarityStat(10, 15, 20, 30, 40, 50)),
            List.of("<gray>Titanium Ore grants <green>+10 Mithril Powder",
                    "<gray>when mined."), true),

    Empowered(Map.of(
            HEALTH, new RarityStat(10, 15, 20, 25, 35, 50),
            DEFENSE, new RarityStat(10, 15, 20, 25, 35, 50)),
            List.of("<gray>Grants <green>+10 Mending <gray>while in Dungeons."), true),

    Festive(Map.of(
            FISHING_SPEED, new RarityStat(2, 3, 4, 6, 8, 10),
            SEA_CREATURE_CHANCE, new RarityStat(0.05, 0.05, 0.1, 0.15, 0.2, 0.25),
            INTELLIGENCE, new RarityStat(5, 10, 15, 20, 25, 30)),
            null, true),

    Giant(Map.of(
            HEALTH, new RarityStat(50, 60, 80, 120, 180, 240)),
            null, true),

    GreaterSpook("Greater Spook",
            new ItemType[]{ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS,
                    ItemType.BELT, ItemType.CLOAK, ItemType.GLOVES, ItemType.NECKLACE, ItemType.BRACELET},
            Map.of(FEAR, new RarityStat(1, 1, 2, 3, 4, 5)),
            List.of("<gray>Grants <green>5% <gray>more <dark_purple>Fear<gray>."), true, "Greater"),

    Hyper(Map.of(
            SPEED, new RarityStat(1, 1, 2, 2, 3, 3),
            STRENGTH, new RarityStat(2, 4, 6, 7, 10, 12),
            ATTACK_SPEED, new RarityStat(2, 3, 4, 5, 6, 7)),
            List.of("<gray>Gain <green>+5 Speed <gray>for <green>5s <gray>after teleporting."), true),

    Jaded(Map.of(
            MINING_FORTUNE, new RarityStat(5, 10, 15, 20, 25, 30),
            MINING_SPEED, new RarityStat(5, 12, 20, 30, 45, 60)),
            null, true),

    Loving(null,
            new ItemType[]{ItemType.CHESTPLATE},
            Map.of(
                    INTELLIGENCE, new RarityStat(20, 40, 60, 80, 100, 120),
                    HEALTH, new RarityStat(4, 5, 6, 8, 10, 14),
                    DEFENSE, new RarityStat(4, 5, 6, 7, 10, 14)),
            List.of("<gray>Increases ability damage by <green>5%<gray>."), true, null),

    Mossy(Map.of(
            FARMING_FORTUNE, new RarityStat(5, 10, 15, 20, 25, 30),
            SPEED, new RarityStat(3, 3, 5, 5, 7, 7)),
            null, true),

    Necrotic(Map.of(
            INTELLIGENCE, new RarityStat(30, 60, 90, 120, 150, 200)),
            null, true),

    Perfect(Map.of(
            DEFENSE, new RarityStat(25, 35, 50, 65, 80, 110)),
            List.of("<gray>Increases <green>Defense <gray>by <green>+2%<gray>."), true, "Absolutely"),

    Reinforced(Map.of(
            DEFENSE, new RarityStat(25, 35, 50, 65, 80, 110)),
            null, true),

    Renowned(Map.of(
            INTELLIGENCE, new RarityStat(3, 4, 6, 8, 10, 12),
            CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            SPEED, new RarityStat(1, 1, 1, 1, 1, 1),
            STRENGTH, new RarityStat(3, 4, 6, 8, 10, 12),
            ATTACK_SPEED, new RarityStat(1, 1, 2, 3, 4, 5),
            CRITICAL_DAMAGE, new RarityStat(3, 4, 6, 8, 10, 12),
            HEALTH, new RarityStat(2, 3, 4, 6, 8, 10),
            DEFENSE, new RarityStat(2, 3, 4, 6, 8, 10)),
            List.of("<gray>Increases all <red>Combat <gray>stats and",
                    "<aqua>Magic Find <gray>by <green>+1%<gray>."), true),

    Ridiculous(null,
            new ItemType[]{ItemType.HELMET},
            Map.of(PET_LUCK, new RarityStat(1, 2, 3, 5, 8, 12)),
            List.of("<gray>Increases all <light_purple>Pet <gray>buffs by <green>10%<gray>."), true, null),

    Spiked(Map.of(
            INTELLIGENCE, new RarityStat(3, 4, 6, 8, 10, 12),
            CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            SPEED, new RarityStat(1, 1, 1, 1, 1, 1),
            STRENGTH, new RarityStat(3, 4, 6, 8, 10, 12),
            ATTACK_SPEED, new RarityStat(1, 1, 2, 3, 4, 5),
            CRITICAL_DAMAGE, new RarityStat(3, 4, 6, 8, 10, 12),
            HEALTH, new RarityStat(2, 3, 4, 6, 8, 10),
            DEFENSE, new RarityStat(2, 3, 4, 6, 8, 10)),
            null, true),

    Submerged(Map.of(
            CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            SEA_CREATURE_CHANCE, new RarityStat(0.5, 0.6, 0.7, 0.8, 0.9, 1.0),
            FISHING_SPEED, new RarityStat(1, 1, 2, 3, 4, 5)),
            null, true),

    Undead(Map.of(
            STRENGTH, new RarityStat(1, 2, 2, 3, 5, 7),
            ATTACK_SPEED, new RarityStat(1, 2, 3, 4, 5, 6),
            HEALTH, new RarityStat(6, 8, 12, 18, 25, 33),
            DEFENSE, new RarityStat(6, 8, 12, 18, 25, 33)),
            List.of("<gray>Decreases damage taken from <green>Undead",
                    "<gray>mobs by <green>2%<gray>."), true),

    GreatSpook(Map.of(
            FEAR, new RarityStat(1, 2, 3, 4, 5, 6)),
            null, true);

    private static final ItemType[] ARMOR = {
            ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS
    };

    private final String displayName;
    private final ItemType[] allowedTypes;
    private final Map<Stats, RarityStat> stats;
    private final List<String> loreText;
    private final boolean reforgeStone;
    private final String duplicatePrefix;

    ArmorReforge(Map<Stats, RarityStat> stats, List<String> loreText) {
        this(null, null, stats, loreText, false, null);
    }

    ArmorReforge(Map<Stats, RarityStat> stats, List<String> loreText, String duplicatePrefix) {
        this(null, null, stats, loreText, false, duplicatePrefix);
    }

    ArmorReforge(Map<Stats, RarityStat> stats, List<String> loreText, boolean reforgeStone) {
        this(null, null, stats, loreText, reforgeStone, null);
    }

    ArmorReforge(Map<Stats, RarityStat> stats, List<String> loreText, boolean reforgeStone, String duplicatePrefix) {
        this(null, null, stats, loreText, reforgeStone, duplicatePrefix);
    }

    ArmorReforge(String displayName, ItemType[] allowedTypes,
                 Map<Stats, RarityStat> stats, List<String> loreText,
                 boolean reforgeStone, String duplicatePrefix) {
        this.displayName = displayName;
        this.allowedTypes = allowedTypes;
        this.stats = stats;
        this.loreText = loreText;
        this.reforgeStone = reforgeStone;
        this.duplicatePrefix = duplicatePrefix;
    }

    @Override public String getName()              { return displayName != null ? displayName : name(); }
    @Override public String getId()               { return name().toUpperCase(); }
    @Override public List<String> getLoreText()   { return loreText; }
    @Override public Map<Stats, RarityStat> getStats() { return stats; }
    @Override public boolean isReforgeStone()     { return reforgeStone; }
    @Override public boolean isItemSpecific()     { return false; }
    @Override public ItemType[] getAllowedTypes()  { return allowedTypes != null ? allowedTypes : ARMOR; }
    @Override public String getDuplicatePrefix()  { return duplicatePrefix; }

    @Override
    public double getStat(Stats stat, Rarity rarity) {
        RarityStat rs = stats.get(stat);
        return rs == null ? 0 : rs.fromRarity(rarity);
    }
}

