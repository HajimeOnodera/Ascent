package fun.ascent.lobby.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyItemManager {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final Map<UUID, Boolean> playerVisibility = new ConcurrentHashMap<>();

    private static final ItemStack GAME_MENU = ItemStack.builder(Material.COMPASS)
            .set(DataComponents.CUSTOM_NAME, MM.deserialize("<green>Game Menu</green> <gray>(Right Click)</gray>"))
            .set(DataComponents.LORE, List.of(MM.deserialize("<gray>Right Click to bring up the Game Menu!</gray>")))
            .build();

    private static final ItemStack MY_PROFILE = ItemStack.builder(Material.PLAYER_HEAD)
            .set(DataComponents.CUSTOM_NAME, MM.deserialize("<green>My Profile</green> <gray>(Right Click)</gray>"))
            .set(DataComponents.LORE, List.of(
                    MM.deserialize("<gray>Right-click to browse quests, view achievements,</gray>"),
                    MM.deserialize("<gray>activate Network Boosters and more!</gray>")
            ))
            .build();

    private static final ItemStack COLLECTIBLES = ItemStack.builder(Material.CHEST)
            .set(DataComponents.CUSTOM_NAME, MM.deserialize("<green>Collectibles</green> <gray>(Right Click)</gray>"))
            .set(DataComponents.LORE, List.of(
                    MM.deserialize("<gray>Mystery Dust: </gray><aqua>1,078</aqua>"),
                    Component.empty(),
                    MM.deserialize("<gray>Collect fun cosmetic items! Unlock new items</gray>"),
                    MM.deserialize("<gray>using </gray><aqua>Mystery Dust</aqua><gray> or hitting milestone</gray>"),
                    MM.deserialize("<gray>rewards.</gray>"),
                    Component.empty(),
                    MM.deserialize("<aqua>Mystery Dust</aqua><gray> is randomly given after playing</gray>"),
                    MM.deserialize("<gray>games.</gray>")
            ))
            .build();

    private static final ItemStack PLAYERS_HIDDEN = ItemStack.builder(Material.GRAY_DYE)
            .set(DataComponents.CUSTOM_NAME, MM.deserialize("<white>Players: </white><red>Hidden</red> <gray>(Right Click)</gray>"))
            .set(DataComponents.LORE, List.of(MM.deserialize("<gray>Right-click to toggle player visibility!</gray>")))
            .build();

    private static final ItemStack PLAYERS_VISIBLE = ItemStack.builder(Material.LIME_DYE)
            .set(DataComponents.CUSTOM_NAME, MM.deserialize("<white>Players: </white><green>Visible</green> <gray>(Right Click)</gray>"))
            .set(DataComponents.LORE, List.of(MM.deserialize("<gray>Right-click to toggle player visibility!</gray>")))
            .build();

    private static final ItemStack LOBBY_SELECTOR = ItemStack.builder(Material.NETHER_STAR)
            .set(DataComponents.CUSTOM_NAME, MM.deserialize("<green>Lobby Selector</green> <gray>(Right Click)</gray>"))
            .set(DataComponents.LORE, List.of(
                    MM.deserialize("<gray>Right-click to switch between different lobbies!</gray>"),
                    MM.deserialize("<gray>Use this to stay with your friends.</gray>")
            ))
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
                openLobbySelector(player);
                event.setCancelled(true);
            } else if (item.material() == Material.COMPASS || item.material() == Material.CHEST || item.material() == Material.PLAYER_HEAD) {
                event.setCancelled(true);
            }
        });

        handler.addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            if (event.getInventory() instanceof Inventory inv && inv.getTitle().equals(MM.deserialize("Lobby Selector"))) {
                int slot = event.getSlot();
                if (slot >= 11 && slot <= 15) {
                    int lobbyNum = slot - 10;
                    if (lobbyNum != 1) {
                        event.getPlayer().sendMessage(MM.deserialize("<green>Connecting to Lobby " + lobbyNum + "...</green>"));
                        fun.ascent.lobby.transfer.ProxyTransfer.send(event.getPlayer(), "lobby-" + lobbyNum);
                    } else {
                        event.getPlayer().sendMessage(MM.deserialize("<red>You are already connected to this lobby!</red>"));
                    }
                }
            }
        });

        handler.addListener(ItemDropEvent.class, event -> {
            event.setCancelled(true);
        });
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
            if (entity instanceof Player && entity != player) {
                return !newHiddenState;
            }
            return true;
        });
        
        player.sendMessage(MM.deserialize(newHiddenState ? "<red>Players are now hidden.</red>" : "<green>Players are now visible.</green>"));
    }

    private static void openLobbySelector(Player player) {
        Inventory inventory = new Inventory(InventoryType.CHEST_3_ROW, MM.deserialize("Lobby Selector"));
        
        for (int i = 1; i <= 5; i++) {
            boolean isCurrent = (i == 1);
            ItemStack lobbyItem = ItemStack.builder(Material.NETHER_STAR)
                    .set(DataComponents.CUSTOM_NAME, MM.deserialize(isCurrent ? "<green>Lobby " + i + "</green>" : "<yellow>Lobby " + i + "</yellow>"))
                    .set(DataComponents.LORE, List.of(
                            MM.deserialize("<gray>Status: </gray>" + (isCurrent ? "<green>Connected</green>" : "<yellow>Online</yellow>")),
                            Component.empty(),
                            isCurrent ? MM.deserialize("<red>Already connected!</red>") : MM.deserialize("<gray>Click to connect!</gray>")
                    ))
                    .build();
            inventory.setItemStack(10 + i, lobbyItem); // Slots 11, 12, 13, 14, 15
        }
                
        player.openInventory(inventory);
    }
}
