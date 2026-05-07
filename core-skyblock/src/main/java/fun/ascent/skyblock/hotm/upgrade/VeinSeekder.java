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
            "§6Pickaxe Ability: Vein Seeker",
            "§7Points in the direction of the nearest ore vein,",
            "§7highlights all ores in the vein and grants §a+5",
            "§6⸕ Mining Speed §7for each ore in the vein.",
            " ",
            "§8Cooldown: §a10s"
        );
    }
}
