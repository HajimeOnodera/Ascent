package fun.ascent.skyblock.npc.village;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class LumberjackNPC implements NpcDefinition {
    private final Instance instance;

    public LumberjackNPC(Instance instance) {
        this.instance = instance;
    }

    @Override
    public String id() {
        return "village_lumberjack";
    }

    @Override
    public String name() {
        return "<green>Lumberjack</green>";
    }

    @Override
    public NpcType type() {
        return NpcType.VILLAGER;
    }

    @Override
    public Instance instance() {
        return instance;
    }

    @Override
    public Pos position() {
        return new Pos(-5.5, 41, -5.5);
    }

    @Override
    public void onInteract(Player player, AscentNpc npc) {
        npc.speak(player, "It's hard work gooning all day!", "Maybe you could help me out");
    }
}
