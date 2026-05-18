package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record AnitaNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_anita"; }
    @Override public String name() { return "<gold>Anita"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTYwNDQxODU1MDQ5MCwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ0ZTlkMmE4Yzg4ZDBlZmQ3NTBlODUyNzMyYmEwNWZhM2YzMDJlMjA0ZTJiZGM4OGRhZDYwNWUyNTU3ZTJhNiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                "kgTWKoHcjvKRS1D7bWAlG6yIYm+5y6xLnq4j1q8Q1UyeNyQkjaDBis+AiHGyQQQ+iyKC2iu3XTv1nXkxV1oYRwWFqg/aabA3S/FIEwppEZBrkDl+2zWNqFmwfsppPZ5zbsJHBvDm/c9RSlaKLEzeSO66KEyJDllRfcJWIGTg1xk5FxCf3hsgZ0QPynp3v/m0Pv9bmUtd88iuHus7kC76G41DDIQYm4xUOXY6E3i7AyqNX+fhl0EaLGg8DGm4mpfdFx0HVvOf5njXauhkTKCKMg7+WLQcLEHtPnnL8wSHOiNuzk8+tYbah2KzKJHjXSulWE4o5BGLgbbowPnLB3Nknzi2fwNnjqKNaoU1EZj3YpgPgpL4W6+fx7rScrt3gsGEso/7bHJwBBJLoYNdUL3XzJwI/z7sbFFukB28tL4kJ4Bc9eOduVopuaueioNcAHhPfxVp5wSrvNPq6r/c+yDBNHgOgcd3vn5iwWRh7Ls6tzY3bwUDqM7RUjIEhGb4shqDdMUSaS90eLlieZG9jpBVstMwHh5K2LXjIDeGH9sD9hFQaZ7G0OPvtlErRyoEXxnS1DxLs6Zcn/A4sxjFJbs4aoXweM7xpO2DmhdxCGYvMlAcj9KcCPkcYkwN5EM9Ws3EQWURIV37QNOWcd51vDmdH7f3GI6PVjbalS3esM9vgX8="); }
    @Override public Pos position() { return new Pos(53.5, 73, -128.5, 45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello there! I'm Anita.", "I help organize the farming contests around here.", "Speak to Jacob if you want to participate!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking for farming contests? Talk to Jacob!"); }
}
