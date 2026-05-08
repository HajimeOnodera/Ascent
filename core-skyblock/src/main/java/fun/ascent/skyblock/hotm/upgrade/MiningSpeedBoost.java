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
            "<gold>Pickaxe Ability: Mining Speed Boost",
            "<gray>Grants <green>+300% <gold>⸕ Mining Speed <gray>for <green>20s<gray>.",
            " ",
            "<dark_gray>Cooldown: <green>120s"
        );
    }
}

