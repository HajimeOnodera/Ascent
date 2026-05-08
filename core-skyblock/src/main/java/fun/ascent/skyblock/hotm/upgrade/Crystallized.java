package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class Crystallized extends HotmUpgrade {
    @Override public String id() { return "CRYSTALLIZED"; }
    @Override public String name() { return "Crystallized"; }
    @Override public int maxLevel() { return 30; }
    @Override public int tierRequirement() { return 3; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 3.4); }
    @Override public List<Class<? extends HotmUpgrade>> prerequisites() {
        return List.of(Pickobulus.class, FrontLoaded.class);
    }
    @Override public List<String> buildLore(int l) {
        int speed = 20 + (l - 1) * 6;
        int fortune = 20 + (l - 1) * 5;
        return List.of("<gray>Grants <green>+" + speed + " <gold>⸕ Mining Speed <gray>and <green>+" + fortune +
            " <gold>☘ Mining Fortune <gray>near <dark_purple>Fallen Stars<gray>.");
    }
}

