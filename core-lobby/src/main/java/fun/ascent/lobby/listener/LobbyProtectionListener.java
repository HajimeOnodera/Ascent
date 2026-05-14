package fun.ascent.lobby.listener;

import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;

/**
 * Handles protection for the lobby instance, preventing players from
 * modifying the world or harming entities.
 */
public final class LobbyProtectionListener {

    public static void register(GlobalEventHandler handler) {
        // Prevent block breaking
        handler.addListener(PlayerBlockBreakEvent.class, event -> event.setCancelled(true));

        // Prevent block placing
        handler.addListener(PlayerBlockPlaceEvent.class, event -> event.setCancelled(true));

        // Prevent block interactions (chests, doors, etc.)
        handler.addListener(PlayerBlockInteractEvent.class, event -> event.setCancelled(true));

        // Prevent item dropping
        handler.addListener(ItemDropEvent.class, event -> event.setCancelled(true));

        // Prevent PVP and all damage
        handler.addListener(EntityDamageEvent.class, event -> event.setCancelled(true));
    }
}
