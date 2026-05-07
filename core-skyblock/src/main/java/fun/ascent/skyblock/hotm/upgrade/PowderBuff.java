package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class PowderBuff extends HotmUpgrade {
    @Override public String id() { return "POWDER_BUFF"; }
    @Override public String name() { return "Powder Buff"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 7; }
    @Override public Powder powder(int l) { return Powder.GEMSTONE; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 3.2); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Gain §a+" + l + "% §7more §2Mithril Powder §7and §dGemstone Powder§7.");
    }
}
