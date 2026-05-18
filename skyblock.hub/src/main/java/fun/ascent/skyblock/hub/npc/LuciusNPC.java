package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record LuciusNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_lucius"; }
    @Override public String name() { return "<white>Lucius"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NzUyNTE4NDcwMTUsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U0ZDg5NTFjZDIwOTc2OGRiNTcxNWJkYzQ0MzYyMWE1NDUyZDI1ZjRlYjViNmExYzBlNzI4NmIxODQyOTQxZjkifX19", "rXiabawGlB76QvS4asOzdGVmgi3FNkjYA5E00KV9W4mZthyMdJuDNRD3ozvtKXPVUmW2vbHgitViegTlW9UQxvg5dtwsfFXLZ9/II7mehp24FI1GDoInABpqEWVAuz9es+z5sDvmayvhjcRdvExskD7YO2CrYdwo4PZ0+l/axDAs18hVzpZWRq+LswBYbANOo7P0gl3HG4lrpv3kuu4HQsotahfryt1+y+cN+T3FbCljuhL3yn672L9DfUDyW/BFWadWYPuw4SHNYhUCcYUh8aT7+3dYwzl4dz0RymlVU3ghJkljwNq3cvtnvNRCH/nui0waHKEhbUN+fx6n7OmyEIjrRocCJtIpoEKpmXEsHw8Kqa8x1KdPt7fIJsrViEaXuJl5F62oiqINjz5qGClVpzagjhe47a3mgzQFZi6W/BchksWjJml244qWF6Hy41V1viekc3aladLdfNz0oIoQD9rNacRIlnmS6AYGcvWc8xHwPGVYeOK1GEL0Xp/RllVPD6xDzWaN7R8IJ3zsDqo+v+/HX9P1xKR/EZ7wDcCRne76cASMQeROqMsSSJnpkxvUcW7VjnNkSDqx7jcSnucHJDBypcL0ZjwU7sRHm6BBabIG/HRRrGslYCWYyf/6eSt1rgs4t66ez7AVHa+Nc6dRFLRS+7bXuojto2s38FTNDjk="); }
    @Override public Pos position() { return new Pos(125, 73, 165, 30, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Greetings, adventurer. I am Lucius.", "I oversee the Colosseum grounds.", "Come back when you're ready to prove your worth!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "The Colosseum awaits brave warriors. Are you one of them?"); }
}
