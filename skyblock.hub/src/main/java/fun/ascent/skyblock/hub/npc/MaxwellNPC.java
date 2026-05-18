package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record MaxwellNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_maxwell"; }
    @Override public String name() { return "<aqua>Maxwell"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTY0OTM1MTIyNzU1NywKICAicHJvZmlsZUlkIiA6ICIwNjNhMTc2Y2RkMTU0ODRiYjU1MjRhNjQyMGM1YjdhNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJkYXZpcGF0dXJ5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQ0Y2RmNGE4NDJjMWRjZTNhZDJjZmFlNDAzNjZlNTM5ZTlmNGYwNTdhMzliZGI3NzdlM2MxOWYyZWE2ODZlODIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "CALy0nHX/3+BBjSHm3DEfAUEkDJKHU+jgvi3iS9Z3SCxwTGUaP9DiL0Izyc2MJth3B4Qxe9CwYzJ2l5KFBpHu52KTGWyv/cwyJn4GxZ+rDeOkCqeK1aWN7hzOQsZH1kYQ5Xf/KgeF0oIhOgCpexYtIUsU+bERMcl4QCVeogZ6Ewzvyux3tdLTw/TjYDpuAgW+594QtcjKYKE1lFb51DrHxl/c38nddaAPi8Ss4rPe/O3gUp5NVLNnaJ3+hZ8rsOhkEk3YwFCEBLMZ2xlAFm3s8yyILCzT4huZ8lqmZDj3xUM5bFKwl+iGdjfvZyrZUDNr/+zf2ZwhGllzIfttWm6y8xwzwv6JIqu/XTo+taohn7MPZGmJVt0nkrpmrXNBeZAK/g6pvJzNvqiNugXIaokcLsNYzTSKsV++/qbz7ZIaWqsImB7efH6/7fcz30JUga+wdnOMauISONzjvsQOzcGKHTHpaNP402IBjVa3hu0lIDxLwfhm4JYa/RfNGdQwJV8L3G7Vu3EdGMmu28MhfRtmge8CTI5cCf5Jt40HSr4rlTPM+PG2uq6iruOCoK1vtNKbqJubcuNO0AYWfXInlHB/NzrwZ9blemPe4/Lmack8skjrsTxzoM/X6Ze3cBIvOCLtUN8s+v0V4RAkyiA38eR4Z6cLNqVLiGbc3zCTBic34s="); }
    @Override public Pos position() { return new Pos(-66.5, 70, -66.5, -90, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Greetings! I am Maxwell, keeper of the Accessory Bag.", "I can upgrade your Accessory Bag to hold more items.", "The more accessories you can carry, the more powerful you become!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need more space for your accessories? I can help with that."); }
}
