package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class NoStoneUnturned extends HotmUpgrade {
    @Override public String id() { return "NO_STONE_UNTURNED"; }
    @Override public String name() { return "No Stone Unturned"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 8; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 3.05); }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Increases the chance of finding <yellow>Rare <gray>drops in <aqua>Glacite Tunnels <gray>by <green>" + (l * 5) + "%<gray>.");
    }
}

