package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class DustCollector extends HotmUpgrade {
    @Override public String id() { return "DUST_COLLECTOR"; }
    @Override public String name() { return "Dust Collector"; }
    @Override public int maxLevel() { return 20; }
    @Override public int tierRequirement() { return 8; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 4); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Grants §a+" + (l * 5) + " §6☘ Mining Fortune §7when mining §bGlacite§7.");
    }
}
