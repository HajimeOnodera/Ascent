package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class LonesomeMiner extends HotmUpgrade {
    @Override public String id() { return "LONESOME_MINER"; }
    @Override public String name() { return "Lonesome Miner"; }
    @Override public int maxLevel() { return 45; }
    @Override public int tierRequirement() { return 6; }
    @Override public Powder powder(int l) { return Powder.GEMSTONE; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 3.07); }
    @Override public List<Class<? extends HotmUpgrade>> prerequisites() {
        return List.of(GoblinKiller.class, MiningSpeed2.class, Professional.class);
    }
    @Override public List<String> buildLore(int l) {
        return List.of("§7Increases §c❁ Strength§7, §9☣ Crit Chance§7, §9☠ Crit Damage§7, " +
            "§a❈ Defense§7 and §c❤ Health §7stats gain by §a" +
            String.format("%.1f", 5 + (l - 1) * 0.5) + "%§7 while in the §5Crystal Hollows§7.");
    }
}
