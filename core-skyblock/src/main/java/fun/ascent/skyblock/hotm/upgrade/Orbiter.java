package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class Orbiter extends HotmUpgrade {
    @Override public String id() { return "ORBITER"; }
    @Override public String name() { return "Orbiter"; }
    @Override public int maxLevel() { return 80; }
    @Override public int tierRequirement() { return 4; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return l + 70; }
    @Override public List<String> buildLore(int l) {
        return List.of("§7When mining ores, you have a §a" +
            String.format("%.2f", 0.2 + l * 0.01) + "%§7 chance to get a random amount of XP orbs.");
    }
}
