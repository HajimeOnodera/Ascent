package fun.ascent.lobby.item;

import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.redis.ServerPing;
import fun.ascent.lobby.Main;
import net.kyori.adventure.text.Component;
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
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.ResolvableProfile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static fun.ascent.common.util.CC.c;

public class LobbyItemManager {

    private static final Map<UUID, Boolean> playerVisibility = new ConcurrentHashMap<>();

    /** Maximum lobby slots to display in the selector. */
    private static final int MAX_LOBBY_SLOTS = 5;

    private static final ItemStack GAME_MENU = ItemStack.builder(Material.COMPASS)
            .set(DataComponents.CUSTOM_NAME, c("&aGame Menu &7(Right Click)"))
            .set(DataComponents.LORE, List.of(c("&7Right Click to bring up the Game Menu!")))
            .build();

    private static final ItemStack MY_PROFILE = ItemStack.builder(Material.PLAYER_HEAD)
            .set(DataComponents.CUSTOM_NAME, c("&aMy Profile &7(Right Click)"))
            .set(DataComponents.LORE, List.of(
                    c("&7Right-click to browse quests, view achievements,"),
                    c("&7activate Network Boosters and more!")
            ))
            .build();

    private static final ItemStack COLLECTIBLES = ItemStack.builder(Material.CHEST)
            .set(DataComponents.CUSTOM_NAME, c("&aCollectibles &7(Right Click)"))
            .set(DataComponents.LORE, List.of(
                    c("&7Mystery Dust: &b0"),
                    Component.empty(),
                    c("&7Collect fun cosmetic items! Unlock new items"),
                    c("&7using &bMystery Dust &7or hitting milestone"),
                    c("&7rewards."),
                    Component.empty(),
                    c("&bMystery Dust &7is randomly given after playing"),
                    c("&7games.")
            ))
            .build();

    private static final ItemStack PLAYERS_HIDDEN = ItemStack.builder(Material.GRAY_DYE)
            .set(DataComponents.CUSTOM_NAME, c("&fPlayers: &cHidden &7(Right Click)"))
            .set(DataComponents.LORE, List.of(c("&7Right-click to toggle player visibility!")))
            .build();

    private static final ItemStack PLAYERS_VISIBLE = ItemStack.builder(Material.LIME_DYE)
            .set(DataComponents.CUSTOM_NAME, c("&fPlayers: &aVisible &7(Right Click)"))
            .set(DataComponents.LORE, List.of(c("&7Right-click to toggle player visibility!")))
            .build();

