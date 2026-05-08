package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class StarPowder extends HotmUpgrade {
    @Override public String id() { return "STAR_POWDER"; }
    @Override public String name() { return "Star Powder"; }
    @Override public int maxLevel() { return 1; }
    @Override public int tierRequirement() { return 5; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return 0; }
    @Override public List<Class<? extends HotmUpgrade>> prerequisites() {
        return List.of(GreatExplorer.class, FrontLoaded.class);
    }
    @Override public List<String> buildLore(int l) {
        return List.of(
            "<gray>Mining <dark_purple>Fallen Stars <gray>gives <green>+3 <dark_green>Mithril Powder",
            "<gray>per <dark_purple>Fallen Star <gray>piece."
        );
    }
}

