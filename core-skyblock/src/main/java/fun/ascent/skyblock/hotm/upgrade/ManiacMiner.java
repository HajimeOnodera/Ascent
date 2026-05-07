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
            "§6Pickaxe Ability: Maniac Miner",
            "§7Spends all your §bMana §7and grants §a+1 §6⸕ Mining",
            "§7Speed §7for each §b1 Mana §7spent for §a15s§7.",
            " ",
            "§8Cooldown: §a60s"
        );
    }
}
