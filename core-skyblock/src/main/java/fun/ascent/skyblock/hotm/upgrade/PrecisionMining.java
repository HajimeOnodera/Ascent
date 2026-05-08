package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class PrecisionMining extends HotmUpgrade {
    @Override public String id() { return "PRECISION_MINING"; }
    @Override public String name() { return "Precision Mining"; }
    @Override public int maxLevel() { return 1; }
    @Override public int tierRequirement() { return 4; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return 8; }
    @Override public List<String> buildLore(int l) {
        return List.of(
            "<gray>When mining ore, a particle indicator",
            "<gray>appears showing the ore's <green>weak point<gray>.",
            "<gray>Hitting it grants <green>+30 <gold>⸕ Mining Speed<gray>."
        );
    }
}

