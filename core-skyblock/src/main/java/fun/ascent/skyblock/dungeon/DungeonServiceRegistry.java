package fun.ascent.skyblock.dungeon;

public class DungeonServiceRegistry {
    private static DungeonService instance;

    public static DungeonService get() {
        return instance;
    }

    public static void register(DungeonService service) {
        instance = service;
    }
}
