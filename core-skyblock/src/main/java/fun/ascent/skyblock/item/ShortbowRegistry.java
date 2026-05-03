package fun.ascent.skyblock.item;

import java.util.Set;

public class ShortbowRegistry {

    private static final Set<String> SHORTBOW_IDS = Set.of(
            "MACHINE_GUN_BOW",
            "ARTISANAL_SHORTBOW",
            "JUJU_SHORTBOW",
            "TERMINATOR",
            "DRAGON_SHORTBOW",
            "SCORPION_BOW",
            "MOSQUITO_BOW",
            "ITEM_SPIRIT_BOW",
            "STARRED_ITEM_SPIRIT_BOW"
    );

    public static boolean isShortbow(String itemId) {
        return SHORTBOW_IDS.contains(itemId);
    }
}
