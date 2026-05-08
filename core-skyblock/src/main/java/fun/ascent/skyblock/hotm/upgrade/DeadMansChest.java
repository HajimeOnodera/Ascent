package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class DeadMansChest extends HotmUpgrade {
    @Override public String id() { return "DEAD_MANS_CHEST"; }
    @Override public String name() { return "Dead Man's Chest"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 10; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 3.1); }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Increases the amount of loot you find from <aqua>Corpses <gray>in <aqua>Glacite Mineshafts <gray>by <green>" + (l * 4) + "%<gray>.");
    }
}

