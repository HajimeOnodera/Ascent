package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class StrongArm extends HotmUpgrade {
    @Override public String id() { return "STRONG_ARM"; }
    @Override public String name() { return "Strong Arm"; }
    @Override public int maxLevel() { return 100; }
    @Override public int tierRequirement() { return 8; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 2.3); }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Grants <green>+" + (l * 5) + " <gold>⸕ Mining Speed <gray>when mining <gray>Tungsten or <dark_gray>Umber<gray>.");
    }
}

