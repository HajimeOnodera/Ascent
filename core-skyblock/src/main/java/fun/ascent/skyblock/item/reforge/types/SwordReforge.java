package fun.ascent.skyblock.item.reforge.types;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.player.stats.Stats;

import java.util.List;
import java.util.Map;

public enum SwordReforge implements Reforge {

    Fair(Map.of(
            Stats.STRENGTH, new RarityStat(2, 3, 4, 7, 10, 12),
            Stats.CRITICAL_DAMAGE, new RarityStat(2, 3, 4, 7, 10, 12),
            Stats.CRITICAL_CHANCE, new RarityStat(2, 3, 4, 7, 10, 12),
            Stats.ATTACK_SPEED, new RarityStat(2, 3, 4, 7, 10, 12),
            Stats.INTELLIGENCE, new RarityStat(2, 3, 4, 7, 10, 12)), null, true),

    Epic(Map.of(
            Stats.STRENGTH, new RarityStat(15, 20, 25, 32, 40, 50),
            Stats.CRITICAL_DAMAGE, new RarityStat(10, 15, 20, 27, 35, 45),
            Stats.ATTACK_SPEED, new RarityStat(1, 2, 4, 7, 10, 15)), null, true),

    Fast(Map.of(
            Stats.ATTACK_SPEED, new RarityStat(10, 20, 30, 40, 50, 60)), null, true),

    Gentle(Map.of(
            Stats.ATTACK_SPEED, new RarityStat(8, 10, 15, 20, 25, 30),
            Stats.STRENGTH, new RarityStat(3, 5, 7, 10, 15, 20)), null, true),

    Heroic(Map.of(
            Stats.ATTACK_SPEED, new RarityStat(1, 2, 2, 3, 5, 7),
            Stats.INTELLIGENCE, new RarityStat(40, 50, 65, 80, 100, 125),
            Stats.STRENGTH, new RarityStat(15, 20, 25, 32, 40, 50)), null, true),

    Legendary(Map.of(
            Stats.ATTACK_SPEED, new RarityStat(2, 3, 5, 7, 10, 15),
            Stats.STRENGTH, new RarityStat(3, 7, 12, 18, 25, 32),
            Stats.CRITICAL_CHANCE, new RarityStat(5, 7, 9, 12, 15, 18),
            Stats.CRITICAL_DAMAGE, new RarityStat(5, 10, 15, 22, 28, 36),
            Stats.INTELLIGENCE, new RarityStat(5, 8, 12, 18, 25, 35)), null, true),

