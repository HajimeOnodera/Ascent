package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record LonelyPhilosopherNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_lonely_philosopher"; }
    @Override public String name() { return "<white>Lonely Philosopher"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTU4OTMzMzQ0ODA4OSwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFkYTg3NTE3OTBjN2FkZTAzZDNhOGFjMzI1MDZhM2RlZDE4Y2JmYWU4MTI2ODlkMDNmNmYzNGM3YTk2MjYyOCIKICAgIH0KICB9Cn0=", "tKdLS3NJLZ66Hq4KK4jd0mjj+4BOeYpEkTK0+hS9JJfrFqbFdBypZKqnn5mMvujvoKfPUZ3LSH9s/HFfcnLsz33mvTStcv3HzwuiO68Dl7FTI3rye/Fho/en2zbihomGu7p8xb8+gEk6UYVgd9elaYkNt35jGKrlcOGePtPHVrG5LMM7qF9OYJtsA4zdR7h60zPg2SbSexhkbiKTM97xVPF+zya5PqSgwPMe1K/+s0SmIrd/6FMD7h+K0eavnCCq0dPweRYvc5vq3ji8a69h42Fg1p7U7vEuPdW0YH8wxD/dECx2w0VZqwWx4a14Gucnh87AlskOdrp2HZrdEAKU4i/rNPYrNIxzd//Ad899cuQ4fuk3NmnsrKpMvi4S9iPqkIYgkEHSbmm545r+Kcivhjj33eTTZr7ne9L+TAxPaxiqCjsP2hQQMS0jjtm6OUAALQRnq4625ZOZJ6TJGxXC8hSAJGv/PiqCpEsqc34gxhWVq8QgOq+2tX/A3TbmthxWp8BcoxyW9g25srCNZnmN0NyFVTjY9gTdWJwMfFOlzLSkHLksYB3QKYF4+HcTyyDIuQp12zKhYmFFAuz7WHcE4KOJ3QVHygmwPsnQMeqLvH9u6n9zBeC4KTK/AZvOH2BrkSfO6UUlnpv3jyu7ZCiz3PsFZVjn6DmnjOj1p88lan8="); }
    @Override public Pos position() { return new Pos(-250.75, 130, 41.188, -51, 4); }
    @Override public String[] firstInteractionMessages() { return new String[]{"To fast travel or not to fast travel?", "I'm sorry, I have nothing for you."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "I'm sorry, I have nothing for you."); }
}
