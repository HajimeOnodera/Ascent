package fun.ascent.skyblock.dungeon;

import fun.ascent.skyblock.dungeon.generation.RoomType;

import java.util.Map;

public enum DungeonFloor {
    ENTRANCE("The Catacombs - Entrance", "E", 4, 1, 1, 1, false),
    FLOOR_1("The Catacombs - Floor I", "F1", 4, 1, 1, 1, false),
    FLOOR_2("The Catacombs - Floor II", "F2", 5, 2, 1, 1, false),
    FLOOR_3("The Catacombs - Floor III", "F3", 5, 2, 1, 1, false),
    FLOOR_4("The Catacombs - Floor IV", "F4", 5, 2, 1, 1, false),
    FLOOR_5("The Catacombs - Floor V", "F5", 6, 3, 1, 1, false),
    FLOOR_6("The Catacombs - Floor VI", "F6", 6, 3, 1, 1, false),
    FLOOR_7("The Catacombs - Floor VII", "F7", 6, 3, 1, 1, false),

    MASTER_1("Master Mode Catacombs - Floor I", "M1", 4, 1, 1, 1, true),
    MASTER_2("Master Mode Catacombs - Floor II", "M2", 5, 2, 1, 1, true),
    MASTER_3("Master Mode Catacombs - Floor III", "M3", 5, 2, 1, 1, true),
    MASTER_4("Master Mode Catacombs - Floor IV", "M4", 5, 2, 1, 1, true),
    MASTER_5("Master Mode Catacombs - Floor V", "M5", 6, 3, 1, 1, true),
    MASTER_6("Master Mode Catacombs - Floor VI", "M6", 6, 3, 1, 1, true),
    MASTER_7("Master Mode Catacombs - Floor VII", "M7", 6, 3, 1, 1, true);

    private final String displayName;
    private final String shortName;
    private final int gridSize;
    private final int puzzleCount;
    private final int minibossCount;
    private final int trapCount;
    private final boolean masterMode;

    DungeonFloor(String displayName, String shortName, int gridSize,
                 int puzzleCount, int minibossCount, int trapCount, boolean masterMode) {
        this.displayName = displayName;
        this.shortName = shortName;
        this.gridSize = gridSize;
        this.puzzleCount = puzzleCount;
        this.minibossCount = minibossCount;
        this.trapCount = trapCount;
        this.masterMode = masterMode;
    }

    public String displayName() { return displayName; }
    public String shortName() { return shortName; }
    public int gridSize() { return gridSize; }
    public boolean isMasterMode() { return masterMode; }

    public Map<RoomType, Integer> specialRoomRequirements() {
        return Map.of(
                RoomType.PUZZLE, puzzleCount,
                RoomType.MINIBOSS, minibossCount,
                RoomType.TRAP, trapCount
        );
    }

    public static DungeonFloor fromString(String input) {
        if (input == null) return null;
        String s = input.trim().toUpperCase();

        for (DungeonFloor floor : values()) {
            if (floor.shortName.equalsIgnoreCase(s) || floor.name().equalsIgnoreCase(s)) {
                return floor;
            }
        }

        if (s.equals("0") || s.equals("ENTRANCE") || s.equals("E")) return ENTRANCE;

        try {
            int num = Integer.parseInt(s);
            if (num >= 1 && num <= 7) return values()[num];
        } catch (NumberFormatException ignored) {}

        return null;
    }
}
