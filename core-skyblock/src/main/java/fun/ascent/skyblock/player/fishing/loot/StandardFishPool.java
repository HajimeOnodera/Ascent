package fun.ascent.skyblock.player.fishing.loot;

import net.minestom.server.item.Material;

public final class StandardFishPool extends FishLootPool {

    private static final StandardFishPool INSTANCE = new StandardFishPool();

    private StandardFishPool() {
        register(new FishLootEntry(Material.COD, "Raw Fish", 60, 10.0, 1, 1, 1));
        register(new FishLootEntry(Material.SALMON, "Raw Salmon", 25, 15.0, 1, 1, 1));
        register(new FishLootEntry(Material.PUFFERFISH, "Pufferfish", 8, 25.0, 1, 1, 1));
        register(new FishLootEntry(Material.TROPICAL_FISH, "Clownfish", 5, 40.0, 1, 1, 1));
        register(new FishLootEntry(Material.PRISMARINE_SHARD, "Prismarine Shard", 4, 30.0, 1, 1, 3));
        register(new FishLootEntry(Material.PRISMARINE_CRYSTALS, "Prismarine Crystals", 3, 35.0, 1, 1, 2));
        register(new FishLootEntry(Material.SPONGE, "Sponge", 1, 100.0, 1, 1, 1));
    }

    public static StandardFishPool get() {
        return INSTANCE;
    }
}
