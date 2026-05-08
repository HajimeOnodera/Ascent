package fun.ascent.lobby.item;

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
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static fun.ascent.common.StringUtility.text;
import static net.minestom.server.component.DataComponents.*;

public class LobbyItemManager {

    private static final Map<UUID, Boolean> playerVisibility = new ConcurrentHashMap<>();

    private static final ItemStack GAME_MENU = ItemStackCreator
            .createNamedItemStack(Material.COMPASS, text("<green>Game Menu <gray>(Right Click)"))
            .set(LORE,
                    List.of(text("<gray>Right Click to bring up the Game Menu!")))
            .build();

    private static final ItemStack MY_PROFILE = ItemStackCreator
            .createNamedItemStack(Material.PLAYER_HEAD, text("<green>My Profile <gray>(Right Click)"))
            .set(LORE, List.of(
                    text("<gray>Right-click to browse quests, view achievements,"),
                    text("<gray>activate Network Boosters and more!")))
            .build();

    private static final ItemStack COLLECTIBLES = ItemStackCreator
            .createNamedItemStack(Material.CHEST, text("<green>Collectibles <gray>(Right Click)"))
            .set(LORE, List.of(
                    text("<gray>Mystery Dust: <aqua>0"),
                    Component.empty(),
                    text("<gray>Collect fun cosmetic items! Unlock new items"),
                    text("<gray>using <aqua>Mystery Dust <gray>or hitting milestone"),
                    text("<gray>rewards."),
                    Component.empty(),
                    text("<aqua>Mystery Dust <gray>is randomly given after playing"),
                    text("<gray>games.")))
            .build();

    private static final ItemStack PLAYERS_HIDDEN = ItemStackCreator
            .createNamedItemStack(Material.GRAY_DYE, text("<white>Players: <red>Hidden <gray>(Right Click)"))
            .set(LORE,
                    List.of(text("<gray>Right-click to toggle player visibility!")))
            .build();

    private static final ItemStack PLAYERS_VISIBLE = ItemStackCreator
            .createNamedItemStack(Material.LIME_DYE, text("<white>Players: <green>Visible <gray>(Right Click)"))
            .set(LORE,
                    List.of(text("<gray>Right-click to toggle player visibility!")))
            .build();

    private static final ItemStack LOBBY_SELECTOR = ItemStackCreator
            .createNamedItemStack(Material.NETHER_STAR, text("<green>Lobby Selector <gray>(Right Click)"))
            .set(LORE, List.of(
                    text("<gray>Right-click to switch between different lobbies!"),
                    text("<gray>Use this to stay with your friends.")))
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

        handler.addListener(InventoryPreClickEvent.class, event -> event.setCancelled(true));

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

        player.sendMessage(text(text(newHiddenState ? "<red>Players are now hidden." : "<green>Players are now visible.")));
    }
}


