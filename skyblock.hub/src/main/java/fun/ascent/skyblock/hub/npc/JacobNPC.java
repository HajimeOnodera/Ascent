package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record JacobNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_jacob"; }
    @Override public String name() { return "<white>Jacob"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYwNDM3MDQyNDk4NiwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I4YmIxYjQ4ZjRiYWJjNjdjZTM5NTQ3MjA4ZmRiZWQ3MjJjYTU5OGNkZjMwNjgxZTM2N2M2MjQ3Y2FiMTkxOCIKICAgIH0KICB9Cn0=", "pEkQ4zTK7/clvbGWw7JthccvquN+7wwY1rAIqUULhF6PLc8FWRwcVnwtd4K/j8xaTbe3ol8wayk8hraHtPPx6gxtLUbT/r/th9gsfJIBlbvcYJmrLnOI5yUHHYtxPjC2O9OTcxspyv2Ha0kF5Ua/RzOEjU4JN9HM5ni1g6c/U2mFgUnnj3SrNjr5pkIO0wZ27INHcswdVJuD8pC+yaflaSgKPV4CnoPFPp9uBvnVG9Knsz6oVNLno3ZUMojMYU5O3Nnkg8naNb/G2H1FBbwtgVxsjaprKbsqhrQHvFF9oia/Fz9I8IqGukLM0J9Dc3UANjORxeTZcqKkTnDM1wic7s52B5cuZZCQ2by5Di+km/hL2m8dJzVe7TbfS+FrK0galtQHD7jb7yXapQ7LqbutHTuElVCijrp0FlUE0rC+dUeBTbVFcXqo+V/cy2OVuv6DBJM87/6yogqlxyD3ZIetmJx9go42rIXwK97IgTtHt4nO/l14/kc46FL2ESe+P7Nqe/bJRmVDqEy5kuTSB7P4po1O2UHYv9Fydt0daxDrnm/CekKAg5EWh/m30RSKcXu84E85Y2FPW0U4jnmd2fcHaR3MCIY9os44GUEMm56xDKDNtqpRohTEhfo9ncLHTnC3+urN4QudtTbwjha31fvewzIDZKFsHAKakhyEG+OHehk="); }
    @Override public Pos position() { return new Pos(46.5, 73, -128.5, -37, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hey there, farmer! I'm Jacob.", "I host farming contests throughout the seasons.", "Come talk to me when you're ready to compete!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Ready for the next farming contest? Stay tuned!"); }
}
