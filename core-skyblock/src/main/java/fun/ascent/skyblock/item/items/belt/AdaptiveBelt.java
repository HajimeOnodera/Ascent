package fun.ascent.skyblock.item.items.belt;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class AdaptiveBelt implements ItemDefinition {

    @Override
    public String getItemId() {
        return "ADAPTIVE_BELT";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Grants additional bonuses based on your",
                        "selected dungeon class!",
                        "",
                        "<aqua>Berserk<gray>: +<red>10❁ Strength",
                        "<aqua>Healer<gray>: +<red>10❤ Health<gray>, +<green>5☄ Mending",
                        "<aqua>Mage<gray>: +<aqua>25✎ Intelligence",
                        "<aqua>Tank<gray>: +<red>5❤ Health<gray>, +<green>10❈ Defense",
                        "<aqua>Archer<gray>: +<blue>2☣ Crit Chance<gray>, +<blue>5☠ Crit Damage");
    }
}
