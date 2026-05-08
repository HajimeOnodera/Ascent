package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.PickaxeAbility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import java.util.List;

public final class GemstoneInfusion extends PickaxeAbility {
    @Override public String id() { return "GEMSTONE_INFUSION"; }
    @Override public String name() { return "Gemstone Infusion"; }
    @Override public int tierRequirement() { return 10; }
    @Override public int cooldownSeconds() { return 120; }
    @Override public void activate(SkyblockPlayer player) {}
    @Override public List<String> buildLore(int l) {
        return List.of(
            "<gold>Pickaxe Ability: Gemstone Infusion",
            "<gray>Increases the effectiveness of <gold>every Gemstone",
            "<gray>in your pick's Gemstone Slots by <green>50% <gray>for <green>16s<gray>.",
            " ",
            "<dark_gray>Cooldown: <green>120s"
        );
    }
}

