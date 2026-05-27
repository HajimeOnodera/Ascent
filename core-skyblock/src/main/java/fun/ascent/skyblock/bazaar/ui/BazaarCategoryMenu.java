package fun.ascent.skyblock.bazaar.ui;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.BazaarData;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.bazaar.ui.extras.SellInvConfirmMenu;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerEditSignEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.coordinate.Point;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.minestom.server.event.EventListener;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.key.Key;

import java.util.*;

public class BazaarCategoryMenu {

    public static final HashMap<SkyblockPlayer,BazaarEntry> curCategory = new HashMap<>();
    public static void openMenu(SkyblockPlayer player, BazaarEntry category){
        if(category.parent != null){
            System.err.println("[ERROR] User Tried to open category menu for a non category");
            return;
        }
        Inventory inventory = new Inventory(category.getSize(),
                category.titleComp);
        Material COLOR = switch (category.id.toUpperCase()){
            case "FARMING" -> Material.YELLOW_STAINED_GLASS_PANE;
            case "MINING" -> Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            case "COMBAT" -> Material.RED_STAINED_GLASS_PANE;
            case "WOOD_FISHES" -> Material.ORANGE_STAINED_GLASS_PANE;
            case "ODDITIES" -> Material.PINK_STAINED_GLASS_PANE;
            default -> Material.LIME_STAINED_GLASS_PANE;
        };

        addBorders(inventory,COLOR);
        addCategories(inventory, category);
        addChildren(inventory,category);
        addUtility(inventory,player,category);
        curCategory.put(player,category);
        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            mouseClick(event.getSlot(), event.getClickedItem(), (SkyblockPlayer) event.getPlayer(), event.getInventory());
        });
        player.openInventory(inventory);
    }

    private static void mouseClick(int slot, ItemStack clickedItem, SkyblockPlayer player, AbstractInventory inventory) {
        if(slot == 45){
            openSearchBar(player);
        }
        if(slot == 47){
            SellInvConfirmMenu.open(player,curCategory.get(player),null);
            return;
        }
        if(slot == 49){
            player.closeInventory();
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
                openMenu(player,category);
            }
        }
        if(curCategory.containsKey(player)){
            BazaarEntry category = curCategory.get(player);
            if(category.children == null || category.children.isEmpty()) return;
            for(BazaarEntry child : category.children){
                if(child.slot == slot){
                    BazaarChildCategoryMenu.open(player,child);
                    return;
                }
            }
        }

    }

    public static void openSearchBar(SkyblockPlayer player) {
        player.closeInventory();
        Point pos = player.getPosition();
        ListBinaryTag messages = ListBinaryTag.builder()
                .add(StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"))
                .add(StringBinaryTag.stringBinaryTag("{\"text\":\"^^^^^^^^^^^^^^^\"}"))
                .add(StringBinaryTag.stringBinaryTag("{\"text\":\"Enter query\"}"))
                .add(StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"))
                .build();
        CompoundBinaryTag frontText = CompoundBinaryTag.builder()
                .put("messages", messages)
                .build();
        Block signBlock = Block.OAK_SIGN.withNbt(CompoundBinaryTag.builder()
                .put("front_text", frontText)
                .build());

        player.sendPacket(new BlockChangePacket(pos, signBlock));
        player.sendPacket(new OpenSignEditorPacket(pos, true));

        EventListener<PlayerEditSignEvent> listener = EventListener.builder(PlayerEditSignEvent.class)
                .handler(event -> {
                    String query = event.getLines().getFirst();
                    if(query.isBlank()){
                        BazaarCategoryMenu.openMenu(player,BazaarRegistry.bazaarItemList.getFarming());
                        return;
                    }
                    BazaarSearchMenu.open(player,query);
                    player.sendMessage(StringUtility.text("<green>Searching for: " + query));
                    Block original = player.getInstance().getBlock(pos);
                    player.sendPacket(new BlockChangePacket(pos, original));
                })
                .expireCount(1)
                .build();
        player.eventNode().addListener(listener);
        return;
    }

    public static void sellInventory(SkyblockPlayer player, BazaarEntry category,BazaarEntry toReturn) {
        double totalCoins = 0.0;
        int itemsSold = 0;
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItemStack(i);
            if (stack.isAir()) continue;
            
            SkyblockItem item = SkyblockItem.fromStack(stack);
            if (item == null) continue;
            BazaarEntry bzEntry = BazaarRegistry.itemToEntryMap.get(item.getItemId());
            if (bzEntry == null) continue;
            
            if (category != null) {
                boolean isChild = false;
                BazaarEntry cur = bzEntry;
                while (cur != null) {
                    if (cur.id.equals(category.id)) {
                        isChild = true;
                        break;
                    }
                    cur = cur.parentEntry;
                }
                if (!isChild) continue;
            }
            
            int amount = stack.amount();
            double pricePerUnit = BZPriceRegistry.getSell(bzEntry);
            totalCoins += amount * pricePerUnit * (1.0 - BazaarRegistry.sellTax / 100.0);
            itemsSold += amount;
            
            inventory.setItemStack(i, ItemStack.AIR);
        }
        
        if (itemsSold > 0) {
            player.addCoins(totalCoins);
            player.sendMessage(StringUtility.text("<green>Sold " + itemsSold + " items for <gold>" + totalCoins + " coins!"));
            Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 1f);
            player.playSound(sound);
        } else {
            player.sendMessage(StringUtility.text("<red>You don't have any items to sell!"));
        }
        if(toReturn.parent != null){
            BazaarChildCategoryMenu.open(player,toReturn);
        }else{
            BazaarCategoryMenu.openMenu(player,toReturn);
        }
    }


    public static void addUtility(Inventory inventory,SkyblockPlayer player, BazaarEntry category){
        // Search Bar
        ItemStack search = ItemStack.of(Material.OAK_SIGN)
                .withCustomName(StringUtility.text("<green>Search"))
                .withLore(
                        StringUtility.text("<gray>Find products by name!"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to search!")
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
        ItemStack close = ItemStack.of(Material.BARRIER).withLore(Component.empty())
                .withCustomName(StringUtility.text("<red>Close"));
        inventory.setItemStack(49,close);

        ItemStack manage = ItemStack.of(Material.BOOK).withCustomName(
                StringUtility.text("<green>Manage Orders")
        ).withLore(StringUtility.text("<gray>You don't have any ongoing orders."),
                Component.empty(),StringUtility.text("<yellow>Click to manage!"));
        inventory.setItemStack(50,manage);
    }

    public static void addChildren(Inventory inventory, BazaarEntry category){
        category.children.forEach(entry -> inventory.setItemStack(entry.slot,entry.getStack(false)));
    }

    public static void addBorders(Inventory inventory, Material COLOR){
        ItemStack stack = ItemStack.of(COLOR)
                .with(DataComponents.TOOLTIP_DISPLAY,new TooltipDisplay(true, Set.of()))
                .withCustomName(Component.empty()).withLore(List.of());
        for(int i = 1;i < inventory.getSize(); i++){
            if(i % 9 == 0) continue;
            if((i - 1) % 9 != 0){
                if((i + 1) % 9 != 0) continue;
            }
            inventory.setItemStack(i,stack);
        }
        for(int i = 0;i < 9;i++){
            inventory.setItemStack(i,stack);
        }
        for(int i = 45;i < 54;i++){
            inventory.setItemStack(i,stack);
        }
    }
    public static void addCategories(Inventory inventory,BazaarEntry curCategory){
        BazaarData data = BazaarRegistry.bazaarItemList;
        for(int i = 0; i < data.bazaarData.size();i++){
            int slot = 9 * i;
            BazaarEntry entry = data.bazaarData.get(i);
            if(curCategory == null){
                inventory.setItemStack(slot,entry.getStack(false));
                continue;
            }
            inventory.setItemStack(slot,entry.getStack(curCategory.id.equals(entry.id)));
        }
    }
}
