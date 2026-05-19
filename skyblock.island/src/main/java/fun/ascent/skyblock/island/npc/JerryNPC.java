package fun.ascent.skyblock.island.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcType;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record JerryNPC(Instance instance) implements NpcDefinition {

    @Override
    public String id() {
        String worldId = instance.getTag(WorldHandler.worldID);
        return "jerry_" + (worldId != null ? worldId : "unknown");
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
        return new Pos(9.5, 100, 34, 180, 0);
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
