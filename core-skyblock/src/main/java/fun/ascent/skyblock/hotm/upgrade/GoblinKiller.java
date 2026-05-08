package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.Powder;
import java.util.List;

public final class GoblinKiller extends HotmUpgrade {
    @Override public String id() { return "GOBLIN_KILLER"; }
    @Override public String name() { return "Goblin Killer"; }
    @Override public int maxLevel() { return 1; }
    @Override public int tierRequirement() { return 5; }
    @Override public Powder powder(int l) { return Powder.MITHRIL; }
    @Override public int cost(int l) { return 8; }
    @Override public List<String> buildLore(int l) {
        return List.of("<gray>Killing <gold>Goblins <gray>gives extra <dark_green>Mithril Powder<gray>.");
    }
}

