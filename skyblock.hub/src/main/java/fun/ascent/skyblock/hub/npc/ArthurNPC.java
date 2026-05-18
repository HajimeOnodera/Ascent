package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record ArthurNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_arthur"; }
    @Override public String name() { return "<gold>Arthur"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "eyJ0aW1lc3RhbXAiOjE1MzY3NTkzMzY3NjQsInByb2ZpbGVJZCI6IjQ2N2NmOTRkY2UyYjQ1ZTY4YmRhNTJlNTUwMmU3M2U4IiwicHJvZmlsZU5hbWUiOiJMaWxpeWFfIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jYzJlODE5OTA2ZWIxNzk0MzliOGRkNTU1MTEzMmU1NGViNDcxNzNlMGY0NTg4NjFhZDJhOGM5Mzc5MTgzODkxIn19fQ==",
                "ph8zOGN/cLhHpRWWG6YOFEC4SyDqgCi5WjutZUXWdP1bBSUZLhYBMp8oMOS/wlnt5MIc8RW7Gy3feu26BpiFthpwYXEytK7pqRbszY+TvjlhA496oiEtWliu+MepMQPe4fODyNaRwfis5N2VvbzoKQU2DJ0PZvijrQufe6z7Rai2LesFQeZj3LC4UMqQkyJWkZ8VAMSvvldbMdvZ7osZSGgOVBYi97KyRvgAixxADBNlKpdGmJCqJ3GFEA02enI6VisrsT4Qe1lcSURUu9gUt4PwCGZBN3o/1AJV4R8/QUHIWz+3cx1oWy2MEmLBWIPO6SLqWdyXb+g3IPlQpZGaEINNXHhVsubGscB5fpQzqJCYgX/dHAGffCZLgihjB1eodSvQHV4l+ByQRzp1pf8hHMNBwPXwNleAPYaULeHrEsgpcc4KRzsYWxDn49foTDsUnwmjinZrU7w9G1a/lpMb2HrMDX0gu5P8hYHzfOLgMgm7lNOw9KLqoWVH32vK7aBrlCrEVBXHo2Iv5KR5esvjdNE5prdXj+JoGtLPod2KM5LFsUWLu9D3eRVN3FkxmKWAL7YVVxZNcpZmPRzZllx4H6rNAy9dUrwNkclkyJcACKrs0AOehRjWNfSfhd376MaTC/HegeTPvPQ7tppj/VOb0EbmNcXqL0+Oqp+9SoROZ8U="); }
    @Override public Pos position() { return new Pos(53.5, 72, -111.5, 55, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Greetings, adventurer! I am Arthur.", "I've been exploring these lands for ages.", "There's so much to discover if you know where to look.", "Perhaps one day you'll be as seasoned as I am!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Still exploring? Good! Never stop adventuring."); }
}
