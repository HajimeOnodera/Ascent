package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class Mole extends HotmUpgrade {
    @Override public String id() { return "MOLE"; }
    @Override public String name() { return "Mole"; }
    @Override public int maxLevel() { return 190; }
    @Override public int tierRequirement() { return 6; }
    @Override public Powder powder(int l) { return Powder.GEMSTONE; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 2.2); }
    @Override public List<String> buildLore(int l) {
        int raw = 50 + (l - 1) * 5;
        int blocks = raw / 100 + 1;
        int chance = raw - (blocks - 1) * 100;
        return List.of("§7When mining hard stone, you have a §a" + chance +
            "%§7 chance to mine §a" + blocks + " §7adjacent hard stone block" + (blocks > 1 ? "s" : "") + "§7.");
    }
}
