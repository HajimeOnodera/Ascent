package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record LibrarianNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_librarian"; }
    @Override public String name() { return "<aqua>Librarian"; }
    @Override public NpcType type() { return NpcType.VILLAGER; }
    @Override public Pos position() { return new Pos(-68.5, 70, -79.5, -45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Greetings! Welcome to the Library!", "The Library is your one-stop shop for all things enchanting.", "Enchant items, purchase Enchanted Books, and more!", "You can enchant items by clicking any Enchanting Table.", "Enchanting costs experience levels - the more you spend, the better enchantments you receive."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking to enchant something? Use the Enchanting Tables nearby!"); }
}
