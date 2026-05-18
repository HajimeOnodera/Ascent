package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record KarisNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_karis"; }
    @Override public String name() { return "<white>Karis"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTc1NTcwNTI3Njk1NywKICAicHJvZmlsZUlkIiA6ICIzOTg5OGFiODFmMjU0NmQxOGIyY2ExMTE1MDRkZGU1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICI4YjJjYTExMTUwNGRkZTUwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk0ZjkwNzdiMjY1N2U0ZDhlNzk5YmUxYTBmYmMxYmJmNzY2NDMyMTI4NjRmMTI1YWY0MDhmYTI5NDQ3OWVmNWEiCiAgICB9CiAgfQp9", "m7P7bZdhLoeiJWPqN5ySks53Im0gGo6zLqgg9j+94kGTOweRzKN3Hoc1qOvnMolfUbkGfYvAiBSGCRW3oa//0Pt4cg9qOzWY/p+M7lyiWAMz8gVmZ42ka93cuDCTa3wzksbZWSzsCQBFh7eF81fDG6f8Cs0MYSsJz6ocA+g9bM1jpJwPZksFtRBM3VlZ4cGyDgpyh39l5LTfzVeh4LQDTX0/mkLoHAuYDKJsZKWoEU8pQnxq0Q26WW8cPSC5i0w4Il01jdQWOPbiAdt+Xdp/hZoEdcfM7BGKGIA7yCvdO+1vC7sH4twNyjFwmRhNKPLKVTwfJbeCOhkLh67aak7H3J3Y7PDxhDm/vIMaximo/j/BXxzGsX5kwanOkTKaluCTbLGVr7vqMEUWDDPiy8nPr61mCAQICII3Vm+355AH45jTY1CDYdigT7TNgE/veXDl/1Zz91/KB+btShap1X8iktdQIb8hZZHMk1yboRzav9a1PF354lKybD3S2BXAcksQEyCBKfSNIiuceO+k+Nwf3EKqLqDbabSJ9x9q/TaOth0CoGhssGzBmjpW8p7XrWKhk43ZKPK2NO+6gWIcpAo9O1Gi6BT+bJH8UIhAwxbH+kZJBFJtLFicLYVQssG7w6Jes1UWv17Kt7xGKieAIMmazMRn68j3aoPbql03JFaPN+o="); }
    @Override public Pos position() { return new Pos(65.5, 81, -59.5, -15, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"The Abiphone Basic is the best model for beginners.", "Between you and me, if you ever want to upgrade it you should travel to the Crimson Isle.", "There's this guy named Udel there - he created the Abiphone. Forgot to patent it, though.", "He sells a whole bunch of high-end Abiphones with more features, you should check it out sometime!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need an Abiphone? I've got the basics covered!"); }
}
