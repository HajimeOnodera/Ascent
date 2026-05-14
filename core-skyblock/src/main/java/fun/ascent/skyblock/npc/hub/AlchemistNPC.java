package fun.ascent.skyblock.npc.hub;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record AlchemistNPC(Instance instance) implements NpcDefinition {

    @Override
    public String id() {
        return "hub_alchemist";
    }

    @Override
    public String name() {
        return "<gold>Alchemist</gold>";
    }

    @Override
    public NpcType type() {
        return NpcType.WITCH;
    }

    @Override
    public Pos position() {
        return new Pos(80.5, 72, -90.5);
    }

    @Override
    public void onInteract(Player player, AscentNpc npc) {
        npc.speak(player, "a");
    }
}

