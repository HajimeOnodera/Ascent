package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class StrongArm extends HotmUpgrade {
    @Override public String id() { return "STRONG_ARM"; }
    @Override public String name() { return "Strong Arm"; }
    @Override public int maxLevel() { return 100; }
    @Override public int tierRequirement() { return 8; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 2.3); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Grants §a+" + (l * 5) + " §6⸕ Mining Speed §7when mining §7Tungsten or §8Umber§7.");
    }
}
