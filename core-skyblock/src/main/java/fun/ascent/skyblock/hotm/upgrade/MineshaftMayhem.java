package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class MineshaftMayhem extends HotmUpgrade {
    @Override public String id() { return "MINESHAFT_MAYHEM"; }
    @Override public String name() { return "Mineshaft Mayhem"; }
    @Override public int maxLevel() { return 1; }
    @Override public int tierRequirement() { return 8; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return 0; }
    @Override public List<String> buildLore(int l) {
        return List.of(
            "<gray>Increases the rate at which <aqua>Glacite Mineshafts",
            "<gray>spawn by <green>10%<gray>."
        );
    }
}

