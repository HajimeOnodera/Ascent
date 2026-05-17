package fun.ascent.skyblock.player.fishing.loot;

import net.minestom.server.item.Material;

public final class TreasureLootPool extends FishLootPool {

    private static final TreasureLootPool INSTANCE = new TreasureLootPool();

    private TreasureLootPool() {
        register(new FishLootEntry(Material.GOLD_INGOT, "Gold Ingot", 50, 45.0, 1, 1, 3));
        register(new FishLootEntry(Material.IRON_INGOT, "Iron Ingot", 40, 30.0, 1, 1, 4));
        register(new FishLootEntry(Material.DIAMOND, "Diamond", 20, 120.0, 1, 1, 2));
        register(new FishLootEntry(Material.EMERALD, "Emerald", 15, 150.0, 1, 1, 2));
        register(new FishLootEntry(Material.SADDLE, "Saddle", 10, 80.0, 1, 1, 1));
        register(new FishLootEntry(Material.ENCHANTED_BOOK, "Enchanted Book", 8, 200.0, 1, 1, 1));
        register(new FishLootEntry(Material.MUSIC_DISC_CAT, "Music Disc", 5, 250.0, 1, 1, 1));
    }

    public static TreasureLootPool get() {
        return INSTANCE;
    }
}
