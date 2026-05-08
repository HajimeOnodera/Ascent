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
            "<gold>Pickaxe Ability: Hazardous Miner",
            "<gray>Explode the blocks around you, granting <green>+1,000",
            "<gold>⸕ Mining Speed <gray>and <green>+1,000 <gold>☘ Mining Fortune <gray>to you",
            "<gray>and any nearby miners for <green>5s<gray>.",
            " ",
            "<dark_gray>Cooldown: <green>120s"
        );
    }
}

