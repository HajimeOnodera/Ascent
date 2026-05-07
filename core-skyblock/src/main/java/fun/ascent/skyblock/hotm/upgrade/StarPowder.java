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
            "§7Mining §5Fallen Stars §7gives §a+3 §2Mithril Powder",
            "§7per §5Fallen Star §7piece."
        );
    }
}
