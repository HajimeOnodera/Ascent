package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class MiningSpeed extends HotmUpgrade {
    @Override public String id() { return "MINING_SPEED"; }
    @Override public String name() { return "Mining Speed"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 1; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 3); }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Grants <green>+" + (l * 20) + " <gold>⸕ Mining Speed<gray>.");
    }
}

