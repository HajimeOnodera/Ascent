package fun.ascent.skyblock.island.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record JerryNPC(Instance instance, Pos position, String id) implements NpcDefinition {

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return "<white>Jerry</white>";
    }

    @Override
    public NpcType type() {
        return NpcType.VILLAGER;
    }

    @Override
    public Pos position() {
        return position;
    }

    @Override
    public String[] holograms() {
        return new String[] {
            "<gold><bold>NEW UPDATE",
            "<white>Jerry",
            "<yellow><bold>CLICK"
        };
    }

    @Override
    public void onInteract(Player player, AscentNpc npc) {
        npc.speak(player, "Jerry!");
    }
}
