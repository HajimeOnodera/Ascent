package fun.ascent.skyblock.item.registries;

import java.util.Set;

public class LavaRodRegistry{
    private static final Set<String> LAVA_ROD_ID = Set.of(
            "TOPAZ_ROD",
            "STARTER_LAVA_ROD",
            "MAGMA_ROD",
            "INFERNO_ROD",
            "HELLFIRE_ROD"
    );

    public static boolean isLavaRod(String itemId) {
        return LAVA_ROD_ID.contains(itemId);
    }
}


