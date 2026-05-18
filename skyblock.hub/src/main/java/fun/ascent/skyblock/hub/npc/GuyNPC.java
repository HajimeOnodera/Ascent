package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record GuyNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_guy"; }
    @Override public String name() { return "<green>Guy"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NzUzMDMzNzQ3NjcsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U5ZTIzYmU3ZjA0NTU2ZmEzMzM1MWE0Yzc3MWEzZjA1ZjRhNmQyN2RlNDEzYTM2ZDAyMzBjNjFmNzE2OTg3OTkifX19", "Wt6So7YJ+kxD3vUeu5scgT4SjDb1SwK90lD02L8+PlKyr7v0af8H0DRMt3aF9EEJBZdwz64jEzDEVrG0ZakyM8DuA554pwRp4G3jjQ+uYAxmYK7oaLPuhF46IIhhu5efNRgS9e5NaR+frPMFP+UuR+MthVL3G2BHfwLpUthAx/lEV0CyVYNLvs8cUnGnnl/nm91UOzQjk054RRdDjUS3O8dt7BWo2yykACo/cKgyzhNvy4b/cJg8aB3f/VMQYPi4x/Yuza7OqQWWa2TPFvYk8l4eOqz87qwkAE0yQoBcNPgyUT6R/uJpHAwWonMUkkRzFf+jO1HT79ltrxD1ptghiYmpULZjIIaPL46aqZ78c3N3e4/YezFPl02hKq61GQ7QjwgV7gBBijZHGhygUrv9QiYw6Lxgz3jIF1vE0pYRALAMJHw14EkS/3Oduh0J/ttJahBH+GkhJe0XhLKfvjcEvhqVFBJItbb6cJFmueMWT+G5aYWL0Yq9YvrQVA2KGDaaZ2XLXqNy5BFvJ4NFWkbHVMOFy0PE7jQ7bSr53Bhg8kwXJrcIHBNNNtYk+oD9EiKLgWiyV7//GA9m9nH8HIF6841YZR4MGjJ7POav85Wlx2nXR6TRbTgduImkYZTa5lgvmQL2gF+m/e3cLq3AR4GB35YMVapXgqWXnYKdQoOO5+k="); }
    @Override public Pos position() { return new Pos(51.5, 78, 20.5, -180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hey! I'm Guy.", "Just a regular guy hanging out here.", "Nothing special about me, really!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Still just hanging out here. Nothing to report!"); }
}
