package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record ShaniaNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_shania"; }
    @Override public String name() { return "Shania"; }
    @Override public NpcSkin skin() { return new NpcSkin("", ""); }
    @Override public Pos position() { return new Pos(59.5, 72, -144.5, 0, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Moooooo!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Moooooo!"); }
}
