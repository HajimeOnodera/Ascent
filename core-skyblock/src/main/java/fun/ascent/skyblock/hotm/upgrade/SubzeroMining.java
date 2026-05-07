package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class SubzeroMining extends HotmUpgrade {
    @Override public String id() { return "SUBZERO_MINING"; }
    @Override public String name() { return "Subzero Mining"; }
    @Override public int maxLevel() { return 100; }
    @Override public int tierRequirement() { return 9; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 2.3); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Grants §a+" + l + " §6☘ Mining Fortune §7when mining §bGlacite§7.");
    }
}
