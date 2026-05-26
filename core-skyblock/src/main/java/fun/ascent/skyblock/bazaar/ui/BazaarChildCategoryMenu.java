package fun.ascent.skyblock.bazaar.ui;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;

import java.util.*;

public class BazaarChildCategoryMenu {

    public static final HashMap<SkyblockPlayer,BazaarEntry> curCategory = new HashMap<>();

    public static void open(SkyblockPlayer player, BazaarEntry category){
        Inventory inventory = new Inventory(category.getSize(),category.titleComp);
        curCategory.put(player,category);
        fill(inventory);
        for (BazaarEntry child : category.children) {
            inventory.setItemStack(child.slot,child.getStack(false));
        }
        addUtiltiy(inventory,player);
        player.openInventory(inventory);
        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            mouseClick(event.getSlot(), event.getClickedItem(), (SkyblockPlayer) event.getPlayer(), event.getInventory());
        });
    }

    private static void mouseClick(int slot, ItemStack clickedItem, SkyblockPlayer player, AbstractInventory inventory) {
        // TODO: Handle clicks for child category menu
        int slotBase = inventory.getInnerSize() - 5;
        if(slot == slotBase){
            player.closeInventory();
            return;
        }
        BazaarEntry cur = curCategory.get(player);
        if(slot == (slotBase - 1)){
            if(cur == null) return;
            if(cur.parentEntry == null){
                player.closeInventory();
                return;
            }
            if(cur.parentEntry.parent == null){
                BazaarCategoryMenu.openMenu(player,cur.parentEntry);
            }else {
                BazaarChildCategoryMenu.open(player,cur.parentEntry);
            }
            return;
        }
        if(slot == (slotBase - 2)){
            if (cur != null) {
                BazaarCategoryMenu.sellInventory(player, cur);
            }
            return;
        }
        for (BazaarEntry child : cur.children) {
            if(child.slot == slot){
                BazaarItemBuyMenu.open(player,child);
                return;
            }
        }
    }

    public static void addUtiltiy(Inventory inventory,SkyblockPlayer player){
        ItemStack close = ItemStack.of(Material.BARRIER)
                .withCustomName(StringUtility.text("<red>Close")).withLore(List.of());
        int slot = inventory.getInnerSize() - 5;
        inventory.setItemStack(slot,close);

        ItemStack back = ItemStack.of(Material.ARROW).withCustomName(StringUtility.text("<green>Go Back"))
                .withLore(StringUtility.text("<gray>To Bazaar"));
        int slot_back = slot-1;
        inventory.setItemStack(slot_back,back);

        BazaarEntry category = curCategory.get(player);
        if(category == null)return;
        List<Component> sellLore = new ArrayList<>(List.of(
                StringUtility.text("<gray>Instantly sell all ").append(
                        category.nameComp.color(NamedTextColor.GRAY)
                ),
                StringUtility.text("<gray>derivatives in your inventory."),
                Component.empty()
        ));
        HashMap<String,Integer> amounts = new HashMap<>();
        for(ItemStack stack : player.getInventory().getItemStacks()){
            SkyblockItem item = SkyblockItem.fromStack(stack);
            if(item == null) continue;
            BazaarEntry bzEntry = BazaarRegistry.itemToEntryMap.get(item.getItemId());
            if(bzEntry == null) continue;
            
            boolean isChild = false;
            BazaarEntry curr = bzEntry;
            while (curr != null) {
                if (curr.id.equals(category.id)) {
                    isChild = true;
                    break;
                }
                curr = curr.parentEntry;
            }
            if (!isChild) continue;
            
            int am = stack.amount();
            if(amounts.containsKey(item.getItemId())){
                am += amounts.get(item.getItemId());
            }
            amounts.put(item.getItemId(),am);
        }
        if(amounts.isEmpty()){
            sellLore.add(StringUtility.text("<red>You don't have anything to sell!"));
        }else{
            double finalPrice = 0.0;
            for(Map.Entry<String,Integer> entry : amounts.entrySet()){
                SkyblockItem sbItem = ItemRegistry.getItem(entry.getKey());
                BazaarEntry bzEntry = BazaarRegistry.itemToEntryMap.get(entry.getKey());
                
                double price = 0.0;
                if (bzEntry != null) {
                    price = entry.getValue() * BZPriceRegistry.getSell(bzEntry);
                }
                finalPrice += price;
                
                String text = "<green>" + entry.getValue() + "<gray>x " + sbItem.getRarity().getRarityColor() +
                        sbItem.getDisplayName() + " <gray>for <gold> " + price + " coins";
                sellLore.add(StringUtility.text(text));
            }
            sellLore.add(Component.empty());
            sellLore.add(StringUtility.text("<gray>You earn: <gold>" + finalPrice + " coins."));
            sellLore.add(Component.empty());
            sellLore.add(StringUtility.text("<yellow>Click to sell!"));
        }
        ItemStack sell = ItemStack.of(Material.CRAFTING_TABLE).withCustomName(StringUtility.text("<green>Sell Inventory Now"))
                .withLore(sellLore);
        int slot_sell = slot-2;
        inventory.setItemStack(slot_sell,sell);
    }

    public static void fill(Inventory inventory){
        ItemStack stack = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
                .withCustomName(Component.empty()).withLore(List.of())
                .with(DataComponents.TOOLTIP_DISPLAY,new TooltipDisplay(true, Set.of()));
        for(int i = 0; i < inventory.getInnerSize();i++){
            inventory.setItemStack(i,stack);
        }
    }
}
