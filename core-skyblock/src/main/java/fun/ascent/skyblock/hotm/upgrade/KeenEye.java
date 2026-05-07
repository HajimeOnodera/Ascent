package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class KeenEye extends HotmUpgrade {
    @Override public String id() { return "KEEN_EYE"; }
    @Override public String name() { return "Keen Eye"; }
    @Override public int maxLevel() { return 1; }
    @Override public int tierRequirement() { return 8; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return 0; }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Grants the ability to see ores through walls in §bGlacite Tunnels§7.");
    }
}
