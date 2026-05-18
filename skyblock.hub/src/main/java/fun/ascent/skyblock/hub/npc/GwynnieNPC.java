package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record GwynnieNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_gwynnie"; }
    @Override public String name() { return "<aqua>Gwynnie"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTc0MTEwMTMwMDUzMCwKICAicHJvZmlsZUlkIiA6ICIyMWUzNjdkNzI1Y2Y0ZTNiYjI2OTJjNGEzMDBhNGRlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJHZXlzZXJNQyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82NzQyZTkzNDFmNjQ3NzRjNWY5ZGE2Y2ZkNGE0NGNmMjQ5N2JjMTI0YjJkNDc0NzNhYzdiODUyNTFkNDg5Nzg3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=", "Jz7f2Q0r6YMxHlChNsefY1wbyjyarmBZbGpILURdbTWVFNETWr7c+an6uOpPQ89q1To8KnIkpL/QHF2U82FxMnrKXi0pVkocx4C3LI0hu8O/z4CXQyYUwqHk6fF3uazeHqhB2mreHrTLosFJv4H6QXoRcVatiGAvbqzh9o3nUHfrTkX0wFeQimsgr5nHdNDSQcgkIQ1X416hdW6aziPi3E7Zut7Jwn1a/RYNdFNfeue/rRoyjFUPpJZYV7PLry5dKioskaJoSD4toeZrhxSi6Z6nmRAodYmRJeiu4L8xrF8lHzAF0eDYajLewElf/HfDwFtiYDGN9LFoQnDvrs2B/itvkYOel8k+4K0lPfwXa9y1T8Kj4wdHZ1+KKW+AEkKCpAjKzIWZj9lT4rZ75mWV7xIqqvZvsONWCxj/kL1Rcj9QG7Q8GQOacjk/nwWgwfHHG2JbdUvBrI2k6D+9kr2msX7tGUq/iXTIrXHR3F+oyLATrHGitryrhJDFgiF4MJU4/hs4VZ08vfYtdvAv4ek89ZcN7+y0JWMl8qmY88ntv324alralO1Km59MnIFFhXY9PxaPH6LCUw1ZBPhPRlc+uS1MB6lSXAOZ8N3zhf5OqFNL/SoVus/tlex2AdR0joNUEBA465+MwAHDKw0ITHgRzz/DbZLT2/xb9eQ+j8myQPA="); }
    @Override public Pos position() { return new Pos(116.5, 71, -25.5, 135, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hi there! I'm Gwynnie.", "I love hanging out around the village.", "Come say hi anytime!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Hey again! Beautiful day, isn't it?"); }
}
