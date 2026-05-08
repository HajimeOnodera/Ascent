package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class RagsToRiches extends HotmUpgrade {
    @Override public String id() { return "RAGS_TO_RICHES"; }
    @Override public String name() { return "Rags to Riches"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 10; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 3); }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Grants <green>+" + (l * 2) + " <gold>☘ Mining Fortune <gray>when mining <gray>Tungsten or <dark_gray>Umber<gray>.");
    }
}

