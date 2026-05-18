package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record SalesmanNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_salesman"; }
    @Override public String name() { return "<aqua>Salesman"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NDUxNTQ2NzY3MjEsInByb2ZpbGVJZCI6ImRiYTJmMTE1NmNjMjQwMWJhOWU5YjRkMjdmN2M4OTdkIiwicHJvZmlsZU5hbWUiOiJjb2RlbmFtZV9CIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NTJiYjQ4YWYyZjU3ZDZhYzljOGU4NzVjMGM5ODc3YTM5NTNmMDQ4MDgxN2QyODQxZTU4NmU3NDdjODdjZTBjIn19fQ==", "N1ypz7pBZlbwToXhi3vBN1naQCOPXQq9l3Ps2V5FEsLlIgXQTe9AUKGYsHxmxlu6rinMX2euQn1eyv3eS759C6LrqGoMdBYLJbLClyBy8VfaDOZjaCUG6f6wmRNaGcoce0Cf7wB9QC+tgutuy1QV6AtefdpgMQPYCDXLLzbjqwUyIJ/sLzViiecZXN8g13dd8f4LipRXCPV10xtO8Kq3eXLZeyOj6r4HE+R6UVQ+QIEvG857XmnH1o0ehmwRBg/0ftbgsVPgprfu8M0LgYb6VVswpMou++jXhUDzvHKzOICVNsKMC3BLouXzsVqSXhFP081kkw/cqVaHgtMXbRTxGIfIoPg+Q6sB53fqGe6U04M3YpjtlQO+SvmRAwpU2Zcqu39KhjBs0z58uawx1PNKo6dJkRgP1RpIkkPCJTQukNPrICxvy7o+s22DA5y7T5y8Hf4G5LbQ7q1stqiflttZ8RvHImVXL4biw9EACxGcqy04CdEXTh36lm2Il/r85cLTK2D94z18xFuQ4RYP1DVkmoDW1me5qOqNYhMy9hC9/ZW6QkhMJ9gmpDDqxpAeIDdFc3w1YNkS527smpLimdjY3gbxxl2AtGU8ZLo72yFCo8s3Ox8vi8TiU4s4HpMtGaL8bS3Sfr7z+aB4pJTmQwY/Ll4dLyzK7aLPWdCoiEQbQt0="); }
    @Override public Pos position() { return new Pos(-9.5, 71, -15.5, 0, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Thank you for supporting the server!", "If you want to support us, check out our store!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Thank you for your support!"); }
}
