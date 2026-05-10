package fun.ascent.skyblock.item.items.bows;

import fun.ascent.skyblock.item.ItemAbility;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

import java.util.List;

public class Terminator implements ItemDefinition {

    @Override
    public String getItemId() {
        return "TERMINATOR";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("<gray>Shoots <aqua>3 <gray>arrows at once.",
                        "<gray>Can damage endermen.",
                        " ",
                        "<red>Divides your <blue>☣ Crit Chance <red>by 4!")
                .ability(new ItemAbility(
                        "Salvation", ItemAbility.AbilityType.LEFT_CLICK,
                        List.of("Can be casted after landing <gold>3 <gray>hits.",
                                "Shoot a beam, penetrating up to <yellow>5",
                                ">enemies.",
                                "The beam always crits."),
                        0, 1, 0, 0
                ));
    }
}
