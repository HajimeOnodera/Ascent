package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class SeasonedMineman extends HotmUpgrade {
    @Override public String id() { return "SEASONED_MINEMAN"; }
    @Override public String name() { return "Seasoned Mineman"; }
    @Override public int maxLevel() { return 100; }
    @Override public int tierRequirement() { return 4; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 2.3); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Grants §a+" + String.format("%.1f", 5 + l * 0.1) + " §3☯ Mining Wisdom§7.");
    }
}
