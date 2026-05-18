package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record TaylorNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_taylor"; }
    @Override public String name() { return "<aqua>Taylor"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NzMwNTQyNjQwODgsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE4Y2EwYzg5YmVmOGM0ZTdlNTIxYjExMzgyMWZmZGYwMGU3YjVlYjU1MmEzNzhmOTAzN2NiZWM2NTVkZDI3YzgiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==", "tOgjpsAATSBO9ocrq+5sLS9mPbma67wiFGNYXipzTl2BSJQQL0Ab55SW+ls1LiLL3ZeMEzLf0BCixHSmQ5Ewk+GQNsabvxSul3ILHVPZPNl5rdg3y5KTw/+tP5LjJh1kw4HpS6Gt468H8EuP26McbnmlD/l9BgnJaTN8HhuxnpaVugvt8nXYy0/IreIi4oKEL+c279hF3yopaQ/y/dhPshnXlgy8qh0SRbWodV5238DRDj7RuH53ZwSh4CH+3pmaIljcbnU5hjsZw5x69SzXjlRbcceaTgJJRg361IH25ig10b5XpMMWv7qMmAQmHtGztJT/XWyBIYd4KbLEC10bPl4eSPbd9Ke6RhG0XnlY9bIBPSLHakWkUYIm4Y2fsYrLxKFUDCg7tk0kJ+e1FejgJzLslRW8rj4HAGt7w6Px7dOW5q3fzUd4nzqCSyquNlluaKqbMyICvXoBLHqnTg0X4E5RRoZSXVUw37Bd5EGzkNck6VKDTBaMjFsguWY5zRqYGfKq8jQSKjIr9MuG+PYS8n2lqvEeHKHrHfIqgcKGP33dnOqmKTb7pwtdWkPpH/4DzzKNqVIlUQZdYolEy2wTDCKnWHK+Vj3C3IPyYjpUKttNuRH8QcUf6flNmpVDJBg9eJfjSpzo5xv/MxN4gzt4gWhyMPO5qCxjR8YTjar0a5A="); }
    @Override public Pos position() { return new Pos(37.5, 74, -39.5, 135, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hey there! I'm Taylor, the tailor!", "I can customize your armor with all sorts of colors and styles!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking for a new look? I can help with that!"); }
}
