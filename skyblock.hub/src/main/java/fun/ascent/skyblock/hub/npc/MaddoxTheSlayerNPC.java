package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record MaddoxTheSlayerNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_maddox_the_slayer"; }
    @Override public String name() { return "<dark_purple>Maddox the Slayer"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTY0NjY1MjUwNjMxMCwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzM4YzY4N2RkODg4N2M1MDlkNTU4NjVlYmI2MmFiNjA4N2VmNzQzZTk0MjVjZDVkYzZiNGM3YjkzYjc0N2ZmMiIKICAgIH0KICB9Cn0=", "enmXRpBElHQ2aBCDawMdi5hG0pm9oLwOerSYQHhSO/fDZwy0eZRJwJzo3QgnYcESf9EEGyxZ1sVpn3Apw1UAPh8h0j5rf0I6l1xXmlj5cyqTaPZWPETMCvOSgX75L0hUm87X3eROyyU9pQq1HY6JaMC/raNwwg/PQuxt28UmTYQczzCg4cStVfLfO1sAIzYZK5+gBY3fnKkW3N0t1gcJfJJXUKOKnfTTy31C3rtKqU8FKg4VqRH3JbYSdGjV1+uUtTv59s9NnJJIjgTvj+uyzaqgy189xh9c8apUUftwyUhapymG9XyMBVF8SclTicPSA+Bgqx7OpVGcgE0fW8a3H0hLO6ZbDhODmAIfPV+gxXb8rPR12i4JvzuK2AuInsLeIrpUpeujoO0VcZl4a/h3TQLaUgTjVQzkoLYEi+b7VXuaJBdSiHXjjfD2op+iGIzUaNmNHjIcNpBvTds2Uu1bZdygYFciANJEPtUHnXH8I6Vv/hnJ9mQ7py6UOSRlxN9ncOuTtwvSqudZnaTueHZ2Jbclu4worL0wCNIXQyOxIqlC4sMfYm7H56JCrLlpZCH1rFuNa88M051PVXZei4Z4X+eIUhB3xiW/gpwwZW32ax0W8Vlgdy928BUmczRksjxKJ2hHCO14QCJnH9kgdQtVQPxnobYnz+56ijNsByWdBMg="); }
    @Override public Pos position() { return new Pos(-83.5, 68, -129.5, -135, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"So you want to be a Slayer, huh?", "I can assign you quests to slay powerful creatures.", "Complete enough and you'll earn rewards beyond your wildest dreams.", "Come back when you're ready for a challenge."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Ready for another Slayer quest?", "Choose wisely... some of these creatures are not to be trifled with."); }
}
