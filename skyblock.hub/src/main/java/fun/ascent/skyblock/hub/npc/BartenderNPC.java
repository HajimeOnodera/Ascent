package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record BartenderNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_bartender"; }
    @Override public String name() { return "<gold>Bartender"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "eyJ0aW1lc3RhbXAiOjE1Mzc2NTgyNzQ0NDcsInByb2ZpbGVJZCI6ImVmYWY1NzU3NzgxZTQ3YWViYzE0Y2Q4MmM5MWM3ZjgyIiwicHJvZmlsZU5hbWUiOiJNaW5lU2tpbiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVkZDhmMTZiMjAyYzQyYjNiNWFkNDU3ZDQzNmNjNjM5OTUyMTA3ZTM2MWFiMWU2MWI5MTY4OWU0MGM3OGJlNyJ9fX0=",
                "BzV+QBeWNTuShsKXgjlncos8dIDbogV/LUCOSGt6yU+hc+aAhtdo68Z0fQYo53MEj2rIJSeAe+oqKvmWpp7PCbualsJ2YoU/RAM/E1n+3bsWZFfiYAtQNZZA/tSiJgHQ1pLW2GLD7xcOvMYtam5cP82PtGgbTk4TZVnTSaPI6syHWXz8jDEFY0jg2CVr6v6zxNf7uP2vXI1U2Xf8//rEhNXYmyBDWC3BBh0I5hYjYiioV5C/ibwe6yShaI/Qdbl9/NASZqFfPisqvo96wHePPPfSyZETsz9oY92jYFG8wXaq44JENEYfl3iehwnM2MVhIvEoZUADTgyJcnBmM63x18ldNqKqf5sj8YkWQOeCMes/F8HDn8VRqdXLqBC+P/FMVmnDKWuC2BAUij2762TFJ0G1+814rUbz3poCM5cpJwHN1JjXENWQqZgyPBcRDha67oJdDixrkzCLQ6UNJ+I2D6fyaTvCEXQC0S+CMMd7fIoDrBABnOJmoxn3wTBfHqquqLooTL4Vvnt/vh5miLEmDf/SkfdPRHZMRvfgPMTGdAC0FjaoWB+RmKANh2ePh85XhFplI5lI22kf+Jn1SxwLhP+5qv/3+0m2KY6czPrGau3HkCoDKurd/oimQgaqF8WvpKrBT3ciGiz9i6fJH/m0eNL/cdPWAit7xCPyuj5MiLU="); }
    @Override public Pos position() { return new Pos(-83.5, 74, -135.5, 0, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome to the Tavern! Pull up a stool.", "I serve the finest brews on the island.", "Looking for something to warm your bones after a long adventure?"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "What'll it be today? The usual?"); }
}
