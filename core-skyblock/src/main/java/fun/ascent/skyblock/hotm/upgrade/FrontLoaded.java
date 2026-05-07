package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class FrontLoaded extends HotmUpgrade {
    @Override public String id() { return "FRONT_LOADED"; }
    @Override public String name() { return "Front Loaded"; }
    @Override public int maxLevel() { return 1; }
    @Override public int tierRequirement() { return 4; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return 8; }
    @Override public List<String> buildLore(int l) {
        return List.of(
            "§7Grants §a+100 §6⸕ Mining Speed §7and §a+100 §6☘ Mining Fortune",
            "§7for the first §a2,500 §7ores you mine in a day."
        );
    }
}
