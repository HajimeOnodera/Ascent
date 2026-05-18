package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record JaxNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_jax"; }
    @Override public String name() { return "<white>Jax"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYyNTcyODU3NTQyNiwKICAicHJvZmlsZUlkIiA6ICI0NWY3YTJlNjE3ODE0YjJjODAwODM5MmRmN2IzNWY0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfSnVzdERvSXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRiODUwMzIxNDYxMDZiMTlmMGJlZDA2Y2FhMDNhYjk5OWYwODA3OTY3NjgwNGU2ZTBkZjJmZjFkMjhlMmNiMSIKICAgIH0KICB9Cn0=", "WkNbkv4F5IKaJsWVscT7vxmBlYqJZvsyobQvQKRbgsCxNbW+nN0B3lWLwnk2CNnlMkUvYZfqjCuiOKH/IRbP9IKTRxw2yU8eel8a7IWuQtB4UlNHLVfgjmbC5EPdBRKKF0QGJcBYLIeKOXKAoVS12RA9CsNl0k1PLRZ0+vHA+M208djgySS8K+DN/JluZchF5jx0io9KxyygLZToAsD5/DLB1pkiPhaqYX6COrBg3malhHBg2xwP3KSXHAmVWx2HUTByZoJOnsGy9GbGVBd7bFOKO3Pp8+3PqxBVg7vL2Hy69ZjqiwGtvzrkGc1P+RI92ZwGkXpe+vi8fmF+E2ZleOt0PpoYbGYRI0GehCVRiPMJo206Axsh385AHCvjNClGhO1vyBLqDonYkkQJQKvH7BbjVeQ0U5T8d6f8PibHwt0UWum8s/IG0w8Lglj5usMBNS0RdKPDSeBRIt2f0cMaLRvyI6KORMARQ8dsPs/JL8qkzvOjIBIzYWkLB6IQkW2f1IdVOCbb7rjk3lE1GdY1VKEnt5scznBXVIm40hWJvly4e1kQSV8mXlqPavSpHR7nsrhaMd/g2TZplwUPdRIE97ZqM0fHdpDds7rfL0Z+LoleKUGTxib3VBwILQ9///rfljL4f5fl94fUdFjLuMAm1x7jwxuOvq3jX76dAMiJlB0="); }
    @Override public Pos position() { return new Pos(-40.5, 69, -92.5, 35, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello! What brings you to my workshop? I forge the newest and most powerful arrows in all of SkyBlock!", "If you can prove to me you're a real archer I'll forge you arrows whenever you need them.", "Ready to test your skills? Step on the pressure plate and all the targets will light up, if you can shoot them all in 25 seconds, I'll know you're the real deal."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need some arrows forged? I'm your guy! Let me know what you need."); }
}
