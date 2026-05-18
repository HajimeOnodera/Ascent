package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record MadRedstoneEngineerNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_mad_redstone_engineer"; }
    @Override public String name() { return "<red>Mad Redstone Engineer"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYwMjY0ODI3Mzg3OCwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWMzNjM5NGQ3NzRhZGU1OWMyNTYxODQ5NGQwNTE1ZDkwZjJiZDJmMmI0NDcyYmEzYzAzOGQ3NTA5NTNmOWE0MyIKICAgIH0KICB9Cn0=", "voTLQRDa5VvAd3n8vk9A0q2SdvmMmsWnIOLM9LFsWwebfAfZskwvaotuJQ6cv1PEfPc62pmZXPjd73R+BNZOaibVBcFqnFSZ4NBDoj8H8VVSSjz79iRIqjUTXAbw7yxZPQ+icMlaQ2EMgUVHB9z6NP6ly6uptpOJEMNRXqwf+rqPjUQbjA1SvRWY7njcz5zqExbvTX05gPU2Z5mC4ocHm3iWTjWeHwf0zEcYBYurOZ8RU8FR65rIDQv9SZ4Qx+DfffUtN892SRU2cdYXtXCizUyEe7g1vt7c+yZ0Es+1voJkgz4UUA9/0irZJ3FqCF+wL9sjG7zg5cog8jRrMynVYWM6ODcf9Q2KvaO7ka66HuFboJdo2e9MDoGNcOdWPV9nWJR05znu0hQCD2sc7qTnU72CLQjXgasPlF45JkIcEeqZ0i85uBHk1lhuJVvWFfFGlFfQ7KcxDiIw/Bd2QZS34BaWmJYmYPh65xiHngN6JQoQllscll72Rf4/9rU1JE9rJDdGd42DPjk2LLx8/eJBc/HyogZ2H3Egopna1IVK2lUGoTVA0FRjIe4PATwmM5BWj8xX2XK8V2PKchL0MH/4+v3sCPqSBKAW3GyvXvw2TOrt9W4/mxNhDvA6SurxbrOqJ1hQSwtLf4G/K8XsH4HN2rre9qUnl5X3S6cwoqMrX5c="); }
    @Override public Pos position() { return new Pos(-8.5, 71, -55.5, -180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Every problem in life can be solved with a little redstone.", "I've been working on some... experiments.", "Don't touch anything! It's all very delicate."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Every problem in life can be solved with a little redstone."); }
}
