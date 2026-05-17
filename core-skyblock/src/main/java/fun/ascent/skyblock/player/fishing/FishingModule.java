package fun.ascent.skyblock.player.fishing;

import fun.ascent.skyblock.entity.mob.EntityRegistry;

public final class FishingModule {

    private FishingModule() {}

    public static void init() {
        EntityRegistry.scanAndRegister("fun.ascent.skyblock.player.fishing.loot.mobs");
    }
}
