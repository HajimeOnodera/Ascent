package fun.ascent.skyblock.npc.impl;

import fun.ascent.skyblock.npc.impl.enums.NPCType;
import net.minestom.server.entity.Player;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

public interface NPCParameters {

    String CLICK = "<yellow><bold>CLICK</bold>";

    String id();

    String name();

    default NPCType type() {
        return NPCType.PLAYER;
    }

    default String[] firstInteractionMessages() {
        return null;
    }

    default String[] messages() {
        return null;
    }

    default String[] holograms() {
        return name() != null ? new String[] { name(), CLICK } : null;
    }

    default NPCSkin skin() {
        return null;
    }

    Instance instance();

    Pos position();

    default boolean looking() {
        return true;
    }

    default void onFirstInteract(Player player, SkyBlockNPC npc) {
        onInteract(player, npc);
    }

    void onInteract(Player player, SkyBlockNPC npc);
}
