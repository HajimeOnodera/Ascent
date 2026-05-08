package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class MiningSpeed2 extends HotmUpgrade {
    @Override public String id() { return "MINING_SPEED_2"; }
    @Override public String name() { return "Mining Speed Boost"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 7; }
    @Override public Powder powder(int l) { return Powder.GEMSTONE; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 3.2); }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Grants <green>+" + (l * 40) + " <gold>⸕ Mining Speed<gray>.");
    }
}

