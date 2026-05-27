package fun.ascent.skyblock.bazaar.ui;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.bazaar.ui.extras.SellInvConfirmMenu;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static fun.ascent.skyblock.bazaar.ui.BazaarCategoryMenu.*;

public class BazaarSearchMenu {

    private static final int[] RESULT_SLOTS = {
        11, 12, 13, 14, 15, 16,
        20, 21, 22, 23, 24, 25,
        29, 30, 31, 32, 33, 34,
        38, 39, 40, 41, 42, 43
    };

    public static final HashMap<SkyblockPlayer,String> searchMap = new HashMap<>();
    public static final HashMap<SkyblockPlayer,HashMap<Integer,BazaarEntry>> searchResults = new HashMap<>();


    public static void open(SkyblockPlayer player,String search){
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW,
                StringUtility.text("<dark_gray>Bazaar ➔ \"" + search + "\""));
        searchMap.put(player,search);

        populateSearchResults(player, search);
        Material COLOR = Material.LIME_STAINED_GLASS_PANE;
        addBorders(inventory,COLOR);
        addCategories(inventory, null);
        addUtility(inventory,player,null);
        addResults(inventory,searchResults.get(player));
        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            if(event.getSlot() == 45){
                if(event.getClick() instanceof Click.Left){
                    BazaarCategoryMenu.openSearchBar((SkyblockPlayer) event.getPlayer());
                }else {
                    BazaarCategoryMenu.openMenu((SkyblockPlayer) event.getPlayer(),BazaarRegistry.bazaarItemList.getFarming());
                }
            } else {
                mouseClick(event.getSlot(), event.getClickedItem(), (SkyblockPlayer) event.getPlayer(), event.getInventory());
            }
        });
        player.openInventory(inventory);
    }

    public static void populateSearchResults(SkyblockPlayer player, String search) {
        HashMap<Integer, BazaarEntry> results = new HashMap<>();
        if (search == null || search.isBlank()) {
            searchResults.put(player, results);
            return;
        }

        String query = search.toLowerCase();
        List<BazaarEntry> matched = new ArrayList<>();
        for (BazaarEntry entry : BazaarRegistry.itemToEntryMap.values()) {
            if (entry.children != null) continue;

            String id = entry.id.toLowerCase();
            String name = "";
            if (entry.itemToSell != null && entry.itemToSell.getDisplayName() != null) {
                name = entry.itemToSell.getDisplayName().toLowerCase();
            } else if (entry.nameComp != null) {
                name = StringUtility.getTextFromComponent(entry.nameComp).toLowerCase();
            } else if (entry.itemName != null) {
                name = entry.itemName.toLowerCase();
            }

            if (id.contains(query) || name.contains(query)) {
                matched.add(entry);
            }
        }

        matched.sort((a, b) -> {
            int scoreA = getSearchScore(a, query);
            int scoreB = getSearchScore(b, query);
            if (scoreA != scoreB) {
                return Integer.compare(scoreB, scoreA);
            }
            String nameA = a.itemToSell != null ? a.itemToSell.getDisplayName() : a.id;
            String nameB = b.itemToSell != null ? b.itemToSell.getDisplayName() : b.id;
            return nameA.compareToIgnoreCase(nameB);
        });

        int max = Math.min(24, matched.size());
        for (int i = 0; i < max; i++) {
            int slot = RESULT_SLOTS[i];
            results.put(slot, matched.get(i));
        }

        searchResults.put(player, results);
    }

    private static int getSearchScore(BazaarEntry entry, String query) {
        String id = entry.id.toLowerCase();
        String name = "";
        if (entry.itemToSell != null && entry.itemToSell.getDisplayName() != null) {
            name = entry.itemToSell.getDisplayName().toLowerCase();
        } else if (entry.nameComp != null) {
            name = StringUtility.getTextFromComponent(entry.nameComp).toLowerCase();
        } else if (entry.itemName != null) {
            name = entry.itemName.toLowerCase();
        }

        if (id.equals(query) || name.equals(query)) {
            return 3;
        } else if (id.startsWith(query) || name.startsWith(query)) {
            return 2;
        } else if (id.contains(query) || name.contains(query)) {
            return 1;
        }
        return 0;
    }

    private static void addResults(Inventory inventory, HashMap<Integer, BazaarEntry> results) {
        if (results == null) return;
        results.forEach((slot, entry) -> inventory.setItemStack(slot, entry.getStack(false)));
    }

    private static void mouseClick(int slot, ItemStack clickedItem, SkyblockPlayer player, AbstractInventory inventory) {
        if(slot == 47){
            SellInvConfirmMenu.open(player,BazaarRegistry.bazaarItemList.getFarming(),null);
            return;
        }
        if(slot == 49){
            BazaarCategoryMenu.openMenu(player,BazaarRegistry.bazaarItemList.getFarming());
            return;
        }
        if(slot == 50){
            player.sendMessage(StringUtility.text("<red>This Feature has not yet been implemented."));
            return;
        }
        if(slot % 9 == 0){
            int index = slot / 9;
            if(BazaarRegistry.bazaarItemList.bazaarData.size() <= index) return;
            BazaarEntry category = BazaarRegistry.bazaarItemList.bazaarData.get(index);
            if(category != null && category.parent == null){
                BazaarCategoryMenu.openMenu(player,category);
            }
        }
        HashMap<Integer,BazaarEntry> entryMap = searchResults.getOrDefault(player,new HashMap<>());
        if(entryMap.isEmpty()) return;
        if(entryMap.containsKey(slot)){
            BazaarItemBuyMenu.open(player,entryMap.get(slot));
        }
    }

    public static void addUtility(Inventory inventory,SkyblockPlayer player, BazaarEntry category){
        // Search Bar
        ItemStack search = ItemStack.of(Material.OAK_SIGN)
                .withCustomName(StringUtility.text("<green>Search"))
                .withLore(
                        StringUtility.text("<gray>Find products by name!"),
                        Component.empty(),
                        StringUtility.text("<gray>Query: <green>" + searchMap.getOrDefault(player,"")),
                        Component.empty(),
                        StringUtility.text("<aqua>Right-Click to clear!"),
                        StringUtility.text("<yellow>Click to edit filter!")
                );
        inventory.setItemStack(45, search);

        // Sell Inventory
        ItemStack sell = ItemStack.of(Material.CHEST)
                .withCustomName(StringUtility.text("<green>Sell Inventory Now"));
        List<Component> lore = new ArrayList<>(
                List.of(
                        StringUtility.text("<gray>Instantly sell any items in your"),
                        StringUtility.text("<gray>inventory that can be sold on the"),
                        StringUtility.text("<gray>Bazaar."),
                        Component.empty()
                )
        );
        HashMap<String,Integer> amounts = new HashMap<>();

        double[] finalPrice = {0.0};
        for(ItemStack stack : player.getInventory().getItemStacks()){
            SkyblockItem item = SkyblockItem.fromStack(stack);
            if(item == null) continue;
            BazaarEntry bzEntry = BazaarRegistry.itemToEntryMap.get(item.getItemId());
            if(bzEntry == null) continue;

            int amount = stack.amount();
            if(amounts.containsKey(item.getItemId())){
                amount += amounts.get(item.getItemId());
            }
            amounts.put(item.getItemId(),amount);
        }
        if(!amounts.isEmpty()) {
            amounts.forEach((item, amount) -> {
                SkyblockItem sbItem = ItemRegistry.getItem(item);
                BazaarEntry bzEntry = BazaarRegistry.itemToEntryMap.get(item);

                double price = 0.0;
                if (bzEntry != null) {
                    price = amount * BZPriceRegistry.getSell(bzEntry) * (1.0 - BazaarRegistry.sellTax / 100.0);
                }
                finalPrice[0] += price;

                String text = "<green>" + amount + "<gray>x " + sbItem.getRarity().getRarityColor() +
                        sbItem.getDisplayName() + " <gray>for <gold> " + price + " coins";
                lore.add(StringUtility.text(text));
            });
            lore.add(Component.empty());
            lore.add(StringUtility.text("<gray>You earn: <gold>" + finalPrice[0] + " coins"));
            lore.add(Component.empty());
            lore.add(StringUtility.text("<yellow>Click to sell!"));
        }
        sell = sell.withLore(lore);
        inventory.setItemStack(47, sell);

        // Close
        ItemStack close = ItemStack.of(Material.ARROW).withLore(
                StringUtility.text("<gray>Cancels the Search")
                )
                .withCustomName(StringUtility.text("<green>Go Back"));
        inventory.setItemStack(49,close);

        ItemStack manage = ItemStack.of(Material.BOOK).withCustomName(
                StringUtility.text("<green>Manage Orders")
        ).withLore(StringUtility.text("<gray>You don't have any ongoing orders."),
                Component.empty(),StringUtility.text("<yellow>Click to manage!"));
        inventory.setItemStack(50,manage);
    }

}
