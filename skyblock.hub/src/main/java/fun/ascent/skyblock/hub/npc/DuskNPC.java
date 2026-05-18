package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record DuskNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_dusk"; }
    @Override public String name() { return "<dark_purple>Dusk"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NjMzMTc0ODQ5NzQsInByb2ZpbGVJZCI6IjllOGI4NzQ3YTMxNzQ0ZTY4YTQ4NzEzMzQwM2I0ZDM1IiwicHJvZmlsZU5hbWUiOiJFdmlsRGN0ciIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2NhODVjMTU2M2JmNTY5ZDg4YmY3YmMzNzVjYmM4MjBkN2Q0YTczYzkyYWFmN2Q5NDc5YWY1ZWY1MjU1ZDA0MCJ9fX0=", "dRKIQEk2uly8QEPNSrKsJm+e9qN4+XUL149+U3jlpWClh/H5YdZAjHDazK6WkqRSHHU4UgD0zcf4tCcHDPXr4T/mV2iOYVGEQPUJAcjlIJZVy/Yl0UjmQl3aCpyejljvNkcE2JQotALQGb/Lce2H9ILqU4iae7JBZbHCA/8kO3AXmaeaYucxuee8VQU66V9G1m5xH+E3370K5X2UgNj+gkdxzhlxf64SK6dtDXJF/1L5pnhqQziYRbRHw+Wi90+WS+LME760fAThCDJU+cL3ANRekFSiKV62RaIYtLGvTj9e1TMmVFFg5J+pY0Qa62vipW+v453diS15XmzHsxAG/QxudjmqooCbLVgkrMslTsd4nb4xXPDaHv3tGh9D8RKlr9jTT3T59aAqtxFAlA0gJAILI+LhfJZi7Pzm39S0e9VMbEoMOWfMMfwbKT3DlLSokjtVMAnzjehd1jHtp/FsWBNDcA9PwbJl5ap+/UFCVgTakyIUKu7Nje7F+1426xz1+FpAlaDhirJMsxtFuhOdoZq91QvdO+75o+PcBWc0PU+BOLgqCljBwgp7CBnExpif+PCBaAPJai6Gfpzpw0RnFsfnbDTHkKFIff/Us6l6dCt/vixvJpdIgPnuqLMt4SzmOO1qErzz8a3mXVLo7a8ShDbMhNkqtj0EyeXQDL9WD6I="); }
    @Override public Pos position() { return new Pos(20.5, 63, -135.5, 45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Greetings, traveler.", "I study the ancient art of Runes. Place a rune on the Rune Pedestal to apply it to your equipment."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need to apply a Rune? Use the Pedestal nearby."); }
}
