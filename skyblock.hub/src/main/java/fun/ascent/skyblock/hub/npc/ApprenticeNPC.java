package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record ApprenticeNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_apprentice"; }
    @Override public String name() { return "<green>Apprentice"; }
    @Override public NpcType type() { return NpcType.VILLAGER; }
    @Override public Pos position() { return new Pos(-152.5, 63, -33.5, -90, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello! I'm the Blacksmith's apprentice.", "I can help with basic repairs and upgrades.", "One day I'll be as skilled as my master!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Still learning the trade! Can I help with anything?"); }
}
