package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class FrozenSolid extends HotmUpgrade {
    @Override public String id() { return "FROZEN_SOLID"; }
    @Override public String name() { return "Frozen Solid"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 10; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 1, 3.1); }
    @Override public List<Class<? extends HotmUpgrade>> prerequisites() {
        return List.of(GiftsFromTheDeparted.class, DeadMansChest.class);
    }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Killing mobs in §bGlacite Tunnels §7gives §a+" + (l * 2) + " §bGlacial Powder§7.");
    }
}
