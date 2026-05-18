package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record FishermanGeraldNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_fisherman_gerald"; }
    @Override public String name() { return "<blue>Fisherman Gerald"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NTk1NzQ0MjU3OTEsInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hNjc1ZjA3MWVmYjBjZjI3YmYxNjA4MWUzZjgyZjliNWY4YWU4OGVjYTllZTk1MjNiNjIxNmU2MTdmNmY0NWM5In19fQ==", "oJ24ajDV0/I3NFdHBh7D71v+jboQFJlaFxxu47bWeSmUXhLl6z1Vk9aksUE8qqTNs9EVUWFpSjAe7i/w57nkh4AAH+GvplyzZANEhHf9SJhBdwjCpIDVJZ453hs9xMYbyvp4KiZkia+jbLKrQfQOFOa9aWt1mmhhOneNgzx4it5Bo1qzDoPvbgFu5uL7rbyzdOl9ZW5wEobb2Ns4TbqPdT+NjZurw7rkpRpdLhAbHZoD2NEw0BX3VHTvtlY8zh14//YV7Vo5+xUUWGTrt0UjudIxDdJI8R9ZiWgCgl9N1ElzokFh/h0aHg0vL0QSG1Y5bHY5ea+E2+3tLDiEvQO7Soh/VV1/yySjmkt/JbUiEFmCv6vkjm4bgbAZAm42GxlvkAyFpZoFZijmCaw8ObJivZlwJNUjY4D0PBEm4rnVSVFjjWBGaGXyFG2/KtUL8nYZE81ABqrL3xSHFeEUIBePNsBq84eI88aNGYCjU9Ct4bfAhbdZMWM84PzBqAa5jH6NNqb/5aV2jmEp0OcoF80W+pcaR/uNPOo9Gjy5HkUYMpLJ775SqC//m/Rrh7RypdNcVUIanmUqP+hP7oz1SI3L6glv4+CGlGprr67QHP9d9PcZzVgF1YHOfOKW4muqJNjgpKYFZRiz4yWmOdURKomqUuz4tyKDxu0drx5eHfQ+3mw="); }
    @Override public Pos position() { return new Pos(118.5, 71, -32.5, 145, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Ahoy! I'm Fisherman Gerald.", "I've been fishing these waters for decades.", "Grab a rod and try your luck! The Fishing Skill unlocks great rewards."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Caught anything good today?"); }
}
