package fun.ascent.common.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public interface NpcDefinition {

    String CLICK = "<yellow><bold>CLICK</bold>";

    String id();
    String name();
    default NpcType type() {
        return NpcType.PLAYER;
    }
    default String[] firstInteractionMessages() {
        return null;
    }
    default String[] holograms() {
        return name() != null ? new String[] {
            name(), CLICK } : null;
    }
    default NpcSkin skin() {
        return null;
    }
    Instance instance();
    Pos position();
    default boolean looking() {
        return true;
    }
    default void onFirstInteract(Player player, AscentNpc npc) {
        onInteract(player, npc);
    }
    void onInteract(Player player, AscentNpc npc);
}