    private static final ItemStack LOBBY_SELECTOR = ItemStack.builder(Material.NETHER_STAR)
            .set(DataComponents.CUSTOM_NAME, c("&aLobby Selector &7(Right Click)"))
            .set(DataComponents.LORE, List.of(
                    c("&7Right-click to switch between different lobbies!"),
                    c("&7Use this to stay with your friends.")
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
            } else if (item.material() == Material.COMPASS) {
                openGameMenu(player);
                event.setCancelled(true);
            } else if (item.material() == Material.CHEST || item.material() == Material.PLAYER_HEAD) {
                event.setCancelled(true);
            }
        });

        handler.addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            if (event.getInventory() instanceof Inventory inv) {
                if (inv.getTitle().equals(c("Lobby Selector"))) {
                    int slot = event.getSlot();
                    if (slot >= 0 && slot <= 4) {
                        List<ServerPing> lobbies = ServerLookup.findByPrefix("lobby");
                        if (slot < lobbies.size()) {
                            ServerPing target = lobbies.get(slot);
                            String currentName = Main.getServerName();
                            if (target.serverName().equals(currentName)) {
                                event.getPlayer().sendMessage(c("&cYou are already connected to this lobby!"));
                            } else {
                                event.getPlayer().sendMessage(c("&aConnecting to Lobby " + (slot + 1) + "..."));
                                fun.ascent.lobby.transfer.ProxyTransfer.send(event.getPlayer(), target.serverName());
                            }
                        }
                    }
                } else if (inv.getTitle().equals(c("Game Menu"))) {
                    int slot = event.getSlot();
                    if (slot == 10) {
                        List<ServerPing> skyblocks = ServerLookup.findByPrefix("skyblock");
                        if (!skyblocks.isEmpty()) {
                            ServerPing target = skyblocks.getFirst();
                            event.getPlayer().sendMessage(c("&aConnecting to Skyblock..."));
                            fun.ascent.lobby.transfer.ProxyTransfer.send(event.getPlayer(), target.serverName());
                        } else {
                            event.getPlayer().sendMessage(c("&cSkyblock is currently offline!"));
                        }
                    }
                }
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
        
        player.sendMessage(c(newHiddenState ? "&cPlayers are now hidden." : "&aPlayers are now visible."));
    }

    private static void openLobbySelector(Player player) {
        Inventory inventory = new Inventory(InventoryType.CHEST_2_ROW, c("Lobby Selector"));

        // Fetch live lobby servers from Redis
        List<ServerPing> lobbies = ServerLookup.findByPrefix("lobby");
        String currentName = Main.getServerName();

        for (int i = 0; i < MAX_LOBBY_SLOTS; i++) {
            String lobbyDisplayName = "Lobby " + (i + 1);

            if (i < lobbies.size()) {
                ServerPing lobby = lobbies.get(i);
                boolean isCurrent = lobby.serverName().equals(currentName);

                Material mat = isCurrent ? Material.RED_CONCRETE : Material.WHITE_CONCRETE;
                String title = isCurrent ? "&c" + lobbyDisplayName + " &7(Current)" : "&a" + lobbyDisplayName;

                ItemStack lobbyItem = ItemStack.builder(mat)
                        .set(DataComponents.CUSTOM_NAME, c(title))
                        .set(DataComponents.LORE, List.of(
                                c("&7Players: &a" + lobby.onlinePlayers()),
                                Component.empty(),
                                isCurrent ? c("&cAlready connected!") : c("&7Click to connect!")))
                        .build();
                inventory.setItemStack(i, lobbyItem);
            } else {
                // Offline
                ItemStack offlineItem = ItemStack.builder(Material.BARRIER)
                        .set(DataComponents.CUSTOM_NAME, c("&c" + lobbyDisplayName + " &8(Offline)"))
                        .set(DataComponents.LORE, List.of(c("&7This lobby is not available.")))
                        .build();
                inventory.setItemStack(i, offlineItem);
            }
        }
        player.openInventory(inventory);
    }

    private static void openGameMenu(Player player) {
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, c("Game Menu"));
        
        String textureHash = "686718d85e25b006f2c8f160f619b23c8fd6ae75ddf1c06308ec0f539d931703";
        String url = "http://textures.minecraft.net/texture/" + textureHash;
        String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        ResolvableProfile profile = new ResolvableProfile(
                new ResolvableProfile.Partial(
                        "", UUID.randomUUID(),
                        List.of(new GameProfile.Property("textures", base64, ""))));

        List<ServerPing> skyblocks = ServerLookup.findByPrefix("skyblock");
        int totalPlayers = skyblocks.stream().mapToInt(ServerPing::onlinePlayers).sum();

        ItemStack sbItem = ItemStack.builder(Material.PLAYER_HEAD)
                .set(DataComponents.CUSTOM_NAME, c("&aSkyBlock"))
                .set(DataComponents.PROFILE, profile)
                .set(DataComponents.LORE, List.of(
                        c("&7Play the popular SkyBlock game mode!"),
                        c("&7Complete quests, collect resources, and more!"),
                        Component.empty(),
                        c("&7Players: &a" + totalPlayers),
                        Component.empty(),
                        skyblocks.isEmpty() ? c("&cOffline!") : c("&eClick to play!")
                ))
                .build();
                
        inventory.setItemStack(10, sbItem);
        player.openInventory(inventory);
    }
}
