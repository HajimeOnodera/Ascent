package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class DailyGrind extends HotmUpgrade {
    @Override public String id() { return "DAILY_GRIND"; }
    @Override public String name() { return "Daily Grind"; }
    @Override public int maxLevel() { return 100; }
    @Override public int tierRequirement() { return 8; }
    @Override public Powder powder(int l) { return Powder.GLACIAL; }
    @Override public int cost(int l) { return 200 + (l - 1) * 18; }
    @Override public List<String> buildLore(int l) {
        int bonus = (200 + (l - 1) * 18) * 2;
        return List.of(
            "§7Gain §a" + fmt(bonus) + " §bGlacial Powder §7from the first",
            "§7ore you mine each day in §bGlacite Tunnels§7."
        );
    }
}
