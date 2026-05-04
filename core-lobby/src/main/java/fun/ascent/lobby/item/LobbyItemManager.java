package fun.ascent.lobby.item;

import fun.ascent.common.StringUtility;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.lobby.gui.CollectiblesGUI;
import fun.ascent.lobby.gui.GameMenuGUI;
import fun.ascent.lobby.gui.LobbySelectorGUI;
import fun.ascent.lobby.gui.ProfileGUI;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static fun.ascent.common.StringUtility.color;

public class LobbyItemManager {

    private static final Map<UUID, Boolean> playerVisibility = new ConcurrentHashMap<>();

    private static final ItemStack GAME_MENU = ItemStackCreator
            .createNamedItemStack(Material.COMPASS, color("&aGame Menu &7(Right Click)"))
            .set(net.minestom.server.component.DataComponents.LORE,
                    List.of(color("&7Right Click to bring up the Game Menu!")))
            .build();

    private static final ItemStack MY_PROFILE = ItemStackCreator
            .createNamedItemStack(Material.PLAYER_HEAD, color("&aMy Profile &7(Right Click)"))
            .set(net.minestom.server.component.DataComponents.LORE, List.of(
                    color("&7Right-click to browse quests, view achievements,"),
                    color("&7activate Network Boosters and more!")))
            .build();

    private static final ItemStack COLLECTIBLES = ItemStackCreator
            .createNamedItemStack(Material.CHEST, color("&aCollectibles &7(Right Click)"))
            .set(net.minestom.server.component.DataComponents.LORE, List.of(
                    color("&7Mystery Dust: &b0"),
                    Component.empty(),
                    color("&7Collect fun cosmetic items! Unlock new items"),
                    color("&7using &bMystery Dust &7or hitting milestone"),
                    color("&7rewards."),
                    Component.empty(),
                    color("&bMystery Dust &7is randomly given after playing"),
                    color("&7games.")))
            .build();

    private static final ItemStack PLAYERS_HIDDEN = ItemStackCreator
            .createNamedItemStack(Material.GRAY_DYE, color("&fPlayers: &cHidden &7(Right Click)"))
            .set(net.minestom.server.component.DataComponents.LORE,
                    List.of(color("&7Right-click to toggle player visibility!")))
            .build();

    private static final ItemStack PLAYERS_VISIBLE = ItemStackCreator
            .createNamedItemStack(Material.LIME_DYE, color("&fPlayers: &aVisible &7(Right Click)"))
            .set(net.minestom.server.component.DataComponents.LORE,
                    List.of(color("&7Right-click to toggle player visibility!")))
            .build();

    private static final ItemStack LOBBY_SELECTOR = ItemStackCreator
            .createNamedItemStack(Material.NETHER_STAR, color("&aLobby Selector &7(Right Click)"))
            .set(net.minestom.server.component.DataComponents.LORE, List.of(
                    color("&7Right-click to switch between different lobbies!"),
                    color("&7Use this to stay with your friends.")))
            .build();

    public static void init(GlobalEventHandler handler) {
        handler.addListener(PlayerSpawnEvent.class, event -> {
            if (event.isFirstSpawn()) {
                giveItems(event.getPlayer());
            }
        });

        handler.addListener(PlayerUseItemEvent.class, event -> {
            Player player = event.getPlayer();
            ItemStack item = event.getItemStack();
            if (item.material() == Material.GRAY_DYE || item.material() == Material.LIME_DYE) {
                toggleVisibility(player);
                event.setCancelled(true);
            } else if (item.material() == Material.NETHER_STAR) {
                new LobbySelectorGUI().open(player);
                event.setCancelled(true);
            } else if (item.material() == Material.COMPASS) {
                new GameMenuGUI().open(player);
                event.setCancelled(true);
            } else if (item.material() == Material.CHEST) {
                new CollectiblesGUI().open(player);
                event.setCancelled(true);
            } else if (item.material() == Material.PLAYER_HEAD) {
                new ProfileGUI().open(player);
                event.setCancelled(true);
            }
        });

        handler.addListener(InventoryPreClickEvent.class, event -> {
            if (event.getInventory() != null) {
                event.setCancelled(true);
            }
        });

        handler.addListener(ItemDropEvent.class, event -> event.setCancelled(true));
    }

    private static void giveItems(Player player) {
        player.getInventory().setItemStack(0, GAME_MENU);
        player.getInventory().setItemStack(1, MY_PROFILE);
        player.getInventory().setItemStack(4, COLLECTIBLES);

        boolean hidden = playerVisibility.getOrDefault(player.getUuid(), false);
        player.getInventory().setItemStack(7, hidden ? PLAYERS_HIDDEN : PLAYERS_VISIBLE);
        player.getInventory().setItemStack(8, LOBBY_SELECTOR);
    }

    private static void toggleVisibility(Player player) {
        boolean isHidden = playerVisibility.getOrDefault(player.getUuid(), false);
        boolean newHiddenState = !isHidden;
        playerVisibility.put(player.getUuid(), newHiddenState);

        player.getInventory().setItemStack(7, newHiddenState ? PLAYERS_HIDDEN : PLAYERS_VISIBLE);

        player.updateViewableRule(entity -> {
            if (entity != player) {
                return !newHiddenState;
            }
            return true;
        });

        player.sendMessage(color(newHiddenState ? "&cPlayers are now hidden." : "&aPlayers are now visible."));
    }
}
