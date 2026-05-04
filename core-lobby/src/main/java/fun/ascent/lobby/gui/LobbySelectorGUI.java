package fun.ascent.lobby.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.redis.ServerPing;
import fun.ascent.lobby.Main;
import fun.ascent.lobby.cache.ServerInfoCache;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

import static fun.ascent.common.StringUtility.color;

public class LobbySelectorGUI extends InventoryGUI {

    private static final int MAX_LOBBY_SLOTS = 5;

    public LobbySelectorGUI() {
        super("Main Lobby Selector", InventoryType.CHEST_2_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        Player player = e.player();
        ServerInfoCache.getServers().thenAccept(lobbies -> {
            String currentName = Main.getServerName();

            // Filter for lobbies only
            List<ServerPing> lobbyList = lobbies.stream()
                    .filter(s -> s.serverName().startsWith("lobby"))
                    .toList();

            for (int i = 0; i < MAX_LOBBY_SLOTS; i++) {
                final int slot = i;
                if (i < lobbyList.size()) {
                    ServerPing lobby = lobbyList.get(i);
                    boolean isCurrent = lobby.serverName().equals(currentName);

                    set(new GUIClickableItem(slot) {
                        @Override
                        public ItemStack.Builder getItem(Player player) {
                            Material mat = isCurrent ? Material.RED_CONCRETE : Material.WHITE_CONCRETE;
                            String title = isCurrent ? "&c" + "Main Lobby #" + (slot + 1) : "&a" + "Main Lobby #" + (slot + 1);

                            return ItemStackCreator.createNamedItemStack(mat, color(title))
                                    .set(net.minestom.server.component.DataComponents.LORE, List.of(
                                            color("&7Players: " + StringUtility.commaify(lobby.onlinePlayers()) + "/100"),
                                            Component.empty(),
                                            isCurrent ? color("&cAlready connected!") : color("&eClick to connect!")));
                        }

                        @Override
                        public void run(InventoryPreClickEvent e, Player player) {
                            if (isCurrent) {
                                player.sendMessage(color("&cYou are already connected to this lobby!"));
                            } else {
                                player.sendMessage(color("&aConnecting to Main Lobby #" + (slot + 1) + "..."));
                                fun.ascent.lobby.transfer.ProxyTransfer.send(player, lobby.serverName());
                            }
                        }
                    });
                } else {
                    set(new GUIClickableItem(slot) {
                        @Override
                        public ItemStack.Builder getItem(Player player) {
                            return ItemStackCreator.createNamedItemStack(Material.BARRIER, color("&c" + "Main Lobby #" + (slot + 1)))
                                    .set(net.minestom.server.component.DataComponents.LORE, List.of(color("&7This lobby is not available.")));
                        }

                        @Override
                        public void run(InventoryPreClickEvent e, Player player) {
                            player.sendMessage(color("&cThis lobby is currently offline!"));
                        }
                    });
                }
            }
            updateItemStacks(getInventory(), player);
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
