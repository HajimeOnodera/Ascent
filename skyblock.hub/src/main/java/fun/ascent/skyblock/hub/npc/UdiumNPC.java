package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record UdiumNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_udium"; }
    @Override public String name() { return "<aqua>Udium"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTY4NzgwNDE4ODc1NCwKICAicHJvZmlsZUlkIiA6ICIzNjMzMGY2YzI5NmY0NjJmYjQ3M2I4MTc4NjczMzBhMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJoZWFydHlkaWUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVhZWU3ZTUxYTg3NjkzYThmYjdmZTQzMGE4ODdmZmIyN2VlYjk3NDljZWE4MTZkNjUyNDVlMmRmNjU1Y2YzMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9", "Nr5ok8+BUckFs6UrMdisUo105wGkE1x4A3cinq2zqltLkFDei8GDarKeDIIZHKYbAwCt2pqWfCNBSuT/XzhANC0ssQM6N1kh515V2IE92DRV00LGoDY5lH3A5XDhnZy94FAVVsUzVUvVjeB/x7O1HaVF22xNaUzwFD9zAxGuGDyaUsA98i6XDgja9qlNnswAWeDkLPga9gM3XjxCaMwyrw8yroyu8KlkobCSWKCsFRGH2wqkmJJg7qlZgrQNDjDj4j/I6FVaqOxcgWtOrmLgZMF+UMDq0zBw5dtc5lB3myw77CogSNgzbyF5dI1b3kZ/7ULC1evvWCp7YpNMkNh4Aezp9ElTTUe+0mF6gGAMEo4F3Nj1VD8cs+eINFiCPneSJoMxAKCjppYaGXfO2zZeAgmmTnbj39v1/A5ZxC262z8fA26EfrUXoLiKsJ5He4dJfmo7WO+33ha+pMTLuzP+LrZ9QyInhD6uM0YpQRauTZPrAnz6x6gJvnCirFNjtPIPQZf01KJEB7hCvlPjMIHL+9MlRG1uflgKvMWiBbtZnLegAKvjCETHJ8tPd4hRrxbcNh15n3tyMuWGQCRDDX+fvEqRFg/FYATd9dnady1/monmy25j5+c6qpYlvxKGMS7UWjSDuBzH5uQvq03VMx2D4Qd6CniJ3jDEhTjsAD/7fWs="); }
    @Override public Pos position() { return new Pos(46.5, 101, 100.5, 145, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome to the Hub! I can help you find your way around.", "There's so much to explore here!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need help finding something?"); }
}
