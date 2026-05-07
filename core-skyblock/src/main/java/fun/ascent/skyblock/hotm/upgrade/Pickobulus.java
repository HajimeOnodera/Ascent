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
            "§6Pickaxe Ability: Pickobulus",
            "§7Throw your pickaxe to create an explosion",
            "§7that §bmines all ores §7in a §a3 block radius§7.",
            " ",
            "§8Cooldown: §a110s"
        );
    }
}
