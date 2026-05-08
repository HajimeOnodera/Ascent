package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.PickaxeAbility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import java.util.List;

public final class ManiacMiner extends PickaxeAbility {
    @Override public String id() { return "MANIAC_MINER"; }
    @Override public String name() { return "Maniac Miner"; }
    @Override public int tierRequirement() { return 6; }
    @Override public int cooldownSeconds() { return 60; }
    @Override public List<Class<? extends fun.ascent.skyblock.hotm.HotmUpgrade>> prerequisites() {
        return List.of(GreatExplorer.class);
    }
    @Override public void activate(SkyblockPlayer player) {}
    @Override public List<String> buildLore(int l) {
        return List.of(
            "<gold>Pickaxe Ability: Maniac Miner",
            "<gray>Spends all your <aqua>Mana <gray>and grants <green>+1 <gold>⸕ Mining",
            "<gray>Speed <gray>for each <aqua>1 Mana <gray>spent for <green>15s<gray>.",
            " ",
            "<dark_gray>Cooldown: <green>60s"
        );
    }
}

