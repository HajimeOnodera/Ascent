package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record RosettaNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_rosetta"; }
    @Override public String name() { return "<light_purple>Rosetta"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYyNjExMTA4NDYzNCwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEwMzA3M2Y3MTZmZmQzNzE4ODJkOTRiYTVhZjBlNWU1YzBkMzc2MTljNGExZGJhNTk5NmFmZDczMjI1ODczYiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9", "AoCJxIstTeKrg3IMz+OVliX+I8lXNSVTrFZmGhAABZLxFQly8JdzpnuuYUe+DqDMGQ2tGEeFmm5FA4IvGqU0YWe6j2QhzyvKO9ZzD1rG9Urtu+34bYXRx6XSkrYoKWFHXoRMbEp/3jLEQbyLZnNrSa46Bwa+KYV2FA5TOcxTrN/dbnWqCK+u2YX0sw7ctrPuts/C/RBXiuQKEQ9N9GrXTPYYCumnawtaEPun5IGH8EHVvQTdBMmrqk03VcIz+M2IakNKKxUG3p7h2g2N6X6GuoPMz/6j/EcFnXEaJUCtkj+P4hxwud6RD2FMctUn/HEAVBRXKWR+9Ou+TY4yfGGwswwyOfdV1cge4UuZL3nfNNRso5yIZK22aB9EesFyUZtJBcdBXYl/Iw/vrNRWMq+sb0sbP7zsWfyshrj55rod0YSxzY1vUn/vCc7R+7JnR+nYco5wzGtDxMQrvU5jRNfa3x4lcRfdNSITQrQHGuOZ3h/eTbe2wTGod2Lko5I6oBoNvtffdW91GRUsrXmIwds+JgeYFYaeJLVclXchjEPH2dtWPN1IqKCd1beRgK46l6yzO96fR1l9JPTF5MR6yrfvzJW6vb5oK9yhRH/gUZIzU63FdXr9DHln1mIT7ya2Nd0LEaqpmV6IZ6DV+/qFxH9zRJfP/v+PxaL9HIOMaGWSz4w="); }
    @Override public Pos position() { return new Pos(-53.5, 69, -88.5, 45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello there! I'm Rosetta.", "I tend to the flowers in this garden.", "Nature is a wonderful thing, don't you think?"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "The flowers are blooming beautifully today!"); }
}
