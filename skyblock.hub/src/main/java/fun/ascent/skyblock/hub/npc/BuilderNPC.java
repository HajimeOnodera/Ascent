package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record BuilderNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_builder"; }
    @Override public String name() { return "<gold>Builder"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "eyJ0aW1lc3RhbXAiOjE1NTA2Nzg1OTkwMDQsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M4Y2NkNGZkZjU4YjMwYWE4MzAxN2NmYTVmZWQ5NzcxOTZjMDI0YzhkMWEyNzYwMDRlOTA2OGU4ZWNiYjBiNzkifX19",
                "EzyAHb6TAVKVuO3R6cTt6eNJYXdU6C1fpPByuOEL/FUIIHqW5QpUnQLP7s3EjLhhzRagDi/eU/xGe09Ucsb7s6tSavn1jzfqwnmVG7C2FJ30ELl35y3pYbNKwmBl8I2fDY9pQrmfJbWRVhv9Gw8W4h8YRZARnW5PfVdsL1ddbTTsssaxapU8YTfUc88h2egnTD/bEHaqYEgfLBzjyMAyK9pDUIqe0NDmBJLbjPZXIVImRbMKanwgLRxmUkjGLONerb0HE8Kx6QoJEumoLOBrOLA5BJF7Jwghrv2d1W9S6hr89Ul6R8CnxQwHFfBMejccm0hLZein4DrKbiFHC8c/hs4jCoC4JT4rvOd/Yp8zNr3Y/dtUk5uTOguk/gYExI+p+1xc8HwTK3sK75LiFl+Ryu4LlKv5GBEznsnRHv1Ufeia3NeuVXDLi/W3zR8VG95Hf0lmKHdwJ/R9E56TxNYRh7wpma37ZTfEpUpKE1o7Z2m5c3jmDxLRdQg8dK1ZYMjlul36Qa8SXYTM4T+bdB1577M/44Vyde1NFVepYK0vRXDDNRal2LoDRM9buoTuN2taeP3pmt5C+pL554r7tWgOdCHUz51E9hwsOA9VCVxIA5eS+bgzSBLkWbXZYNo+zi/0bVr9OGdP2hCTJDsd0x7YEL2P7qribidVjnRLWWP8a2k="); }
    @Override public Pos position() { return new Pos(-8.5, 71, -61.5, 45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"If you build, they will come!", "I sell all sorts of building materials.", "Click me again to open the Builder Shop!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need building supplies? I've got everything you need!"); }
}
