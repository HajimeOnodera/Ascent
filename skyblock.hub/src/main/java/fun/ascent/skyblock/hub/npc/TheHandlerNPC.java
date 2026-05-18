package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record TheHandlerNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_the_handler"; }
    @Override public String name() { return "<dark_purple>The Handler"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYyMTcxNjcyOTQ1MSwKICAicHJvZmlsZUlkIiA6ICJiNjM2OWQ0MzMwNTU0NGIzOWE5OTBhODYyNWY5MmEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCb2JpbmhvXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80YTNmZjg5N2QzMjg3YWY4MjBhMWYyNDAwNDM0MGUzYWY2MjU3ZGM1ZjhkYzBmOGQyZjlkYmViZGM1M2UzNjZiIgogICAgfQogIH0KfQ==", "WoxEznJjYEa8/EQR/hLwVMgP4yAz6n12GZ7ZEOLw7WOV0CHMJ+ipoNvERz9fTWg3X8X28hfv5mFKuK7XQ7WqKyEj0OchfHGdyVeAvgCmIkL5o4g0Ah8WCKNbCfBvdKHiKLDsjJv91DdVo3ZZao9UVEKg2r7E/Gsk/mdqGrXUwv73P1vA+PprdsL5AQnLPFUxUZN15pl5LkNMdLoQq2i/yPiFRIqDaqNuNzyvadXFO91ET+NhkLnNN5BssIE98G+xMgoeFLQspbubO5NImZkQjzrvXVJzScWAusFvW0x+BPGGmX6yCOGqHdEc8NdoOobZJ8NfAxxktaldFnacbqfWJu+sEXDHxk/gJhgr0EDE5/InLgKZXErDE+oCW/WzLg05F6zyAKHKfrj6eDlKwEVyqMZxEW4A7iMHzTdyfdkuvWt5LaHsPinVvjv2toZzT4Nx4nXARI3bIch9Sw42xtIRFnrFlXT9OtiJCyXUv6u9tDI2cC1Dd3TQYjDvKJxjwxsqpaPgixsRsXHb5pMHugfgccyYXpy4fpM6xdcIh2mqlZwXSvrsdTsqMirkkDZeSz71HhPmI2pwokghlH812kMWM/V4N1jfOguzdAcVb+M/eoP3WCBUCumjimlGtsd7egCrc/3mhHx2ViSeQ/n8yh9y0OcEqnqbu/j0maAwT8DbFBM="); }
    @Override public Pos position() { return new Pos(40.5, 72, 1.5, 90, 45); }
    @Override public String[] firstInteractionMessages() { return new String[]{"The Hex will not see you now.", "There is an absence of prosperity within you."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "The Hex does not have time for you."); }
}
