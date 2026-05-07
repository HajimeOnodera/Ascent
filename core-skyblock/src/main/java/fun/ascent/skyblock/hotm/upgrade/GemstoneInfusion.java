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
            "§6Pickaxe Ability: Gemstone Infusion",
            "§7Increases the effectiveness of §6every Gemstone",
            "§7in your pick's Gemstone Slots by §a50% §7for §a16s§7.",
            " ",
            "§8Cooldown: §a120s"
        );
    }
}
