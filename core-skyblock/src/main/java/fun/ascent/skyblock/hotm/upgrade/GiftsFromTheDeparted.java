package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class GiftsFromTheDeparted extends HotmUpgrade {
    @Override public String id() { return "GIFTS_FROM_THE_DEPARTED"; }
    @Override public String name() { return "Gifts from the Departed"; }
    @Override public int maxLevel() { return 100; }
    @Override public int tierRequirement() { return 10; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 2.45); }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Increases the chance of finding loot from §bCorpses §7in §bGlacite Mineshafts §7by §a" + l + "%§7.");
    }
}
