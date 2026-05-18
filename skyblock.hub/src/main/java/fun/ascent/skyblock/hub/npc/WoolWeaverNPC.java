package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record WoolWeaverNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_wool_weaver"; }
    @Override public String name() { return "Wool Weaver"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTU5MTQ0MTQ2NzIxNCwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRlNTg3YzA4YjM0MDNkNzYxY2RhZmU1NGY0ZDUzMGNiZTE0YjRjNWMyYTAxZTg3ZWExOGFmMTFkMGUwNGMyMCIKICAgIH0KICB9Cn0=", "P2Gc0WgQ/6RuU3zP8YC5j6sfWtTlVPZOgpCPaW/ecSREesqHZ+9RCIISo34wlOUHRd985GRM6YpM2ZEwzBuJAIbqT6JYKB3/9As1M9fgCDTAqE60dKsTCflD9zNYW4FZFkvSt+OHTqBvfDVqMPtV7Kq3Rt3KZGt3bLsChRj1I1DLeR8plQPasWyYPzHj2AdTIKqLHTYqB8Mxq0EKlI+V5VUvo6urNkhm8ReFiA8aBXSbfWz98gVkDYp1GRY3z75DH6jn5n/xkGbRbeWPv6b3x0hZYoEyb1Na0JGoVy8RCxiDI6L7ibxK4TOdZqdvhVAdVqWgLgEzeocmb1uAKp5WQEoCQ1rClwjI1gIeLo03ByMg6ddFu756U2yTI6t8L7HW1jKhfmOykM1IvABjL1/b+lQ/I/3XMM6A61YKuh/Eh91WNd2gwuubYl6YyIlvOskAkEQ6iqm8yxnJQ1lziy343bC7lEK7QaJ/XdDJvA8MQN8qCxc53ZnnQb4FZgPf6IslE6gR2rolb3n96skPVlbfwd99hY5jwvptN1WF0l5ouMKEgk+Lku5fp4jKSs1iAr0Q5vJ4TXDhdEevsHZdf0na3opClk5+MseEP1g+gn5Vc/4c7IAe2KJyrp6F7PF4NDGwci5NSWhLGAMQaZyhrcTXRuVnhDaxgF+bLwsQOUagC7c="); }
    @Override public Pos position() { return new Pos(-17.5, 71, -57.5, -135, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"If wool shrinks when you wash it...", "...why don't sheep get smaller when it rains?"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking for some colorful wool? I've got plenty!"); }
}
