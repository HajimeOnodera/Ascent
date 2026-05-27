package fun.ascent.skyblock.bazaar.ui.buySell;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.bazaar.ui.BazaarItemBuyMenu;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.key.Key;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import java.util.HashMap;
import java.util.UUID;
import static fun.ascent.skyblock.bazaar.ui.BazaarChildCategoryMenu.fill;

public class BazaarInstantSellMenu {

    public static final HashMap<UUID,BazaarEntry> items = new HashMap<>();
    public static final HashMap<UUID,Integer> amounts = new HashMap<>();

    public static void open(SkyblockPlayer player, BazaarEntry item){
        items.put(player.getUuid(), item);
        String titleStart = item.title.split("➜")[1];

        int amount = 0;
        for(ItemStack stack : player.getInventory().getItemStacks()){
            SkyblockItem itemS = SkyblockItem.fromStack(stack);
            if(itemS == null) continue;
            if(itemS.getItemId().equals(item.itemToSell.getItemId())){
                amount += stack.amount();
            }
        }
        if(amount <= 0) return;
        amounts.put(player.getUuid(),amount);

        Inventory inventory = new Inventory(InventoryType.CHEST_4_ROW,
                StringUtility.text("<dark_gray>" + titleStart + " ➜ Instant Sell"));
        fill(inventory);
        addButtons(inventory,player,item);
        inventory.eventNode().addListener(InventoryPreClickEvent.class,event -> {
            event.setCancelled(true);
            SkyblockPlayer player1 = (SkyblockPlayer)event.getPlayer();
            if(event.getSlot() == 11){
                int stackSize = item.itemToSell.buildItemStack().maxStackSize();
                int currentAmount = getInventoryAmount(player1, item);
                if (currentAmount >= stackSize) {
                    removeItems(player1, item.itemToSell.getItemId(), stackSize);
                    double coinsEarned = stackSize * BZPriceRegistry.getSell(item) * (1.0 - BazaarRegistry.sellTax / 100.0);
                    player1.addCoins(coinsEarned);
                    player1.sendMessage(StringUtility.text("<green>Sold " + stackSize + " items for <gold>" + coinsEarned + " coins!"));
                    Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 1f);
                    player1.playSound(sound);
                    BazaarItemBuyMenu.open(player1, item);
                } else {
                    player1.sendMessage(StringUtility.text("<red>You don't have enough products!"));
                }
            }
            if(event.getSlot() == 13){
                int currentAmount = getInventoryAmount(player1, item);
                if (currentAmount > 0) {
                    int amountHalf = (int) Math.round(currentAmount / 2.0);
                    removeItems(player1, item.itemToSell.getItemId(), amountHalf);
                    double coinsEarned = amountHalf * BZPriceRegistry.getSell(item) * (1.0 - BazaarRegistry.sellTax / 100.0);
                    player1.addCoins(coinsEarned);
                    player1.sendMessage(StringUtility.text("<green>Sold " + amountHalf + " items for <gold>" + coinsEarned + " coins!"));
                    Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 1f);
                    player1.playSound(sound);
                    BazaarItemBuyMenu.open(player1, item);
                } else {
                    player1.sendMessage(StringUtility.text("<red>You don't have any items to sell!"));
                }
            }
            if(event.getSlot() == 15){
                int currentAmount = getInventoryAmount(player1, item);
                if (currentAmount > 0) {
                    removeItems(player1, item.itemToSell.getItemId(), currentAmount);
                    double coinsEarned = currentAmount * BZPriceRegistry.getSell(item) * (1.0 - BazaarRegistry.sellTax / 100.0);
                    player1.addCoins(coinsEarned);
                    player1.sendMessage(StringUtility.text("<green>Sold " + currentAmount + " items for <gold>" + coinsEarned + " coins!"));
                    Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 1f);
                    player1.playSound(sound);
                    BazaarItemBuyMenu.open(player1, item);
                } else {
                    player1.sendMessage(StringUtility.text("<red>You don't have any items to sell!"));
                }
            }
            if(event.getSlot() == 31){
                BazaarEntry bzItem = items.getOrDefault(player1.getUuid(),null);
                if(bzItem == null) return;
                BazaarItemBuyMenu.open(player1,bzItem);
                return;
            }
        });
        player.openInventory(inventory);
    }

    public static int getInventoryAmount(SkyblockPlayer player, BazaarEntry item) {
        int amount = 0;
        for (ItemStack stack : player.getInventory().getItemStacks()) {
            SkyblockItem itemS = SkyblockItem.fromStack(stack);
            if (itemS == null) continue;
            if (itemS.getItemId().equals(item.itemToSell.getItemId())) {
                amount += stack.amount();
            }
        }
        return amount;
    }

    public static void removeItems(SkyblockPlayer player, String itemId, int toRemove) {
        net.minestom.server.inventory.PlayerInventory inventory = player.getInventory();
        int remaining = toRemove;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItemStack(i);
            if (stack.isAir()) continue;
            SkyblockItem sbItem = SkyblockItem.fromStack(stack);
            if (sbItem != null && sbItem.getItemId().equals(itemId)) {
                int amt = stack.amount();
                if (amt <= remaining) {
                    inventory.setItemStack(i, ItemStack.AIR);
                    remaining -= amt;
                } else {
                    inventory.setItemStack(i, stack.withAmount(amt - remaining));
                    remaining = 0;
                }
                if (remaining <= 0) break;
            }
        }
    }

    private static void addButtons(Inventory inventory, SkyblockPlayer player, BazaarEntry item) {
        int amount = amounts.get(player.getUuid());
        if(amount <= 0) return;
        double singlePrice = BZPriceRegistry.getSell(item);
        int stackSize = item.itemToSell.buildItemStack().maxStackSize();
        
        double stackTotal = stackSize * singlePrice * (1.0 - BazaarRegistry.sellTax / 100.0);
        ItemStack stackSell = ItemStack.of(item.itemToSell.getMaterial())
                .withCustomName(StringUtility.text("<gold>Sell a stack!"))
                .withAmount(stackSize)
                .withLore(
                        StringUtility.text("<dark_gray>" + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Inventory: <green>" + amount + " items"),
                        Component.empty(),
                        StringUtility.text("<gray>Amount:<green> " + stackSize + " <gray>x"),
                        StringUtility.text("<gray>Total: <gold>" + stackTotal + " coins"),
                        StringUtility.text("<dark_gray>Current tax: " + BazaarRegistry.sellTax + "%"),
                        Component.empty(),
                        StringUtility.text((amount >= stackSize ? "<yellow>Click to sell!" : "<red>You don't have enough products!"))
                );
        inventory.setItemStack(11,stackSell);

        int amountHalf = (int)Math.round(amount / 2.0);
        double halfTotal = amountHalf * singlePrice * (1.0 - BazaarRegistry.sellTax / 100.0);
        ItemStack halfInv = ItemStack.of(Material.CHEST)
                .withCustomName(StringUtility.text("<gold>Sell half your inventory!"))
                .withLore(
                        StringUtility.text("<dark_gray>" + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Inventory: <green>" + amount + " items"),
                        Component.empty(),
                        StringUtility.text("<gray>Amount:<green> " + amountHalf + " <gray>x"),
                        StringUtility.text("<gray>Total: <gold>" + halfTotal + " coins"),
                        StringUtility.text("<dark_gray>Current tax: " + BazaarRegistry.sellTax + "%"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to sell!")
                );
        inventory.setItemStack(13,halfInv);

        double fullTotal = amount * singlePrice * (1.0 - BazaarRegistry.sellTax / 100.0);
        ItemStack fullInv = ItemStack.of(Material.CHEST)
                .withCustomName(StringUtility.text("<gold>Sell whole inventory!"))
                .withLore(
                        StringUtility.text("<dark_gray>" + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Inventory: <green>" + amount + " items"),
                        Component.empty(),
                        StringUtility.text("<gray>Amount:<green> " + amount + " <gray>x"),
                        StringUtility.text("<gray>Total: <gold>" + fullTotal + " coins"),
                        StringUtility.text("<dark_gray>Current tax: " + BazaarRegistry.sellTax + "%"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to sell!")
                );
        inventory.setItemStack(15,fullInv);

        ItemStack back = ItemStack.of(Material.ARROW)
                .withCustomName(StringUtility.text("<green>Go Back"))
                .withLore(
                        StringUtility.text("<gray>To " + item.itemToSell.getDisplayName())
                );
        inventory.setItemStack(31,back);
    }

}
