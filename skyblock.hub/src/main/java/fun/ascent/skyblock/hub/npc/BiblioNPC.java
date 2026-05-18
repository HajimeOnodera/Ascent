package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record BiblioNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_biblio"; }
    @Override public String name() { return "<green>Biblio"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "eyJ0aW1lc3RhbXAiOjE1NzU4Mzk4MDQwMTQsInByb2ZpbGVJZCI6IjkxOGEwMjk1NTlkZDRjZTZiMTZmN2E1ZDUzZWZiNDEyIiwicHJvZmlsZU5hbWUiOiJCZWV2ZWxvcGVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83MDE5Mjg5MDU1ODkwYzEyMTFhY2QzNzI1YTcwZjA1MTU1ZTZlYzE2ZWE4MzI2YzM3ZjQxYjIxNjEyNjYxZGI4In19fQ==",
                "vV2/3ouX5+qRmFZzCRTz7aAniDmjJjictDu0/9krwI3x3FJdyL2eG/5GpEE2SKCY7J/znjKXs9oWaRvspM2DW0KVRspVlJYtz8hagJjRXRBM2mzIjXTzzVXNi+aHCzz3AARxd+9cWYOcEKKqh6snYSX2uJNBVz9cgfelykktL+TccVk2cbdLg8VMkZe24rXKQioOEc0QTYM0v7LkLODEvuWRL439j6Sa4sfq4FPqJzPtRUgjexMQtoNkCQBJdG266rnzXJjsh8Mgx8NChozmtY3tMyi8c4s1mAE5YQlI5P3sWHU5kOuAq2zpftI6wkSeDdMoxIIbWk+MINsKH4zMfiXK2u0FxViU654CWEN68RcXOpjVGK8ca+1quTeyHI9wu7FCY0Jm1aCo4MxJNhdWD8IjvhdC1pzcHpca+t7xAE7/0alWpMk0SkQSbHp+OqmGju29FNcguYsPQzAHKUB37XBANMqPKbrPdbKAL687kQy1FBri00V2Z83iYsq2vquUo1oqwzQ2UVY6ZrCamcr8ATvGpDMXJt4DSkUhHk3gP4lny3gL30n5dXyNxiJTn8UPqkDgBRRgAAKxYnhlzwku10/3gMT9N45ZWp/SokilGah5lG/0ZHH57EyTevOSD/q2i3y4qnHi2LU19HTvKDopwXh8mIn13O46zgh55izaVqg="); }
    @Override public Pos position() { return new Pos(8.5, 79, 10.5, 75, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Ah, a fellow seeker of knowledge!", "I collect rare books from across the lands.", "If you find any unusual tomes, bring them to me!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Found any rare books lately?"); }
}
