package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class Professional extends HotmUpgrade {
    @Override public String id() { return "PROFESSIONAL"; }
    @Override public String name() { return "Professional"; }
    @Override public int maxLevel() { return 140; }
    @Override public int tierRequirement() { return 6; }
    @Override public Powder powder(int l) { return Powder.GEMSTONE; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 2.3); }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Grants <green>+" + (l * 5) + " <gold>⸕ Mining Speed <gray>when mining <light_purple>Gemstones<gray>.");
    }
}

