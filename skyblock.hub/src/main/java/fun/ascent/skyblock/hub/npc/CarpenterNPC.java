package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record CarpenterNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_carpenter"; }
    @Override public String name() { return "<gold>Carpenter"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "eyJ0aW1lc3RhbXAiOjE1NjM4MjIyNTY3OTQsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NjOGQyYTVhZjk3NWM1M2NmMDgxYjUzMTU2NjE0OGJhZTM2NjE0MTMxNGIzNWFhNTcyNmU5N2NjOTdlZjExOGIifX19",
                "XM0830BVKJKAtHvafFdC6JikuXLGj5A9alrAuF6s3IYw1zidmOI4EMsIaCEZpwuEApoGEOgorc1H5r5vo0YSeeXZNNPBTN6s7pG1mn8PqMxXGOEN4yXVnq13YEohZdNPFLA9lbDGnnGcWIncBb6ggzphtDfNDFA7hrqVEwzDcnej9RxfxmMtvH0k1fS8TKAsspclyjlparSQu3EdWQRFlW5RjFxGtrvTttvOJGUuJmMnIFbRZWfdwi1kPgrYa6/H34fRra57ktOOnCIz2iCs98KGi2kqQpOSozCwo7ykHdVHFRu2iBLE0gp2UmKp9aou6Kx/GyXAOd3HQJ5wMuJBfFurZAc47zvJDz1OP4lZtjNZjdxvlZAc14aWI7Guc/a/neeQH9B2XYJa/EdOcritdHLRh02AXbjKOkoBHs487YaWNXjMdx9eEJ64DxfEWFNa91g9776giQrkeKIove4A1HsiFl/5raST/N5u1/x1OF8z7WG7bzbQznMQVmTKYQKCz/f09EeEC0yP7nQFBevIkZ2CRC77TdN9EMOgQb8whe6MxzS9c1oaSA4lPjho/O1aVV5h0UVz7dHpZF0bsISdVfuZVc35xLOzUoGaYiegoo18GjmUtRxyfcQl1CVyhpZDt8JLau4562F1G7cPlxrW0mAt4WvENHCg6TFVx0O3RxM="); }
    @Override public Pos position() { return new Pos(-137.5, 74, -41.5, -11, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello! I'm the Carpenter.", "I build and sell furniture for your island.", "Bring me the right materials and I'll craft something special for you!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need some furniture? Bring me materials and I'll get to work!"); }
}
