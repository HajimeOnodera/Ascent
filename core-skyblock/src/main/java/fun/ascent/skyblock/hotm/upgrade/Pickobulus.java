package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.PickaxeAbility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import java.util.List;

public final class Pickobulus extends PickaxeAbility {
    @Override public String id() { return "PICKOBULUS"; }
    @Override public String name() { return "Pickobulus"; }
    @Override public int tierRequirement() { return 2; }
    @Override public int cooldownSeconds() { return 110; }
    @Override public List<Class<? extends fun.ascent.skyblock.hotm.HotmUpgrade>> prerequisites() {
        return List.of(QuickForge.class, Crystallized.class);
    }
    @Override public void activate(SkyblockPlayer player) {}
    @Override public List<String> buildLore(int l) {
        return List.of(
            "<gold>Pickaxe Ability: Pickobulus",
            "<gray>Throw your pickaxe to create an explosion",
            "<gray>that <aqua>mines all ores <gray>in a <green>3 block radius<gray>.",
            " ",
            "<dark_gray>Cooldown: <green>110s"
        );
    }
}

