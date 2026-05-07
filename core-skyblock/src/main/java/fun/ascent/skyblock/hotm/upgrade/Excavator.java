package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class Excavator extends HotmUpgrade {
    @Override public String id() { return "EXCAVATOR"; }
    @Override public String name() { return "Excavator"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 10; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 3); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Grants §a+" + (l * 5) + " §6⸕ Mining Speed §7when mining §7Tungsten or §8Umber§7.");
    }
}
