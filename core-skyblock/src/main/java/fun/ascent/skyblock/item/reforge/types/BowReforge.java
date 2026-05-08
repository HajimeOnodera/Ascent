package fun.ascent.skyblock.item.reforge.types;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.player.stats.Stats;

import java.util.List;
import java.util.Map;

import static fun.ascent.skyblock.player.stats.Stats.*;

public enum BowReforge implements Reforge {

    Awkward(Map.of(
            INTELLIGENCE, new RarityStat(-5, -10, -18, -32, -50, -60),
            CRITICAL_CHANCE, new RarityStat(10, 12, 15, 20, 25, 30),
            CRITICAL_DAMAGE, new RarityStat(5, 10, 15, 22, 30, 40)), null),

    Deadly(Map.of(
            CRITICAL_CHANCE, new RarityStat(10, 13, 16, 19, 22, 25),
            CRITICAL_DAMAGE, new RarityStat(5, 10, 18, 32, 50, 70)), null),

    Fine(Map.of(
            CRITICAL_CHANCE, new RarityStat(5, 7, 9, 12, 15, 20),
            STRENGTH, new RarityStat(3, 7, 12, 18, 25, 40),
            CRITICAL_DAMAGE, new RarityStat(2, 4, 7, 10, 15, 20)), null),

    Grand(Map.of(
            STRENGTH, new RarityStat(25, 32, 40, 50, 60, 70)), null),

    Hasty(Map.of(
            CRITICAL_CHANCE, new RarityStat(20, 25, 30, 40, 50, 60),
            STRENGTH, new RarityStat(3, 5, 7, 10, 15, 20)), null),

    Neat(Map.of(
            INTELLIGENCE, new RarityStat(3, 6, 10, 15, 20, 30),
            CRITICAL_CHANCE, new RarityStat(10, 12, 14, 17, 20, 25),
            CRITICAL_DAMAGE, new RarityStat(4, 8, 14, 20, 30, 40)), null),

    Odd(Map.of(
            INTELLIGENCE, new RarityStat(-5, -10, -18, -32, -50, -50),
            CRITICAL_CHANCE, new RarityStat(1, 2, 4, 7, 10, 10),
            CRITICAL_DAMAGE, new RarityStat(1, 2, 4, 7, 10, 10)), null),

    Rapid(Map.of(
            STRENGTH, new RarityStat(2, 3, 4, 7, 10, 12),
            CRITICAL_DAMAGE, new RarityStat(35, 45, 55, 65, 75, 90)), null),

    Rich(Map.of(
            INTELLIGENCE, new RarityStat(3, 6, 10, 15, 20, 30),
            CRITICAL_CHANCE, new RarityStat(10, 12, 14, 17, 20, 25),
            CRITICAL_DAMAGE, new RarityStat(2, 4, 7, 10, 15, 20)), null),

    Unreal(Map.of(
            CRITICAL_CHANCE, new RarityStat(8, 9, 10, 11, 13, 15),
            STRENGTH, new RarityStat(3, 7, 12, 18, 25, 34),
            CRITICAL_DAMAGE, new RarityStat(5, 10, 18, 32, 50, 70)), null),

    Headstrong(Map.of(
            CRITICAL_CHANCE, new RarityStat(10, 11, 12, 13, 15, 17),
            STRENGTH, new RarityStat(2, 5, 10, 16, 23, 33),
            CRITICAL_DAMAGE, new RarityStat(4, 8, 16, 28, 42, 60)),
            List.of("<gray>Deal <green>+8%<gray> extra damage when arrows",
                    "<gray>hit the head of a mob."), true),

    Precise(Map.of(
            CRITICAL_CHANCE, new RarityStat(8, 9, 10, 11, 13, 15),
            STRENGTH, new RarityStat(3, 7, 12, 18, 25, 34),
            CRITICAL_DAMAGE, new RarityStat(5, 10, 18, 32, 50, 70)),
            List.of("<gray>Deal <green>+10%<gray> extra damage when arrows",
                    "<gray>hit the head of a mob."), true),

    Spiritual(Map.of(
            CRITICAL_CHANCE, new RarityStat(7, 8, 9, 10, 12, 14),
            STRENGTH, new RarityStat(4, 8, 14, 20, 28, 38),
            CRITICAL_DAMAGE, new RarityStat(10, 15, 23, 37, 55, 75)),
            List.of("<gray>Grants a <green>10% <gray>chance to spawn a",
                    "<gray>Spirit Decoy when you kill an",
                    "<gray>enemy in a dungeon."), true);

    private static final ItemType[] BOW = {ItemType.BOW};

    private final Map<Stats, RarityStat> stats;
    private final List<String> loreText;
    private final boolean reforgeStone;

    BowReforge(Map<Stats, RarityStat> stats, List<String> loreText) {
        this(stats, loreText, false);
    }

    BowReforge(Map<Stats, RarityStat> stats, List<String> loreText, boolean reforgeStone) {
        this.stats = stats;
        this.loreText = loreText;
        this.reforgeStone = reforgeStone;
    }

    @Override public String getName() { return name(); }
    @Override public String getId() { return name().toUpperCase(); }
    @Override public List<String> getLoreText() { return loreText; }
    @Override public Map<Stats, RarityStat> getStats() { return stats; }
    @Override public boolean isReforgeStone() { return reforgeStone; }
    @Override public boolean isItemSpecific() { return false; }
    @Override public ItemType[] getAllowedTypes() { return BOW; }

    @Override
    public double getStat(Stats stat, Rarity rarity) {
        RarityStat rs = stats.get(stat);
        return rs == null ? 0 : rs.fromRarity(rarity);
    }
}

