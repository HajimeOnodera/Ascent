package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.PickaxeAbility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import java.util.List;

public final class HazardousMiner extends PickaxeAbility {
    @Override public String id() { return "HAZARDOUS_MINER"; }
    @Override public String name() { return "Hazardous Miner"; }
    @Override public int tierRequirement() { return 10; }
    @Override public int cooldownSeconds() { return 120; }
    @Override public void activate(SkyblockPlayer player) {}
    @Override public List<String> buildLore(int l) {
        return List.of(
            "§6Pickaxe Ability: Hazardous Miner",
            "§7Explode the blocks around you, granting §a+1,000",
            "§6⸕ Mining Speed §7and §a+1,000 §6☘ Mining Fortune §7to you",
            "§7and any nearby miners for §a5s§7.",
            " ",
            "§8Cooldown: §a120s"
        );
    }
}
