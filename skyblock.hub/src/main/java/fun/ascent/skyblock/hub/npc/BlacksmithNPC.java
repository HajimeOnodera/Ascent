package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record BlacksmithNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_blacksmith"; }
    @Override public String name() { return "<gold>Blacksmith"; }
    @Override public NpcType type() { return NpcType.VILLAGER; }
    @Override public Pos position() { return new Pos(10.5, 63, -126.5, 45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"I'm the town Blacksmith! I can reforge items for you, for a price.", "Reforging usually costs Coins, but since I'm feeling friendly I can reforge your first item for Coal x10.", "Go into the Mine to collect Coal, then come back to learn how to reforge items!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Want me to reforge something for you?", "Place an item and I'll add stat modifiers to it!"); }
}
