package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record BakerNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_baker"; }
    @Override public String name() { return "<white>Baker"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTU5OTQ1NzU3MjI1NCwKICAicHJvZmlsZUlkIiA6ICI2MTI4MTA4MjU5M2Q0OGQ2OWIzMmI3YjlkMzIxMGUxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJuaWNyb25pYzcyMTk2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U5ZTIzYmU3ZjA0NTU2ZmEzMzM1MWE0Yzc3MWEzZjA1ZjRhNmQyN2RlNDEzYTM2ZDAyMzBjNjFmNzE2OTg3OTkiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "lu9SuKpv/U8XqaZTkleKzPDg8S1pcqA7LSWiWimR9x0BnkpK5CkyLwkWA1AMKCibQZSMPjoFFySNMVRcIhylv3yN0V6/Y6moJi1/SmRIeJJL/FovCUykzTSvbWsqJXfRyoi+5mUt6REj6bvJQruNtCedIHQD5a0Mrw3d8LbvZ0OlGPUbaAv1O7dW1O2uxmxCDSWMOL8PN+6fb/zYgA/XeJvSj97LafK4YAeb1YV362CeMkhmMP0uE5wj11+BnexEN+WaBzbRIUlBuSMB+Pw+7RoS4Nk7kxxKSNAR/pzlSqFHLkTlL88ljrLeyEooccpETSuqLh55/wsWSdesEDpSNjmfRYVX9EXOk783VRz3Btb+MItjiqmos5Mgmjelnx34utIPkAFbLyn/AUvWaNImxhWw/iDFYod+C/QNbUqR/H9ahIHzZXun4+6tKhVBgaCfLqaqF+V9Js8miapUpW16EEnElTNJ843+/HFgqex18q2vCTUX0tixtzHrFmwhhbBnT02DSvbvIxm9ucyNMwTpYhJ33I433pB67i1iQxiNBxaTTVSn2bGs4AKLgOjkTg3TsixEix02fCOzFl8bau/JlZMDmk7/2SAI74VRnreBVTEHjIAb7SRRXNy+zOxQJLzyMB+TwpGBBIUbNpCgjKu0aqu+Ld/FOO37dvBke8bv7Uw="); }
    @Override public Pos position() { return new Pos(-6.5, 70, -47.5, 180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Oh hello there! I'm the Baker.", "I bake special cakes for the New Year celebration!", "Come see me during the festivities for a treat."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Come back during the New Year celebration for a special cake!"); }
}
