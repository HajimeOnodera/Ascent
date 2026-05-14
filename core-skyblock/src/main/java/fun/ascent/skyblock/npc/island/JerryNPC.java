package fun.ascent.skyblock.npc.island;

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
        return "<green>Jerry</green>";
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
            "<green>Jerry</green>",
            "<yellow><bold>CLICK TO MANAGE</bold></yellow>"
        };
    }

    @Override
    public void onInteract(Player player, AscentNpc npc) {
        npc.speak(player, "Welcome back to your island!", "I'm here to help you manage your resources.");
    }
}
