package fun.ascent.lobby.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.gui.RefreshingGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.redis.ServerPing;
import fun.ascent.lobby.cache.ServerInfoCache;
import fun.ascent.lobby.game.GameType;
import fun.ascent.lobby.transfer.ProxyTransfer;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.player.ResolvableProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameMenuGUI extends InventoryGUI implements RefreshingGUI {
    private static final int[] GAME_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            21,     23,
            28, 29, 30, 31, 32, 33, 33,
            37, 38, 39, 40, 41, 42, 43
    };

    private int cycleIndex = 0;

    public GameMenuGUI() {
        super("Game Menu", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        Player player = e.player();

        set(new GUIClickableItem(4) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {

            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack("§aMain Lobby",
                        Material.BOOKSHELF, 1,
                        "",
                        "§7Return to the Main Lobby.");
            }
        });

        // Refresh cache, then populate items
        ServerInfoCache.refreshCache().thenAccept(servers -> {
            int i = 0;
            for (GameType game : GameType.values()) {
                if (i >= GAME_SLOTS.length) break;
                set(createGameItem(game, GAME_SLOTS[i++]));
            }
            set(createRandomGameItem(49));
            updateItemStacks(getInventory(), player);
        });

        updateItemStacks(getInventory(), player);
    }

    private GUIClickableItem createGameItem(GameType game, int slot) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(Player player) {
                String playerCount = StringUtility.commaify(game.getPlayerCount());
                ItemStack.Builder itemBuilder = ItemStackCreator.getFromStack(game.getItem().build());

                List<String> lore = new ArrayList<>();
                lore.add("§8" + StringUtility.toNormalCase(game.getCategory().name()));
                lore.add("");
                lore.addAll(Arrays.asList(game.getLore()));
                lore.add("");
                if (game.isImplemented()) {
                    if (cycleIndex % 2 == 0) {
                        lore.add("§a  Click to Connect!");
                    } else {
                        lore.add("§a► Click to Connect!");
                    }
                    lore.add("§7" + playerCount + " currently playing!");
                }

                return ItemStackCreator.appendLore(itemBuilder, lore).customName(
                        Component.text("§a" + game.getDisplayName())
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                if (!game.isImplemented()) {
                    player.sendMessage("§cThis game is not yet available!");
                    return;
                }
                
                List<fun.ascent.common.redis.ServerPing> servers = fun.ascent.common.redis.ServerLookup.findByPrefix(game.getServerPrefix());
                if (servers.isEmpty()) {
                    player.sendMessage("§c" + game.getDisplayName() + " is currently offline!");
                    return;
                }
                
                player.closeInventory();
                fun.ascent.lobby.transfer.ProxyTransfer.send(player, servers.getFirst().serverName());
            }
        };
    }

    private GUIClickableItem createRandomGameItem(int slot) {
        GameType displayGame = GameType.values()[cycleIndex % GameType.values().length];
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(Player player) {
                ItemStack base = displayGame.getItem().build();
                ItemStack.Builder builder = ItemStack.builder(base.material())
                        .amount(1)
                        .customName(Component.text("§aRandom Game"))
                        .lore(
                                Component.text("§7Join a random game."),
                                Component.empty(),
                                Component.text("§eClick to Play")
                        );

                // Copy head texture if present
                ResolvableProfile profile = base.get(DataComponents.PROFILE);
                if (profile != null) {
                    builder.set(DataComponents.PROFILE, profile);
                }

                return builder;
            }

            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                List<GameType> implemented = Arrays.stream(GameType.values())
                        .filter(GameType::isImplemented)
                        .toList();
                if (implemented.isEmpty()) {
                    player.sendMessage("§cNo games available!");
                    return;
                }
                GameType random = implemented.get(
                        ThreadLocalRandom.current().nextInt(implemented.size())
                );
                
                List<ServerPing> servers = ServerLookup.findByPrefix(random.getServerPrefix());
                if (servers.isEmpty()) {
                    player.sendMessage("§c" + random.getDisplayName() + " is currently offline!");
                    return;
                }

                player.closeInventory();
                ProxyTransfer.send(player, servers.getFirst().serverName());
            }
        };
    }

    @Override
    public void refreshItems(Player player) {
        cycleIndex++;
        set(createRandomGameItem(49));
    }

    @Override
    public int refreshRate() {
        return 10; // 0.5 seconds
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
