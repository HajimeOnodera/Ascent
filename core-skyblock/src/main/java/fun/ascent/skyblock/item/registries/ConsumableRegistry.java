package fun.ascent.skyblock.item.registries;

import java.util.Set;
public class ConsumableRegistry {
    private static final Set<String> CONSUMABLE_IDS = Set.of(
            "DWARVEN_OS_BLOCK_BRAN",
            "DWARVEN_OS_GEMSTONE_GRAHAMS",
            "DWARVEN_OS_METALLIC_MINIS",
            "DWARVEN_OS_ORE_OATS",

            "BRAIN_FOOD",
            "FESTERING_MAGGOT",
            "ROSEWATER_FLASK",
            "FILLED_ROSEWATER_FLASK",
            "MCGRUBBER_BURGER",
            "METAPHYSICAL_SERUM",
            "MOBY_DUCK",
            "MOBY_DUCK_COLLECTOR_EDITION",
            "NEW_BOTTLE_OF_JYRRE",
            "REFINED_BOTTLE_OF_JYRRE",
            "DARK_CACOA_TRUFFLE",
            "REFINED_DARK_CACOA_TRUFFLE",
            "DISCRITE",
            "REAPER_PEPPER",
            "SPOTLITE",
            "TELEPORTER_PILL",
            "VIAL_OF_VENOM",
            "WRIGGLING_LARVA",
            "GOD_POTION",
            "GOD_POTION_2",
            "BOOSTER_COOKIE"

    );

    public static boolean isConsumable(String itemId) {
        return CONSUMABLE_IDS.contains(itemId);
    }
}

