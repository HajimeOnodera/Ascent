package fun.ascent.skyblock.dungeon.generation;

import fun.ascent.skyblock.dungeon.DungeonFloor;

import java.util.Map;

public record DungeonConfig(DungeonFloor floor, int gridSize, Map<RoomType, Integer> specialRoomRequirements) {

    public DungeonConfig(DungeonFloor floor) {
        this(floor, floor.gridSize(), floor.specialRoomRequirements());
    }

    public static DungeonConfig defaultConfig() {
        return new DungeonConfig(DungeonFloor.FLOOR_7);
    }
}
