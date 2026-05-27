package fun.ascent.skyblock.bazaar.ui;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.bazaar.ui.buySell.BazaarInstantBuyMenu;
import fun.ascent.skyblock.bazaar.ui.buySell.BazaarInstantSellMenu;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BazaarItemBuyMenu {

    public static final HashMap<SkyblockPlayer,BazaarEntry> curItem = new HashMap<>();

    public static void open(SkyblockPlayer player, BazaarEntry item){
       curItem.put(player,item);
        Inventory inventory = new Inventory(item.getSize(),item.titleComp);
        fill(inventory);
        addUtility(inventory,player);
        addBuySell(inventory,item,player);
        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            mouseClick(event.getSlot(),event.getClickedItem(),event.getClick(),(SkyblockPlayer) event.getPlayer(),event.getInventory());
        });
        player.openInventory(inventory);
    }

    private static void mouseClick(int slot, ItemStack clickedItem, Click click,SkyblockPlayer player, AbstractInventory inventory) {
        int slotBase = inventory.getInnerSize() - 5;
        BazaarEntry cur = curItem.get(player);
        if(cur == null || cur.parentEntry == null) return;
        if (slot == slotBase) {
            BazaarEntry parent = cur.parentEntry;
            while (parent.parentEntry != null){
                parent = parent.parentEntry;
            }
            BazaarCategoryMenu.openMenu(player,parent);
            return;
        }
        if (slot == slotBase - 1) {
            BazaarChildCategoryMenu.open(player, cur.parentEntry);
            return;
        }
        if(slot == 10){
            BazaarInstantBuyMenu.open(player,cur);
        }
        if(slot == 11){
            int currentAmount = BazaarInstantSellMenu.getInventoryAmount(player,cur);
           if(currentAmount > 0) {
               if (click instanceof Click.Right) {
                   BazaarInstantSellMenu.open(player, cur);
               } else {
                   sellAll(player, cur);
               }
           }
            return;
        }
        if(slot == 15 || slot == 16){
            player.sendMessage(StringUtility.text("<red>This feature is not yet implemented."));
            return;
        }
    }

    private static void sellAll(SkyblockPlayer player, BazaarEntry cur) {
        int currentAmount = BazaarInstantSellMenu.getInventoryAmount(player,cur);
        if (currentAmount > 0) {
            BazaarInstantSellMenu.removeItems(player, cur.itemToSell.getItemId(), currentAmount);
            double coinsEarned = currentAmount * BZPriceRegistry.getSell(cur) * (1.0 - BazaarRegistry.sellTax / 100.0);
            player.addCoins(coinsEarned);
            player.sendMessage(StringUtility.text("<green>Sold " + currentAmount + " items for <gold>" + coinsEarned + " coins!"));
            Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 1f);
            player.playSound(sound);
            BazaarItemBuyMenu.open(player, cur);
        } else {
            player.sendMessage(StringUtility.text("<red>You don't have any items to sell!"));
        }
    }

    public static void addBuySell(Inventory inventory, BazaarEntry item,SkyblockPlayer player) {
        String itemText = "<dark_gray>" + item.itemToSell.getDisplayName();
        int amount = BazaarInstantSellMenu.getInventoryAmount(player,item);

        List<Component> buyLore = List.of(
                StringUtility.text(itemText),
                Component.empty(),
                StringUtility.text("<gray>Price per unit: <gold>" + BZPriceRegistry.getBuy(item) + " coins."),
                StringUtility.text("<gray>Stack Price: <gold>" + (64*BZPriceRegistry.getBuy(item)) + " coins."),
                Component.empty(),
                StringUtility.text("<yellow>Click to pick amount!")
        );
        ItemStack buy = ItemStack.of(Material.GOLDEN_HORSE_ARMOR).withCustomName(
                StringUtility.text("<green>Buy Instantly"))
                .withLore(buyLore);
        inventory.setItemStack(10,buy);

        List<Component> sellLore = new java.util.ArrayList<>(List.of(
                StringUtility.text(itemText),
                Component.empty()
        ));
        sellLore.add(StringUtility.text("<gray>Inventory: " + (amount > 0 ? "<green>" + amount + " items" : "<red> None")));
        if(amount > 0){
            sellLore.add(Component.empty());
            sellLore.add(StringUtility.text("<gray>Amount: <green>" + amount + "<gray>x"));
            double total = amount * BZPriceRegistry.getSell(item) * (1.0 - BazaarRegistry.sellTax / 100.0);
            sellLore.add(StringUtility.text("<gray>Total: <gold>" + total + " coins"));
            sellLore.add(StringUtility.text("<dark_gray>Current tax: " + BazaarRegistry.sellTax + "%"));
            sellLore.add(Component.empty());
            sellLore.add(StringUtility.text("<aqua>Right-Click to pick amount!"));
            sellLore.add(StringUtility.text("<yellow>Click to sell!"));
        }else {
            sellLore.add(Component.empty());
            sellLore.add(StringUtility.text("<gray>Price per unit: <gold>" + BZPriceRegistry.getSell(item) + " coins."));
            sellLore.add(Component.empty());
            sellLore.add(StringUtility.text("<dark_gray>None to sell in your inventory!"));
        }
        ItemStack sell = ItemStack.of(Material.HOPPER).withLore(sellLore).withCustomName(
                StringUtility.text("<gold>Sell Instantly")
        );
        inventory.setItemStack(11,sell);

        inventory.setItemStack(13,item.itemToSell.buildItemStack());

        ItemStack buyOrder = ItemStack.of(Material.FILLED_MAP).withCustomName(StringUtility.text(
                "<green>Create Buy Order"
        )).withLore(StringUtility.text("<dark_gray>Buy Orders are not yet Available."));
        inventory.setItemStack(15,buyOrder);

        ItemStack sellOffer = ItemStack.of(Material.MAP).withCustomName(StringUtility.text(
                "<gold>Create Sell Offer"
        )).withLore(StringUtility.text("<dark_gray>Sell Offers are not yet Available."));
        inventory.setItemStack(16,sellOffer);
    }

    public static void addUtility(Inventory inventory,SkyblockPlayer player){
        //TODO: Put Bazaar NPC Head
        ItemStack close = ItemStack.of(Material.BARRIER)
                .withCustomName(StringUtility.text("<gold>Go Back")).withLore(List.of());
        int slot = inventory.getInnerSize() - 5;
        inventory.setItemStack(slot,close);

        BazaarEntry cur = curItem.get(player);
        if(cur == null) return;
        String text = MiniMessage.miniMessage().serialize(cur.parentEntry.nameComp.color(NamedTextColor.GRAY));
        ItemStack back = ItemStack.of(Material.ARROW).withCustomName(StringUtility.text("<green>Go Back"))
                .withLore(StringUtility.text("<gray>To " + text));
        int slot_back = slot-1;
        inventory.setItemStack(slot_back,back);
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
