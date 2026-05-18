package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record JerryNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_jerry"; }
    @Override public String name() { return "<green>Jerry"; }
    @Override public NpcType type() { return NpcType.VILLAGER; }
    @Override public Pos position() { return new Pos(-33.5, 69, 7.5, -90, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"!", "...", "?"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "..."); }
}
