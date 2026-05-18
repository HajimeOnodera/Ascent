package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record SeymourNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_seymour"; }
    @Override public String name() { return "Seymour"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NzMwNTQ2Njg5NzUsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M4YWFjNGU2MzEzOThjMGY0N2ZlODU2MTVlN2Y0OTIxNTUwMDA0MGJiOTA4MTlkNTY4ZTgyNGFjZjk0YmZhNGUifX19", "sotvNUxqLU3jo0VNPB7W4LhOGzNg1zTftkI9ApzQxXVzcxi7jzCZbAmDgUsHwgsLBag2L20Ma1JCCwL7mC5nbZxqbnF77FY8+PP/WhaFteivTV+p2NEg/LTBtRihKfNlP2Xj+mypFv+QskjsZ02e5t5F5z7AkXEQ+jZe2UnPbashtCg6yrxcdvnWXEi7AeCppfcwFxBubmOS+gnqQiKJ1DjED4s8Ok4L18M/xcvjhicHypghhls7+GUrCrDz+gzghQO9/fiFd4dsrajWEET03afDNr9/o7V1R7PMmESZ/GR98CaJQ1Oezpl1v9Qqtg9UapIj49bFsLjGD/AcjnuzjOBa63Jj+3TwC2mU7VnkFGw3Ez0CemTKJJEGX+qDllqSSAVV5upBhzH6sd8BdxcyjhGKPEATbAXZVKKYGPciU4F/b/XkTdVr8egfg3bkSrI6eTK/juIeCVxcr5u4UGX5j+T9Ug8DisdT6SerVn9yEjttyKDILYn6Rmx+syS82fQ/JWJr881ZlD2lb1FWAx88X6ZOX2uQ7SLHTjc1qI0VGeOttokJpdEFf17FV6rX18kY6yAIHDZ908gQ3XSx4uEchL2bdZrJZYAi6xHnh3wP3rjkLIvooYxJ2AACkMWYoU+vHV3dXvsctlTaJaNJLO+zv+SWuXY0SpQYQ8REnI1f/Ug="); }
    @Override public Pos position() { return new Pos(29, 66, -40, -21, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Looking to buy something fancy?"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Welcome back! Looking for something special today?"); }
}
