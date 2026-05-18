package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record FarmerRigbyNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_farmer_rigby"; }
    @Override public String name() { return "<gold>Farmer Rigby"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NTkxMzM3MTY3MTEsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyMzJhOGU4NWE2NzUwODM2YzVkZmJjMzQwNGMzYWIzMTQ1MzQ5NjIwZjBiYzUyMDhkZWRiMTZiYjRjNDgzMCJ9fX0=", "vq4VZf30+S4JnGMAf4x5LiWe0tMtUsAAxWtfqd7ceEbDzg/JKihESAhTelDED93joK5tJxEWrE2reVxKmqolCKHKoLGrIkX5qn6zcmo+o9M4LHOcApOLxRqmfcbCtCB7KBObK+zwA1Z4P1wuol7AwEax//lssvBruhWaMpH4iOjM/0Ao+0E0to/QE3/oCrq0stgNtqB1ddbI/Vco5AVU26E2LEnyOgrDYTol9TeAyQc7uDqi/s51Z451GCnwuTWo3xPUPQ9quMi+1A7S10OnPOkKvNFyK4T7kqtmJFSCPcxHBXoeY2GGoqbwPvSt83Xl3hgLAlkYizYJBiHtN3ZrewxcVUkZbzxe96+0yHGDPT8oEFErRruHw6ZYY6rgujAeZHrQYAVGTz04XHbVSsi/9l0GwZTipbSpmFJepzAbExTGU6sAeCF3rwzphumhzY8rC+oIN5DBBr1LE4MbuZQaX0wlqW0dcIuDwZU+xeCxv9yJ5atYB6gndmDGOMZaf2pYWhuJNWUBssTj7JDM6VFSHsGR4BFP/KdDLGINlDQq2NZQaZC6T2BGTADFqMDM0JT2OE8PtmVxXhjJEK/rkGnMOqxvElmj+BuxcLlFNbW3CY01F5A9Zj92gcZXwswPORdfdq4/LGyKYd/Bh4nym5fzZvaIAZapVWk2bDhO9HPEkT8="); }
    @Override public Pos position() { return new Pos(61.5, 72, -147.5, -35, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Howdy! I'm Farmer Rigby.", "I've been farming these lands for years.", "If you want to learn about farming, you've come to the right place!", "Collect some Wheat and bring it back to me to get started."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "How's the farming going? Need any tips?"); }
}
