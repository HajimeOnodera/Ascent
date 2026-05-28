package fun.ascent.skyblock.dungeon.generation;

import java.util.ArrayList;
import java.util.List;

public class RoomShapeHandler {

    private final RoomShape shape;
    private final int rotation;
    private final List<Room> rooms = new ArrayList<>();

    public RoomShapeHandler(RoomShape shape, int rotation) {
        this.shape = shape;
        this.rotation = rotation;
    }

    public RoomShape shape() {
        return shape;
    }

    public int rotation() {
        return rotation;
    }

    public List<Room> rooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
}
