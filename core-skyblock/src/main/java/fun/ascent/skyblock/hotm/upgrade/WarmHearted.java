package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class WarmHearted extends HotmUpgrade {
    @Override public String id() { return "WARM_HEARTED"; }
    @Override public String name() { return "Warm Hearted"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 8; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 3.1); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Grants §a+" + String.format("%.1f", l * 0.2) + " §b❄ Cold Resistance§7.");
    }
}
