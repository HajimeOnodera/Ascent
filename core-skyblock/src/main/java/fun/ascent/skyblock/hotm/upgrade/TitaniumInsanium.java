package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class TitaniumInsanium extends HotmUpgrade {
    @Override public String id() { return "TITANIUM_INSANIUM"; }
    @Override public String name() { return "Titanium Insanium"; }
    @Override public int maxLevel() { return 50; }
    @Override public int tierRequirement() { return 2; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return (int) Math.pow(l + 2, 3.1); }
    @Override public List<Class<? extends HotmUpgrade>> prerequisites() {
        return List.of(MiningFortune.class, MiningSpeedBoost.class);
    }
    @Override public List<String> buildLore(int l) {
        return List.of("§7When mining §fMithril§7, you have a §a" +
            String.format("%.1f", 2 + l * 0.1) + "%§7 chance to convert it to §fTitanium§7.");
    }
}
