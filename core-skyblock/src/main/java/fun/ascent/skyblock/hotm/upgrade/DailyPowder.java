package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class DailyPowder extends HotmUpgrade {
    @Override public String id() { return "DAILY_POWDER"; }
    @Override public String name() { return "Daily Powder"; }
    @Override public int maxLevel() { return 100; }
    @Override public int tierRequirement() { return 3; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return l * 18 + 200; }
    @Override public List<String> buildLore(int l) {
        int bonus = (200 + (l - 1) * 18) * 2;
        return List.of(
            "§7Gain §a" + fmt(bonus) + " Powder §7from the first ore you",
            "§7mine each day. Works for all Powder types."
        );
    }
}
