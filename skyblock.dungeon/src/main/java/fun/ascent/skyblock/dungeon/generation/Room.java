package fun.ascent.skyblock.dungeon.generation;

import java.util.HashSet;
import java.util.Set;

public class Room {

    private final GridPoint position;
    private RoomType type;
    private RoomShape shape;
    private final Set<Room> connections = new HashSet<>();

    public Room(GridPoint position, RoomType type) {
        this.position = position;
        this.type = type;
    }

    public GridPoint position() {
        return position;
    }

    public RoomType type() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public RoomShape shape() {
        return shape;
    }

    public void setShape(RoomShape shape) {
        this.shape = shape;
    }

    public Set<Room> connections() {
        return connections;
    }

    public void connect(Room other) {
        connections.add(other);
    }

    public void disconnect(Room other) {
        connections.remove(other);
    }

    @Override
    public String toString() {
        return "Room{pos=" + position + ", type=" + type + "}";
    }
}
