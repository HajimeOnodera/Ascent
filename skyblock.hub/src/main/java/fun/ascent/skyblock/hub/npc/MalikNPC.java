package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record MalikNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_malik"; }
    @Override public String name() { return "<gold>Malik"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1Nzg0MDkwNTYzODYsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FkMjY1YjAwNGI0MTAwNTJmZjE1YmMwOWFjOGM0OGU4ZWYwY2E5YWE2NmYzN2I0NDY0NDE3NTY2N2YxNGY2MDYifX19", "ncXMtxFxI7V2a5yENlI3/RwTv96VE7KoLUFDLmyI7fFqjTtU2toRUIV4NbrMMmkc85O0R19KC3h0HpTg8u2SxMrjbmvJQ0HhuP9+vqklcAoE2sFDpSf94AMXVpao8tLfHirvBQLROP8v6OWG2Kj6SNfbytpTzH+lOYBRXu+la9axEyU7ntzc+hfJ9ijPgs2IfAcL/6yQMe/RDKy39C7JC+oAW3oiBkEqxL5PqkgqHni7N4eY4Xf6iRj2Myh/e4iZ9zIj6iGF73s5c2oaqBUiqk7Up1cDd5GvR61Ptoongnz71fBOS19mHKC4EDOr2lWxMlJc1CKoA9tE2YVg/HwSF0aj4JmZPdESbmdckoxY61j6cihrU1hPQhAo0MM/gIZ+gnAnPIp4TcdmHYlTyRwcWuvh2/VmUZt9TYMJd0PnKS0a69OM8V2gW3kMf9z36Meos8fdGXQr1PGW/rrcwdOftZsMsx6sCAFG7TbWnEDpFsipDBX7tq8+CNY3y9ayiO3HHkgYDTEw1HikGfXu/Nza3s165oSV3SRO6W+5WwdB9uTEwLQB6mcl/cYT29vm6okNVIkKr5aK7SCynGIYgPOEbank0cKNtwZHQg3d9IWlPPydyhybFHgrEkYI/Ecp8gdE3yjBQye/o1032CJwaAgzYfwntn+6aAnr3K45k60oKQk="); }
    @Override public Pos position() { return new Pos(-80.5, 56, -119.5, -180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome to the Catacombs entrance.", "I guard this place from those who are not ready.", "Prove your worth before venturing below."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Are you ready to enter the Catacombs?"); }
}
