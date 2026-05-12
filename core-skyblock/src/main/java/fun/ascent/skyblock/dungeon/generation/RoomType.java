package fun.ascent.skyblock.dungeon.generation;

public enum RoomType {
    NORMAL,
    SPAWN,
    BLOOD,
    FAIRY,
    MINIBOSS,
    TRAP,
    PUZZLE;

    public boolean isSpecial() {
        return this == MINIBOSS || this == TRAP || this == PUZZLE;
    }
}
