package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record LumberMerchantNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_lumber_merchant"; }
    @Override public String name() { return "<dark_green>Lumber Merchant"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NjAxOTQ0MjU4MDEsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdjNGYxYmVmYTAyYjA5ZmEzNDlmMDI5ODZmYjRmNGI1MjBjYjgxYTBjNTJmZmJiNWJkZGQwNzQwYjBmMDMwZjUifX19", "rK6rdM1BcPwjg+cHMT5MJVLaztD/3lH4GZLfzVbD9Rzof9Y4ET9zu/qpgI3XkBYMn0lCcpd/QHfpi8yhlfxpuvdd6hcnzLnKASAikYEVb2yP1HzE/9ScvDNUbnyqBX1DBItwCHdpQyeOxYxOV1yN5x92t3C+7aAG5XORcZhcdYEhwwT1/c+8LSby0diJ7+QFEi1qJNtHzABJmlSMJ7pEH0w7GVBGhY2knPz2cP07CipsrpRz+luypRbMsIubRg2Mx/0sydeNK3QmQVzWJaQjBTI/5VfAgGQwu6H0wEONNXOMmeSqM4SxIqh/KrnUKbifFVCLy3bqR0nRdSa15clRteD6P2LFl1QM8zB9eCR/6269VQPNocPCCImPQR/P2tS5uHpsyoxkfkrMR8nR01f4Wlu1t9k9nNo9rifnSOOFl5qWjxodwKUE6jcNL0eEblFKJxAOUkP/4dwmGYxTffYLzv/RbwTCJvUKRzSw4C60lsmLabVfqokGAPsfo7uF11AqaJ2fJPKDSURxtADNJrsk9PpfH9jJS/ro/bujgA3r3G9xPASknohbi6hAwuLxW7PTEGff8PBAEMVNuq3Bfj0oZlIaDDvZy+lIhzHeuX7NZ451KYjzbk1KOzGKW0B1gMd6P7DqZdPlK7+1/vcqvY+hoDY/Wjciuu29HZsdDR86krc="); }
    @Override public Pos position() { return new Pos(-125, 73, -42.5, 0, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Buy and sell wood and axes with me!", "Click me again to open the Lumberjack Shop!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need wood or axes? I've got you covered! Click to open my shop."); }
}
