package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record SecuritySlothNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_security_sloth"; }
    @Override public String name() { return "<red>Security Sloth"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTY1MjkwNzI5ODg4MywKICAicHJvZmlsZUlkIiA6ICIwNDNkZWIzOGIyNjE0MTg1YTIzYzU4ZmI2YTc5ZWZkYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaXRhbFNpZ256MiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iNjI5OGRlZDZhOGM5ZTZjZGZiNWRiMGU1NGViMzUyOWY4YjA4ZmUwYjA3MDliMTA0NzFmZjQ2N2VmZjg0NjYxIgogICAgfQogIH0KfQ==", "ESu1W+i5foGb/+/4hIcVR7ArPgTMZBO2PGjAagT+2jRYbWs0ZZN/5Hs8KxEiBxpU7zaJh1gpt0j9IX0Ckrj/Cii9zoLS03PcoSXjnrdQG/ZkuONoj/2KRddy9Yu17sY2DXYmm9AkjkbnhOT10qwAFMxP6xMHgtVU+Tr/DIXEGrH4GucXkaSUo1irEbflzbA2xxKSSbc0P1fmebWtEitcPDG8SkWYONNkajH/v6X8bChe7udlb+6ECGiNbvegeWhHW+2e7YWzoqV+dg0a3BgvF/NFsWmO8/2imBk/illPo6QQQg2RiwlvH1+/0zCU++tSDucV4B8GrWU/MPe2tA6ZAxEb0uaV0Nm6DCpSL6hm2spvZNt+xkpvD0E3Qv0oENUcw0iEuArhzeO7zCplwZ3d8ce1VnSWEIhHI+JYW+wivnra6CCcnIyUQX5t5U8dDIxIGTLCB4J+JMO7JjArN+3jyywLF/s0/x4QCRfETuyMhzyS6bY7WNxX1XSg1/soMTcp+1p5p5kvyoRyGG9tnoGSdWtYXF0b9I3cjZqruzkZpHHP88PZN6IQrs4C300CrqBPdOAqY53Cu30a493lL0EhliYdoJGwOCGzes8arbAWuqdoC4GthaPB/pj6XRfjbelPtVuw0a9aJzFCDsUlUYskDoNXXuFfxv/knCRuDFKUYsI="); }
    @Override public Pos position() { return new Pos(10.5, 71, -15.5, 45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Downloading suspicious mods or visiting untrusted discord servers can put your account at risk. It is up to you to keep your account secure!", "Here are some helpful tips to keep your account more secure and avoid losing valuable progress or items."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Stay safe out there! Never share your account details with anyone."); }
}
