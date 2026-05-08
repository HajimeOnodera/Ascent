package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class Surveyor extends HotmUpgrade {
    @Override public String id() { return "SURVEYOR"; }
    @Override public String name() { return "Surveyor"; }
    @Override public int maxLevel() { return 20; }
    @Override public int tierRequirement() { return 9; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 4); }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Increases the chance for <aqua>Glacite Mineshafts <gray>to spawn by <green>" + (l * 2) + "%<gray>.");
    }
}

