package fun.ascent.skyblock.item.reforge.types;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.player.stats.Stats;

import java.util.List;
import java.util.Map;

import static fun.ascent.skyblock.player.stats.Stats.*;

public enum SwordReforge implements Reforge {

    Fair(Map.of(
            STRENGTH, new RarityStat(2, 3, 4, 7, 10, 12),
            CRITICAL_DAMAGE, new RarityStat(2, 3, 4, 7, 10, 12),
            CRITICAL_CHANCE, new RarityStat(2, 3, 4, 7, 10, 12),
            ATTACK_SPEED, new RarityStat(2, 3, 4, 7, 10, 12),
            INTELLIGENCE, new RarityStat(2, 3, 4, 7, 10, 12)), null, true),

    Epic(Map.of(
            STRENGTH, new RarityStat(15, 20, 25, 32, 40, 50),
            CRITICAL_DAMAGE, new RarityStat(10, 15, 20, 27, 35, 45),
            ATTACK_SPEED, new RarityStat(1, 2, 4, 7, 10, 15)), null, true),

    Fast(Map.of(
            ATTACK_SPEED, new RarityStat(10, 20, 30, 40, 50, 60)), null, true),

    Gentle(Map.of(
            ATTACK_SPEED, new RarityStat(8, 10, 15, 20, 25, 30),
            STRENGTH, new RarityStat(3, 5, 7, 10, 15, 20)), null, true),

    Heroic(Map.of(
            ATTACK_SPEED, new RarityStat(1, 2, 2, 3, 5, 7),
            INTELLIGENCE, new RarityStat(40, 50, 65, 80, 100, 125),
            STRENGTH, new RarityStat(15, 20, 25, 32, 40, 50)), null, true),

    Legendary(Map.of(
            ATTACK_SPEED, new RarityStat(2, 3, 5, 7, 10, 15),
            STRENGTH, new RarityStat(3, 7, 12, 18, 25, 32),
            CRITICAL_CHANCE, new RarityStat(5, 7, 9, 12, 15, 18),
            CRITICAL_DAMAGE, new RarityStat(5, 10, 15, 22, 28, 36),
            INTELLIGENCE, new RarityStat(5, 8, 12, 18, 25, 35)), null, true),

    Odd(Map.of(
            CRITICAL_CHANCE, new RarityStat(10, 12, 15, 20, 25, 30),
            CRITICAL_DAMAGE, new RarityStat(5, 10, 15, 22, 30, 40),
            INTELLIGENCE, new RarityStat(-5, -10, -18, -32, -50, -75)), null, true),

    Rich(Map.of(
            CRITICAL_CHANCE, new RarityStat(1, 2, 4, 7, 10, 10),
            CRITICAL_DAMAGE, new RarityStat(1, 2, 4, 7, 10, 10),
            INTELLIGENCE, new RarityStat(3, 6, 10, 15, 20, 20)), null, true),

    Sharp(Map.of(
            CRITICAL_CHANCE, new RarityStat(10, 12, 14, 17, 20, 25),
            CRITICAL_DAMAGE, new RarityStat(20, 30, 40, 55, 75, 90)), null, true),

    Spicy(Map.of(
            ATTACK_SPEED, new RarityStat(2, 3, 5, 7, 10, 15),
            STRENGTH, new RarityStat(2, 3, 4, 7, 10, 12),
            CRITICAL_CHANCE, new RarityStat(1),
            CRITICAL_DAMAGE, new RarityStat(25, 35, 45, 60, 80, 100)), null, true),

    Dirty(Map.of(
            ATTACK_SPEED, new RarityStat(2, 3, 5, 10, 15, 20),
            STRENGTH, new RarityStat(2, 4, 6, 10, 12, 15),
            FEROCITY, new RarityStat(2, 3, 6, 9, 12, 15)), null, true, true, false),

    Warped(Map.of(
            DAMAGE, new RarityStat(165),
            STRENGTH, new RarityStat(165),
            INTELLIGENCE, new RarityStat(0, 0, 65, 100, 165, 0)), null, true, true, true),

    Bulky(Map.of(
            HEALTH, new RarityStat(4, 6, 9, 12, 15, 20),
            DEFENSE, new RarityStat(2, 3, 5, 8, 13, 21)), null, false, true, false),

    Coldfused(Map.of(
            MAGIC_FIND, new RarityStat(2),
            STRENGTH, new RarityStat(15, 20, 25, 35, 45, 55),
            CRITICAL_DAMAGE, new RarityStat(20, 30, 40, 50, 60, 70)),
            List.of("<dark_gray>Only if Wisp is equipped",
                    "<red>+75❁ Strength",
                    "<blue>+55☠ Crit Damage",
                    "<gray>Deal <gold>2x <gray>to fire pillars, breaking one grants",
                    "<white>30❂ True Defense <gray>and <red>+1.15x damage <gray>for <green>60s<gray>."), false, true, false),

    Fabled(Map.of(
            STRENGTH, new RarityStat(30, 35, 40, 50, 60, 75),
            CRITICAL_DAMAGE, new RarityStat(15, 20, 25, 32, 40, 50)),
            List.of("<gray>Critical hits have a chance to deal",
                    "<gray>up to <green>+15%<gray> extra damage."), false, true, false),

    Withered(Map.of(
            STRENGTH, new RarityStat(60, 75, 90, 110, 135, 170)),
            List.of("<gray>Grants <green>+1 <red>❁ Strength <gray>per",
                    "<red>Catacombs <gray>level."), false, true, false),

    Fanged(Map.of(
            STRENGTH, new RarityStat(30, 35, 40, 50, 60, 65),
            VITALITY, new RarityStat(2, 3, 4, 6, 8, 10),
            ATTACK_SPEED, new RarityStat(2, 3, 4, 6, 9, 10),
            CRITICAL_CHANCE, new RarityStat(3, 4, 5, 7, 8, 10)),
            List.of("<gray>Every <red>7th <gray>melee hit on an enemy",
                    "deals <red>+100% <gray>damage."), true, true, false),

    Gilded(Map.of(
            STRENGTH, new RarityStat(75, 75, 75, 75, 75, 90),
            DAMAGE, new RarityStat(75, 75, 75, 75, 75, 90),
            ABILITY_DAMAGE, new RarityStat(8, 8, 8, 8, 8, 10)),
            List.of("<gray>Upon killing an enemy, you have a",
                    "<gray>rare chance to grant coins to a",
                    "<gray>player around you."), false, true, true),

    Jerrys("Jerry's", Map.of(
            CRITICAL_CHANCE, new RarityStat(10),
            STRENGTH, new RarityStat(25),
            CRITICAL_DAMAGE, new RarityStat(50)),
            List.of("<gray>Consumes all your mana and adds 10%",
                    "<gray> of that amount as damage on your",
                    "<gray>next AotJ hit."), false, true, true),

    Suspicious(Map.of(
            CRITICAL_CHANCE, new RarityStat(1, 2, 3, 5, 7, 10),
            CRITICAL_DAMAGE, new RarityStat(30, 40, 50, 65, 85, 110)),
            List.of("<gray>Increases weapon damage by <red>+15<gray>."), false, true, false);


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

