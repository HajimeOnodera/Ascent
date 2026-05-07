package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class QuickForge extends HotmUpgrade {
    @Override public String id() { return "QUICK_FORGE"; }
    @Override public String name() { return "Quick Forge"; }
    @Override public int maxLevel() { return 20; }
    @Override public int tierRequirement() { return 2; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 4); }
    @Override public List<String> buildLore(int l) {
        double reduction = Math.min(30, 10 + l * 0.5 + (l / 20) * 20);
        return List.of("§7Decreases the time it takes to forge by §a" +
            String.format("%.1f", reduction) + "%§7.");
    }
}
