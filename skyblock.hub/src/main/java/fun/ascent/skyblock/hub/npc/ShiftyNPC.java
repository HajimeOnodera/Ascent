package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record ShiftyNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_shifty"; }
    @Override public String name() { return "Shifty"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NzUyOTg4MzI3NzcsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZjMDFiMjEzNTc0ZjYzMDYxNmRkMDBiN2EzYmM2MTc3NGZkN2Q0NjcxMzY0NzZiZDdiN2E5OWVjMjVlZTI3NTcifX19", "phxW0auuDIf8/A99q8l6i6VYwIadA+TXJMNcMBVdLK7ItptNJH6TAHjBU5tzXJb2lZ4+4zLY5Nl2a78dpXX8lCegLNUhO0rvFpnTOJIR4mXxAxi0frUmaoiUh5ElK+XhSU6GBzvqgf4K0V+HAfFBDWqYP/IcQdJYr1YDftfEuFkmcJuTCbiJ0kVc+6Hy09adBrcYJnDtiYpF4m3OG2vieXUyIUcB2UeyOvAQuVNQ4HvyhZHqXg93WmZVA5JZRLefOryU9xF/nw367a0rTxiANUkGoQ7kdqJ4MDD/v6yNfin5aLFtEu3tVqMc2QPOpMtjn5Mm7IAOXBVB/LuICZ+EW9LDozV/tPqlGs5cbGWzvKhla/fUJkIhhcjtgHF5NwCr88hlo/TCiI0PPXNAG/2woa2eYZtL6T4g5y5DeTqo/vbZrgedJV7F+gvc3v3BvGeVmQUlu+u1I5T0gR/U2nqCH1RTDx/8zJO0luOmGBCF6Py7W1vhwAEUHNuh/EQyBPkyGHbiupvitlYleIvXvn0VzgSNts3fvsh4lNYXfRcmzLb7kTilh65DIyg9wsrDQ+YBVochFSm2c2FzhdxklhU4iPASz+rmfo+AaL8iKfMKgva4GwAOWYfq294B/avB3bgUcOrmmFopjANr5sx5V1VXaQXMzx7DTw0eKXEF+PA5IJ4="); }
    @Override public Pos position() { return new Pos(114.5, 73, 175, -180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Psst... over here.", "I deal in items that are... hard to find elsewhere."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking for something special?"); }
}
