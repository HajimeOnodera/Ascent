package fun.ascent.skyblock.item.reforge.types;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.player.stats.Stats;

import java.util.List;
import java.util.Map;

public enum ArmorReforge implements Reforge {

    // ─── Non-stone reforges (wiki order) ─────────────────────────────
    Clean(Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            Stats.HEALTH, new RarityStat(5, 7, 10, 15, 20, 25),
            Stats.DEFENSE, new RarityStat(5, 7, 10, 15, 20, 25)), null),

    Fierce(Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(2, 3, 4, 5, 6, 8),
            Stats.STRENGTH, new RarityStat(2, 4, 6, 8, 10, 12),
            Stats.CRITICAL_DAMAGE, new RarityStat(4, 7, 10, 14, 18, 24)), null),

    Heavy(Map.of(
            Stats.SPEED, new RarityStat(-1, -1, -1, -1, -1, -1),
            Stats.CRITICAL_DAMAGE, new RarityStat(-1, -2, -2, -3, -5, -7),
            Stats.DEFENSE, new RarityStat(25, 35, 50, 65, 80, 110)), null, "Extremely"),

    Light(Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(1, 1, 2, 2, 3, 3),
            Stats.SPEED, new RarityStat(1, 2, 3, 4, 5, 6),
            Stats.ATTACK_SPEED, new RarityStat(1, 2, 3, 4, 5, 6),
            Stats.CRITICAL_DAMAGE, new RarityStat(1, 2, 3, 4, 5, 6),
            Stats.HEALTH, new RarityStat(5, 7, 10, 15, 20, 25),
            Stats.DEFENSE, new RarityStat(1, 2, 3, 4, 5, 6)), null, "Not So"),

    Mythic(Map.of(
            Stats.INTELLIGENCE, new RarityStat(20, 25, 30, 40, 50, 60),
            Stats.CRITICAL_CHANCE, new RarityStat(1, 2, 3, 4, 5, 6),
            Stats.SPEED, new RarityStat(2, 2, 2, 2, 2, 2),
            Stats.STRENGTH, new RarityStat(2, 4, 6, 8, 10, 12),
            Stats.HEALTH, new RarityStat(2, 4, 6, 8, 10, 12),
            Stats.DEFENSE, new RarityStat(2, 3, 6, 8, 10, 12)), null),

    Pure(Map.of(
            Stats.INTELLIGENCE, new RarityStat(2, 3, 4, 6, 8, 10),
            Stats.CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            Stats.SPEED, new RarityStat(1, 1, 1, 1, 1, 1),
            Stats.STRENGTH, new RarityStat(2, 3, 4, 6, 8, 10),
            Stats.ATTACK_SPEED, new RarityStat(1, 1, 2, 3, 4, 5),
            Stats.CRITICAL_DAMAGE, new RarityStat(2, 3, 4, 6, 8, 10),
            Stats.HEALTH, new RarityStat(2, 3, 4, 6, 8, 10),
            Stats.DEFENSE, new RarityStat(2, 3, 4, 6, 8, 10)), null),

    Smart(Map.of(
            Stats.INTELLIGENCE, new RarityStat(20, 40, 60, 80, 100, 120),
            Stats.HEALTH, new RarityStat(4, 6, 9, 12, 15, 20),
            Stats.DEFENSE, new RarityStat(4, 6, 9, 12, 15, 20)), null),

    Titanic(Map.of(
            Stats.HEALTH, new RarityStat(10, 15, 20, 25, 35, 50),
            Stats.DEFENSE, new RarityStat(10, 15, 20, 25, 35, 50)), null),

    Wise(Map.of(
            Stats.INTELLIGENCE, new RarityStat(25, 50, 75, 100, 125, 150),
            Stats.SPEED, new RarityStat(1, 1, 1, 2, 2, 3),
            Stats.HEALTH, new RarityStat(6, 8, 10, 12, 15, 20)), null, "Very"),

    // ─── Reforge stone reforges (wiki order) ─────────────────────────
    Ancient(Map.of(
            Stats.INTELLIGENCE, new RarityStat(6, 9, 12, 16, 20, 25),
            Stats.CRITICAL_CHANCE, new RarityStat(3, 5, 7, 9, 12, 15),
            Stats.STRENGTH, new RarityStat(4, 8, 12, 18, 25, 35),
            Stats.HEALTH, new RarityStat(7, 7, 7, 7, 7, 7),
            Stats.DEFENSE, new RarityStat(7, 7, 7, 7, 7, 7)),
            List.of("§7Grants §a+1 §9Crit Damage §7per",
                    "§cCatacombs §7level."), true),

    Bustling(Map.of(
            Stats.FARMING_FORTUNE, new RarityStat(1, 2, 4, 6, 8, 10)),
            null, true),

    Calcified(Map.of(
            Stats.FISHING_SPEED, new RarityStat(1, 2, 3, 5, 7, 9),
            Stats.DEFENSE, new RarityStat(3, 5, 7, 10, 13, 16)),
            List.of("§7Grants §a+25 Defense §7against",
                    "§bSea Creatures§7."), true),

    Candied(Map.of(
            Stats.HEALTH, new RarityStat(1, 2, 4, 6, 8, 10),
            Stats.DEFENSE, new RarityStat(1, 1, 2, 3, 4, 5)),
            List.of("§7Increases the chance to find candy during the",
                    "§6Spooky Festival §7by §a+1%§7."), true),

    Cubic(Map.of(
            Stats.STRENGTH, new RarityStat(3, 5, 7, 10, 12, 15),
            Stats.HEALTH, new RarityStat(5, 7, 10, 15, 20, 25)),
            List.of("§7Decreases damage taken from Nether mobs by §a2%§7."), true),

    Dimensional(Map.of(
            Stats.MINING_WISDOM, new RarityStat(1, 1, 1, 2, 2, 3),
            Stats.MINING_SPEED, new RarityStat(10, 15, 20, 30, 40, 50)),
            List.of("§7Titanium Ore grants §a+10 Mithril Powder",
                    "§7when mined."), true),

    Empowered(Map.of(
            Stats.HEALTH, new RarityStat(10, 15, 20, 25, 35, 50),
            Stats.DEFENSE, new RarityStat(10, 15, 20, 25, 35, 50)),
            List.of("§7Grants §a+10 Mending §7while in Dungeons."), true),

    Festive(Map.of(
            Stats.FISHING_SPEED, new RarityStat(2, 3, 4, 6, 8, 10),
            Stats.SEA_CREATURE_CHANCE, new RarityStat(0.05, 0.05, 0.1, 0.15, 0.2, 0.25),
            Stats.INTELLIGENCE, new RarityStat(5, 10, 15, 20, 25, 30)),
            null, true),

    Giant(Map.of(
            Stats.HEALTH, new RarityStat(50, 60, 80, 120, 180, 240)),
            null, true),

    GreaterSpook("Greater Spook",
            new ItemType[]{ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS,
                    ItemType.BELT, ItemType.CLOAK, ItemType.GLOVES, ItemType.NECKLACE, ItemType.BRACELET},
            Map.of(Stats.FEAR, new RarityStat(1, 1, 2, 3, 4, 5)),
            List.of("§7Grants §a5% §7more §5Fear§7."), true, "Greater"),

    Hyper(Map.of(
            Stats.SPEED, new RarityStat(1, 1, 2, 2, 3, 3),
            Stats.STRENGTH, new RarityStat(2, 4, 6, 7, 10, 12),
            Stats.ATTACK_SPEED, new RarityStat(2, 3, 4, 5, 6, 7)),
            List.of("§7Gain §a+5 Speed §7for §a5s §7after teleporting."), true),

    Jaded(Map.of(
            Stats.MINING_FORTUNE, new RarityStat(5, 10, 15, 20, 25, 30),
            Stats.MINING_SPEED, new RarityStat(5, 12, 20, 30, 45, 60)),
            null, true),

    Loving(null,
            new ItemType[]{ItemType.CHESTPLATE},
            Map.of(
                    Stats.INTELLIGENCE, new RarityStat(20, 40, 60, 80, 100, 120),
                    Stats.HEALTH, new RarityStat(4, 5, 6, 8, 10, 14),
                    Stats.DEFENSE, new RarityStat(4, 5, 6, 7, 10, 14)),
            List.of("§7Increases ability damage by §a5%§7."), true, null),

    Mossy(Map.of(
            Stats.FARMING_FORTUNE, new RarityStat(5, 10, 15, 20, 25, 30),
            Stats.SPEED, new RarityStat(3, 3, 5, 5, 7, 7)),
            null, true),

    Necrotic(Map.of(
            Stats.INTELLIGENCE, new RarityStat(30, 60, 90, 120, 150, 200)),
            null, true),

    Perfect(Map.of(
            Stats.DEFENSE, new RarityStat(25, 35, 50, 65, 80, 110)),
            List.of("§7Increases §aDefense §7by §a+2%§7."), true, "Absolutely"),

    Reinforced(Map.of(
            Stats.DEFENSE, new RarityStat(25, 35, 50, 65, 80, 110)),
            null, true),

    Renowned(Map.of(
            Stats.INTELLIGENCE, new RarityStat(3, 4, 6, 8, 10, 12),
            Stats.CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            Stats.SPEED, new RarityStat(1, 1, 1, 1, 1, 1),
            Stats.STRENGTH, new RarityStat(3, 4, 6, 8, 10, 12),
            Stats.ATTACK_SPEED, new RarityStat(1, 1, 2, 3, 4, 5),
            Stats.CRITICAL_DAMAGE, new RarityStat(3, 4, 6, 8, 10, 12),
            Stats.HEALTH, new RarityStat(2, 3, 4, 6, 8, 10),
            Stats.DEFENSE, new RarityStat(2, 3, 4, 6, 8, 10)),
            List.of("§7Increases all §cCombat §7stats and",
                    "§bMagic Find §7by §a+1%§7."), true),

    Ridiculous(null,
            new ItemType[]{ItemType.HELMET},
            Map.of(Stats.PET_LUCK, new RarityStat(1, 2, 3, 5, 8, 12)),
            List.of("§7Increases all §dPet §7buffs by §a10%§7."), true, null),

    Spiked(Map.of(
            Stats.INTELLIGENCE, new RarityStat(3, 4, 6, 8, 10, 12),
            Stats.CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            Stats.SPEED, new RarityStat(1, 1, 1, 1, 1, 1),
            Stats.STRENGTH, new RarityStat(3, 4, 6, 8, 10, 12),
            Stats.ATTACK_SPEED, new RarityStat(1, 1, 2, 3, 4, 5),
            Stats.CRITICAL_DAMAGE, new RarityStat(3, 4, 6, 8, 10, 12),
            Stats.HEALTH, new RarityStat(2, 3, 4, 6, 8, 10),
            Stats.DEFENSE, new RarityStat(2, 3, 4, 6, 8, 10)),
            null, true),

    Submerged(Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(2, 4, 6, 8, 10, 12),
            Stats.SEA_CREATURE_CHANCE, new RarityStat(0.5, 0.6, 0.7, 0.8, 0.9, 1.0),
            Stats.FISHING_SPEED, new RarityStat(1, 1, 2, 3, 4, 5)),
            null, true),

    Undead(Map.of(
            Stats.STRENGTH, new RarityStat(1, 2, 2, 3, 5, 7),
            Stats.ATTACK_SPEED, new RarityStat(1, 2, 3, 4, 5, 6),
            Stats.HEALTH, new RarityStat(6, 8, 12, 18, 25, 33),
            Stats.DEFENSE, new RarityStat(6, 8, 12, 18, 25, 33)),
            List.of("§7Decreases damage taken from §aUndead",
                    "§7mobs by §a2%§7."), true),

    GreatSpook(Map.of(
            Stats.FEAR, new RarityStat(1, 2, 3, 4, 5, 6)),
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
