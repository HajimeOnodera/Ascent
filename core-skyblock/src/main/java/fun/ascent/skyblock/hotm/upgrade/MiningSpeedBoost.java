package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.PickaxeAbility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import java.util.List;

public final class MiningSpeedBoost extends PickaxeAbility {
    @Override public String id() { return "MINING_SPEED_BOOST"; }
    @Override public String name() { return "Mining Speed Boost"; }
    @Override public int tierRequirement() { return 2; }
    @Override public int cooldownSeconds() { return 120; }
    @Override public List<Class<? extends fun.ascent.skyblock.hotm.HotmUpgrade>> prerequisites() {
        return List.of(TitaniumInsanium.class, LuckOfTheCave.class);
    }
    @Override public void activate(SkyblockPlayer player) {}
    @Override public List<String> buildLore(int l) {
        return List.of(
            "§6Pickaxe Ability: Mining Speed Boost",
            "§7Grants §a+300% §6⸕ Mining Speed §7for §a20s§7.",
            " ",
            "§8Cooldown: §a120s"
        );
    }
}