    Odd(Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(10, 12, 15, 20, 25, 30),
            Stats.CRITICAL_DAMAGE, new RarityStat(5, 10, 15, 22, 30, 40),
            Stats.INTELLIGENCE, new RarityStat(-5, -10, -18, -32, -50, -75)), null, true),

    Rich(Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(1, 2, 4, 7, 10, 10),
            Stats.CRITICAL_DAMAGE, new RarityStat(1, 2, 4, 7, 10, 10),
            Stats.INTELLIGENCE, new RarityStat(3, 6, 10, 15, 20, 20)), null, true),

    Sharp(Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(10, 12, 14, 17, 20, 25),
            Stats.CRITICAL_DAMAGE, new RarityStat(20, 30, 40, 55, 75, 90)), null, true),

    Spicy(Map.of(
            Stats.ATTACK_SPEED, new RarityStat(2, 3, 5, 7, 10, 15),
            Stats.STRENGTH, new RarityStat(2, 3, 4, 7, 10, 12),
            Stats.CRITICAL_CHANCE, new RarityStat(1),
            Stats.CRITICAL_DAMAGE, new RarityStat(25, 35, 45, 60, 80, 100)), null, true),

    Dirty(Map.of(
            Stats.ATTACK_SPEED, new RarityStat(2, 3, 5, 10, 15, 20),
            Stats.STRENGTH, new RarityStat(2, 4, 6, 10, 12, 15),
            Stats.FEROCITY, new RarityStat(2, 3, 6, 9, 12, 15)), null, true, true, false),

    Warped(Map.of(
            Stats.DAMAGE, new RarityStat(165),
            Stats.STRENGTH, new RarityStat(165),
            Stats.INTELLIGENCE, new RarityStat(0, 0, 65, 100, 165, 0)), null, true, true, true),

    Bulky(Map.of(
            Stats.HEALTH, new RarityStat(4, 6, 9, 12, 15, 20),
            Stats.DEFENSE, new RarityStat(2, 3, 5, 8, 13, 21)), null, false, true, false),

    Coldfused(Map.of(
            Stats.MAGIC_FIND, new RarityStat(2),
            Stats.STRENGTH, new RarityStat(15, 20, 25, 35, 45, 55),
            Stats.CRITICAL_DAMAGE, new RarityStat(20, 30, 40, 50, 60, 70)),
            List.of("§8Only if Wisp is equipped",
                    "§c+75❁ Strength",
                    "§9+55☠ Crit Damage",
                    "§7Deal §62x §7to fire pillars, breaking one grants",
                    "§f30❂ True Defense §7and §c+1.15x damage §7for §a60s§7."), false, true, false),

    Fabled(Map.of(
            Stats.STRENGTH, new RarityStat(30, 35, 40, 50, 60, 75),
            Stats.CRITICAL_DAMAGE, new RarityStat(15, 20, 25, 32, 40, 50)),
            List.of("§7Critical hits have a chance to deal",
                    "§7up to §a+15%§7 extra damage."), false, true, false),

    Withered(Map.of(
            Stats.STRENGTH, new RarityStat(60, 75, 90, 110, 135, 170)),
            List.of("§7Grants §a+1 §c❁ Strength §7per",
                    "§cCatacombs §7level."), false, true, false),

    Fanged(Map.of(
            Stats.STRENGTH, new RarityStat(30, 35, 40, 50, 60, 65),
            Stats.VITALITY, new RarityStat(2, 3, 4, 6, 8, 10),
            Stats.ATTACK_SPEED, new RarityStat(2, 3, 4, 6, 9, 10),
            Stats.CRITICAL_CHANCE, new RarityStat(3, 4, 5, 7, 8, 10)),
            List.of("§7Every §c7th §7melee hit on an enemy",
                    "deals §c+100% §7damage."), true, true, false),

    Gilded(Map.of(
            Stats.STRENGTH, new RarityStat(75, 75, 75, 75, 75, 90),
            Stats.DAMAGE, new RarityStat(75, 75, 75, 75, 75, 90),
            Stats.ABILITY_DAMAGE, new RarityStat(8, 8, 8, 8, 8, 10)),
            List.of("§7Upon killing an enemy, you have a",
                    "§7rare chance to grant coins to a",
                    "§7player around you."), false, true, true),

    Jerrys("Jerry's", Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(10),
            Stats.STRENGTH, new RarityStat(25),
            Stats.CRITICAL_DAMAGE, new RarityStat(50)),
            List.of("§7Consumes all your mana and adds 10%",
                    "§7 of that amount as damage on your",
                    "§7next AotJ hit."), false, true, true),

    Suspicious(Map.of(
            Stats.CRITICAL_CHANCE, new RarityStat(1, 2, 3, 5, 7, 10),
            Stats.CRITICAL_DAMAGE, new RarityStat(30, 40, 50, 65, 85, 110)),
            List.of("§7Increases weapon damage by §c+15§7."), false, true, false);


    private final String displayName;
    private final Map<Stats, RarityStat> stats;
    private final List<String> loreText;
    private final boolean includeFishingRod;
    private final boolean reforgeStone;
    private final boolean itemSpecific;

    SwordReforge(Map<Stats, RarityStat> stats, List<String> loreText, boolean includeFishingRod) {
        this(null, stats, loreText, includeFishingRod, false, false);
    }

    SwordReforge(Map<Stats, RarityStat> stats, List<String> loreText, boolean includeFishingRod, boolean reforgeStone, boolean itemSpecific) {
        this(null, stats, loreText, includeFishingRod, reforgeStone, itemSpecific);
    }

    SwordReforge(String displayName, Map<Stats, RarityStat> stats, List<String> loreText, boolean includeFishingRod, boolean reforgeStone, boolean itemSpecific) {
        this.displayName = displayName;
        this.stats = stats;
        this.loreText = loreText;
        this.includeFishingRod = includeFishingRod;
        this.reforgeStone = reforgeStone;
        this.itemSpecific = itemSpecific;
    }

    @Override public String getName() { return displayName != null ? displayName : name(); }
    @Override public String getId() { return name().toUpperCase(); }
    @Override public List<String> getLoreText() { return loreText; }
    @Override public Map<Stats, RarityStat> getStats() { return stats; }
    @Override public boolean isReforgeStone() { return reforgeStone; }
    @Override public boolean isItemSpecific() { return itemSpecific; }

    @Override
    public double getStat(Stats stat, Rarity rarity) {
        RarityStat rs = stats.get(stat);
        return rs == null ? 0 : rs.fromRarity(rarity);
    }

    @Override
    public ItemType[] getAllowedTypes() {
        if (includeFishingRod) {
            return new ItemType[]{ItemType.SWORD, ItemType.LONGSWORD, ItemType.FISHING_ROD};
        }
        return new ItemType[]{ItemType.SWORD, ItemType.LONGSWORD};
    }
}
