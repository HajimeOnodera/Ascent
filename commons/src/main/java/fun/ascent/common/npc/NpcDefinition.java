package fun.ascent.common.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

/**
 * Base interface that every NPC class must implement.
 * Each NPC should be its own class file containing all its properties:
 * id, name, skin, position, type, holograms, messages, and interaction logic.
 *
 * <p>Modules (lobby, skyblock, etc.) create their own NPC classes that implement
 * this interface, while the common {@link NpcManager} handles registration,
 * spawning, and event routing.</p>
 */
public interface NpcDefinition {

    String CLICK = "<yellow><bold>CLICK</bold>";

    /** Unique identifier for this NPC (e.g. "village_banker", "lobby_skyblock_portal") */
    String id();

    /** Display name shown above the NPC, supports MiniMessage format */
    String name();

    /** The entity type of this NPC */
    default NpcType type() {
        return NpcType.PLAYER;
    }

    /** Messages the NPC speaks on first interaction */
    default String[] firstInteractionMessages() {
        return null;
    }

    /** Messages the NPC speaks on interaction (spoken sequentially with delays) */
    default String[] messages() {
        return null;
    }

    /** Hologram lines displayed above the NPC */
    default String[] holograms() {
        return name() != null ? new String[] { name(), CLICK } : null;
    }

    /** The player skin for PLAYER-type NPCs */
    default NpcSkin skin() {
        return null;
    }

    /** The instance this NPC belongs to */
    Instance instance();

    /** The spawn position of this NPC */
    Pos position();

    /** Whether the NPC should look at nearby players */
    default boolean looking() {
        return true;
    }

    /** Called on first interaction, defaults to normal onInteract */
    default void onFirstInteract(Player player, AscentNpc npc) {
        onInteract(player, npc);
    }

    /** Called when a player interacts (right-clicks) with this NPC */
    void onInteract(Player player, AscentNpc npc);
}
