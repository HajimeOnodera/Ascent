package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class SkyMall extends HotmUpgrade {
    @Override public String id() { return "SKY_MALL"; }
    @Override public String name() { return "Sky Mall"; }
    @Override public int maxLevel() { return 1; }
    @Override public int tierRequirement() { return 4; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return 8; }
    @Override public List<String> buildLore(int l) {
        return List.of(
            "§7Every day, you receive a random buff in",
            "§2Dwarven Mines§7 and §bGlacite Tunnels§7."
        );
    }
}
