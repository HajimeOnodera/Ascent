package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class LuckOfTheCave extends HotmUpgrade {
    @Override public String id() { return "LUCK_OF_THE_CAVE"; }
    @Override public String name() { return "Luck of the Cave"; }
    @Override public int maxLevel() { return 45; }
    @Override public int tierRequirement() { return 3; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 3.07); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Increases the chance for you to trigger rare occurrences in §2Dwarven Mines §7by §a" + (l + 5) + "%§7.");
    }
}
