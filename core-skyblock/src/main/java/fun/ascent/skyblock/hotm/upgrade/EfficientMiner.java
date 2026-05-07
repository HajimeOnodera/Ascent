package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class EfficientMiner extends HotmUpgrade {
    @Override public String id() { return "EFFICIENT_MINER"; }
    @Override public String name() { return "Efficient Miner"; }
    @Override public int maxLevel() { return 100; }
    @Override public int tierRequirement() { return 4; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 2.6); }
    @Override public List<Class<? extends HotmUpgrade>> prerequisites() {
        return List.of(DailyPowder.class, SeasonedMineman.class, Orbiter.class);
    }
    @Override public List<String> buildLore(int l) {
        double pct = 10 + l * 0.4;
        int blocks = 1 + l / 20;
        return List.of("§7When mining ores, you have a §a" +
            String.format("%.1f", pct) + "%§7 chance to mine §a" + blocks +
            " §7adjacent ore" + (blocks > 1 ? "s" : "") + "§7.");
    }
}
