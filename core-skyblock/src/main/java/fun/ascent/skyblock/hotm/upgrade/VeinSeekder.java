package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.PickaxeAbility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import java.util.List;

public final class VeinSeekder extends PickaxeAbility {
    @Override public String id() { return "VEIN_SEEKER"; }
    @Override public String name() { return "Vein Seeker"; }
    @Override public int tierRequirement() { return 6; }
    @Override public int cooldownSeconds() { return 10; }
    @Override public List<Class<? extends fun.ascent.skyblock.hotm.HotmUpgrade>> prerequisites() {
        return List.of(LonesomeMiner.class);
    }
    @Override public void activate(SkyblockPlayer player) {}
    @Override public List<String> buildLore(int l) {
        return List.of(
            "<gold>Pickaxe Ability: Vein Seeker",
            "<gray>Points in the direction of the nearest ore vein,",
            "<gray>highlights all ores in the vein and grants <green>+5",
            "<gold>⸕ Mining Speed <gray>for each ore in the vein.",
            " ",
            "<dark_gray>Cooldown: <green>10s"
        );
    }
}

