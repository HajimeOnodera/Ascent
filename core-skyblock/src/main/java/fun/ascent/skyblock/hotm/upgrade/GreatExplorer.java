package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class GreatExplorer extends HotmUpgrade {
    @Override public String id() { return "GREAT_EXPLORER"; }
    @Override public String name() { return "Great Explorer"; }
    @Override public int maxLevel() { return 20; }
    @Override public int tierRequirement() { return 6; }
    @Override public Powder powder(int l) { return Powder.GEMSTONE; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 4); }
    @Override public List<String> buildLore(int l) {
        return List.of(
            "§7Increases the chance of finding treasure in §5Crystal",
            "§5Hollows §7by §a" + (l * 6 + 14) + "%§7."
        );
    }
}
