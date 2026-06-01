package fun.ascent.skyblock.shop;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;

import java.util.*;

public class ShopMenu {

    private static final int[] DEFAULT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private static final int BUYBACK_SLOT = 49;
    private static final int PREVIOUS_PAGE_SLOT = 45;
    private static final int NEXT_PAGE_SLOT = 53;

    public static void open(SkyblockPlayer player, String shopId) {
        open(player, shopId, 0);
    }

    private static void open(SkyblockPlayer player, String shopId, int page) {
        if (player == null) return;
        if (shopId == null || shopId.isEmpty()) {
            System.out.println("[SHOP] Invalid SHOP ID");
            return;
        }

        ShopData data = ShopRegistry.get(shopId);
        if (data == null) return;

        int maxPage = Math.max(0, (int) Math.ceil((double) data.getItems().size() / DEFAULT_SLOTS.length) - 1);
        int currentPage = Math.max(0, Math.min(page, maxPage));
        Component title = StringUtility.text(data.shopTitle + (maxPage > 0 ? " <white>| Page " + (currentPage + 1) + "/" + (maxPage + 1) : ""));
        Inventory inv = new Inventory(data.invType, title);
        Map<Integer, ShopData.ShopItem> slotMap = new HashMap<>();

        constructBorders(inv);
        renderItems(player, inv, data, currentPage, slotMap);
        renderBuyback(player, inv);
        renderPagination(inv, currentPage, maxPage);

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            if (event.getInventory() != inv) return;
            event.setCancelled(true);

            if (!(event.getPlayer() instanceof SkyblockPlayer clickingPlayer)) return;
            int slot = event.getSlot();

            if (slot >= inv.getSize()) {
                handleSellClick(clickingPlayer, event, shopId, currentPage);
                return;
            }

            if (slot == BUYBACK_SLOT) {
                handleBuyback(clickingPlayer, shopId, currentPage);
                return;
            }

            if (slot == PREVIOUS_PAGE_SLOT && currentPage > 0) {
                open(clickingPlayer, shopId, currentPage - 1);
                return;
            }

            if (slot == NEXT_PAGE_SLOT && currentPage < maxPage) {
                open(clickingPlayer, shopId, currentPage + 1);
                return;
            }

            ShopData.ShopItem clickedItem = slotMap.get(slot);
            if (clickedItem == null) return;

            if (clickedItem.stackable() && event.getClick() instanceof Click.Right) {
                openTradingOptions(clickingPlayer, shopId, currentPage, clickedItem);
                return;
            }

            handlePurchase(clickingPlayer, clickedItem, clickedItem.amount(), clickedItem.price(), shopId, currentPage);
        });

        player.openInventory(inv);
    }

    private static void renderItems(SkyblockPlayer player, Inventory inv, ShopData data, int page, Map<Integer, ShopData.ShopItem> slotMap) {
        int start = page * DEFAULT_SLOTS.length;
        for (int i = 0; i < DEFAULT_SLOTS.length; i++) {
            int itemIndex = start + i;
            if (itemIndex >= data.getItems().size()) break;

            ShopData.ShopItem item = data.getItems().get(itemIndex);
            int slot = DEFAULT_SLOTS[i];
            inv.setItemStack(slot, buildDisplayItem(player, item));
            slotMap.put(slot, item);
        }
    }

    private static void constructBorders(Inventory inv) {
        ItemStack fill = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
                .customName(Component.empty()).lore(Component.empty())
                .set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of(DataComponents.ATTRIBUTE_MODIFIERS)))
                .build();

        int size = inv.getSize();
        for (int i = 0; i < size; i++) {
            inv.setItemStack(i, fill);
        }
        for (int slot : DEFAULT_SLOTS) {
            if (slot < size) inv.setItemStack(slot, ItemStack.AIR);
        }
    }

    private static void renderBuyback(SkyblockPlayer player, Inventory inv) {
        if (!player.getShoppingData().hasAnythingToBuyback()) {
            inv.setItemStack(BUYBACK_SLOT, ItemStack.builder(Material.HOPPER)
                    .customName(StringUtility.text("<green>Sell Item"))
                    .lore(List.of(
                            StringUtility.text("<gray>Click items in your inventory to"),
                            StringUtility.text("<gray>sell them to this Shop!")
                    ))
                    .build());
            return;
        }

        PlayerShopData.BuybackData buyback = player.getShoppingData().lastBuyback();
        SkyblockItem item = buyback.item();
        double price = item.getNpcSellPrice() * buyback.amount();
        List<Component> lore = new ArrayList<>(item.buildItemStack(player).get(DataComponents.LORE));
        lore.add(Component.empty());
        lore.add(StringUtility.text("<gray>Cost"));
        lore.add(StringUtility.text("<gold>" + StringUtility.commaify(price) + " Coin" + (price != 1 ? "s" : "")));
        lore.add(Component.empty());
        lore.add(StringUtility.text("<yellow>Click to buyback!"));

        inv.setItemStack(BUYBACK_SLOT, item.buildItemStack(player).withAmount(buyback.amount()).withLore(lore));
    }

    private static void renderPagination(Inventory inv, int currentPage, int maxPage) {
        if (currentPage > 0) {
            inv.setItemStack(PREVIOUS_PAGE_SLOT, ItemStack.builder(Material.ARROW)
                    .customName(StringUtility.text("<green>Previous Page"))
                    .build());
        }
        if (currentPage < maxPage) {
            inv.setItemStack(NEXT_PAGE_SLOT, ItemStack.builder(Material.ARROW)
                    .customName(StringUtility.text("<green>Next Page"))
                    .build());
        }
    }

    private static ItemStack buildDisplayItem(SkyblockPlayer player, ShopData.ShopItem shopItem) {
        if (shopItem.item() == null) {
            System.err.println("[Shop] WARNING: Tried to display a null SkyblockItem!");
            return ItemStack.of(Material.BARRIER).withCustomName(Component.text("BROKEN ITEM").color(NamedTextColor.RED));
        }

        ItemStack baseItem = shopItem.item().buildItemStack(player).withAmount(shopItem.amount());
        List<Component> baseLore = baseItem.get(DataComponents.LORE);
        List<Component> newLore = baseLore != null ? new ArrayList<>(baseLore) : new ArrayList<>();

        newLore.add(Component.empty());
        newLore.add(StringUtility.text("<gray>Cost"));
        newLore.addAll(shopItem.price().getGUIDisplay());
        newLore.add(Component.empty());

        if (shopItem.hasStock()) {
            newLore.add(StringUtility.text("<gray>Stock"));
            newLore.add(StringUtility.text("<gold>" + player.getShoppingData().getStock(shopItem.item(), shopItem.stockLimit()) + " <gray>remaining"));
            newLore.add(Component.empty());
        }

        newLore.add(StringUtility.text("<yellow>Click to trade!"));
        if (shopItem.stackable()) {
            newLore.add(StringUtility.text("<yellow>Right-click for more trading options!"));
        }

        return baseItem.withLore(newLore);
    }

    private static void openTradingOptions(SkyblockPlayer player, String shopId, int page, ShopData.ShopItem item) {
        Inventory inv = new Inventory(net.minestom.server.inventory.InventoryType.CHEST_6_ROW, StringUtility.text("<gray>Trading Options"));
        constructBorders(inv);

        int[] amounts = {1, 5, 10, 32, 64};
        int[] slots = {20, 21, 22, 23, 24};
        ShopPrice singlePrice = item.stackPrice();
        for (int i = 0; i < amounts.length; i++) {
            int amount = amounts[i];
            inv.setItemStack(slots[i], buildTradeOption(player, item, amount, singlePrice));
        }
        inv.setItemStack(BUYBACK_SLOT, ItemStack.builder(Material.ARROW).customName(StringUtility.text("<green>Go Back")).build());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            if (event.getInventory() != inv) return;
            event.setCancelled(true);
            if (!(event.getPlayer() instanceof SkyblockPlayer clickingPlayer)) return;

            if (event.getSlot() == BUYBACK_SLOT) {
                open(clickingPlayer, shopId, page);
                return;
            }

            for (int i = 0; i < slots.length; i++) {
                if (event.getSlot() == slots[i]) {
                    handlePurchase(clickingPlayer, item, amounts[i], singlePrice.multiply(amounts[i]), shopId, page);
                    return;
                }
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack buildTradeOption(SkyblockPlayer player, ShopData.ShopItem item, int amount, ShopPrice singlePrice) {
        ShopPrice totalPrice = singlePrice.multiply(amount);
        ItemStack base = item.item().buildItemStack(player).withAmount(amount);
        List<Component> lore = new ArrayList<>(Optional.ofNullable(base.get(DataComponents.LORE)).orElse(List.of()));
        lore.add(Component.empty());
        lore.add(StringUtility.text("<gray>Cost"));
        lore.addAll(totalPrice.getGUIDisplay());
        lore.add(Component.empty());
        lore.add(StringUtility.text("<gray>Stock"));
        lore.add(StringUtility.text("<gold>" + player.getShoppingData().getStock(item.item(), item.stockLimit()) + " <gray>remaining"));
        lore.add(Component.empty());
        lore.add(StringUtility.text("<yellow>Click to purchase!"));

        return base.withCustomName(StringUtility.text(item.item().getRarity().getColor() + item.item().getDisplayName() + " <dark_gray>x" + amount))
                .withLore(lore);
    }

    private static void handlePurchase(SkyblockPlayer player, ShopData.ShopItem itemData, int amount, ShopPrice price, String shopId, int page) {
        if (itemData.item() == null) {
            player.sendMessage(Component.text("You cannot buy a null item").color(NamedTextColor.RED));
            return;
        }

        if (itemData.hasStock() && !player.getShoppingData().canPurchase(itemData.item(), amount, itemData.stockLimit())) {
            player.sendMessage(StringUtility.text("<red>You have reached the maximum amount of items you can buy!"));
            return;
        }

        if (!price.canAfford(player)) {
            player.sendMessage(StringUtility.text("<red>You don't have enough " + price.getNamePlural() + "!"));
            player.playSound(Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.PLAYER, 1f, 0.5f));
            return;
        }

        price.processPurchase(player);
        player.getInventory().addItemStack(itemData.item().buildItemStack(player).withAmount(amount));
        player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
        if (itemData.hasStock()) {
            player.getShoppingData().documentPurchase(itemData.item(), amount);
        }
        open(player, shopId, page);
    }

    private static void handleSellClick(SkyblockPlayer player, InventoryPreClickEvent event, String shopId, int page) {
        ItemStack stack = event.getClickedItem();
        if (stack == null || stack.isAir()) return;

        SkyblockItem item = SkyblockItem.fromStack(stack);
        if (item == null || item.getNpcSellPrice() <= 0) {
            player.sendMessage(StringUtility.text("<red>You can't sell this item!"));
            return;
        }

        double sellPrice = item.getNpcSellPrice() * stack.amount();
        player.getShoppingData().pushBuyback(item, stack.amount());
        player.addCoins(sellPrice);
        player.getInventory().setItemStack(event.getSlot() - event.getInventory().getSize(), ItemStack.AIR);

        Component name = stack.get(DataComponents.CUSTOM_NAME);
        String display = name == null ? item.getDisplayName() : PlainTextComponentSerializer.plainText().serialize(name);
        player.sendMessage(StringUtility.text("<green>You sold <white>" + display + " <green>for <gold>" + StringUtility.commaify(sellPrice) + " Coin" + (sellPrice != 1 ? "s" : "") + "<green>!"));
        open(player, shopId, page);
    }

    private static void handleBuyback(SkyblockPlayer player, String shopId, int page) {
        if (!player.getShoppingData().hasAnythingToBuyback()) return;

        PlayerShopData.BuybackData buyback = player.getShoppingData().lastBuyback();
        double price = buyback.item().getNpcSellPrice() * buyback.amount();
        if (player.getCoins() < price) {
            player.sendMessage(StringUtility.text("<red>You don't have enough coins!"));
            return;
        }

        player.removeCoins(price);
        player.getInventory().addItemStack(buyback.item().buildItemStack(player).withAmount(buyback.amount()));
        player.getShoppingData().popBuyback();
        player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
        open(player, shopId, page);
    }
}
