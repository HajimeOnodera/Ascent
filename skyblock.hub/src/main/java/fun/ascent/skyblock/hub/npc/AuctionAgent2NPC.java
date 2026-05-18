package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record AuctionAgent2NPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_auction_agent_2"; }
    @Override public String name() { return "<yellow>Auction Agent"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTU5NzMwNTM5MDExMSwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg2MTQyNDFiOTgwMzE5YzAyZjVlZTNhZTFhN2ZjN2ViZjhiM2ZkZDUzMDFlZDNkNGUyMTU5YTgwZGFlMWQyYyIKICAgIH0KICB9Cn0=",
                "uBZtSbevRudSrX4uG3hxrRKGqULSiiAW80AJg7RfrK8/I7aJnDMTZ7z4Sz7ck2i2dugnBL3CTs0RyK3QGRVC20yx4YijE50AenwGw65pNHed2l54eThuC01xcPsPM1LQ/qBxldHYy+iLMSDMxf4gYszD50fWLGU2H1tfi9CWQFoea3SngFpUvozICfbCe7ZzlhybE15XmlkAUFR293tYTP6CxA8hIjuisNnW5LE1m1EHNH8K0vR87T4OrMJ4tXKFRlN362v6gUkXC3jPEzAzVAln3oBfUeSvgYne0nyYxFZjTg3+xiP4fd3ULzDIouEBer0VUOB1CIPeoGxK72UviDuXSdOoQonQOdadn4nj2i7MsrIrVzYYz1sWTLUsdfNilZb8rH5T/Z4MvKmktxhe0eeN19NnEWdEwgkZSqi7ivoACWEdFRqr62d3Bkf9kqk/j/cz9Z20VBr57THOFRgSNIznA7eV7Mhdao/G3QGPmbtNCWjhmDQ+UJ7ADbIN1M+p/h7vi/z9nYBpb/ei9U7xPfVKslyY2hgv+eH4dYO3whwWhfGhsjgbOA1Ibp9dln9t612adJh2XSlaJH7AFr2HS40tG6HOWhwxghJl+qScVdoyJ/fm4bfubZYrR3E8QDvQtYqAEuWmLFOiXtOg3SJgi/BfjjXHGfV3AydH0PFAI3A="); }
    @Override public Pos position() { return new Pos(-33.5, 73, -7.5, -180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome to the Auction House!", "I can help you browse and manage your auctions.", "Place bids on items or sell your own!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking to buy or sell? The Auction House is at your service!"); }
}
