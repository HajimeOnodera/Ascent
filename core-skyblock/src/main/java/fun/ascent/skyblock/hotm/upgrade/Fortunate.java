package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class Fortunate extends HotmUpgrade {
    @Override public String id() { return "FORTUNATE"; }
    @Override public String name() { return "Fortunate"; }
    @Override public int maxLevel() { return 20; }
    @Override public int tierRequirement() { return 6; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 3.05); }
    @Override public List<Class<? extends HotmUpgrade>> prerequisites() {
        return List.of(Mole.class, GreatExplorer.class);
    }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Grants <green>+" + (l * 5) + " <gold>☘ Mining Fortune <gray>when mining <light_purple>Gemstones<gray>.");
    }
}

