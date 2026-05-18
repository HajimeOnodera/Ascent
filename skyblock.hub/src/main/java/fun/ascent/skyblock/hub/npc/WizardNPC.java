package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record WizardNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_wizard"; }
    @Override public String name() { return "<light_purple>Wizard"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NTkxNDEyMjMzNjIsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJmNjlhYjJmNGU3MzQxYWMyNjI1OTcwODNjYTM4YWQwMWJmYmJmODczMGZiZmJkZDEwY2E5MGFlZTMzN2EyZGQifX19", "Ubs3ogFzpCcDZZQiNcIrTQTykcnJsac0Fm+mXYMFVh7CNuTdmGlcJcqdb8fXmBJFOfO9/8zAxKVyn3YVehkzzFdHJyMiPqK++arqf0eBNElbON9LUlPvOGca7JCBYBJdZWbpTS3lbzL9VZRoba9GmM0XZfvunZyLTF671JrKT0adDREe4B7HJaqmLAPOXZlzQokxeyN0oqXAPThkQPcR2UzKNQpwAAPiQhrPc7dTvikw9sHOVPX9GsTsqUAjh1vQOZl4DeALMZwG5Clzc+SDWAa5hgNn15SVCWlbPcsumGziXo1EPhYrx5XbGkDS/9OE+y7/7qMQE1YAUoVw4umREJ5YoqWfv5Elko1dOkF6HfVLuyglFIoJenPJ6/jy7C1tNpHbCG/xOKBeMAGR1+YNCIm3JWzgZbFDMpKjFhfgQEOaLPsQPLPxAYA+RjNRpQpXSke8Zfi64j2j85G4gCcOy8zxlYjHdKgxiZNSIs2cyLSU87BdJFaVBKkQvnB5z9AsJu6u1OoC0A4NakRDQyhjzn93RO8i6LoF2MQshEbBn/2q1ja/KfIw4ZjsK11G7xTP+qnVdkXzgC0eGwnfAqkltbfbmwnGVShe0+cVF3FNs+C/j2BtjptUm7Fhw7nu/I40J+u+jTA/NdZl4JWGCIDIzsawACfO14Xt5DjrmtcY24U="); }
    @Override public Pos position() { return new Pos(48.5, 119, 99.5, -231, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Ah, a visitor! I am the Wizard of this tower.", "I deal in powerful enchantments and magical artifacts."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Seeking magical knowledge? You've come to the right place."); }
}
