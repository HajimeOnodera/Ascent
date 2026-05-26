package fun.ascent.skyblock.bazaar.ui.buySell;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fun.ascent.skyblock.bazaar.ui.BazaarChildCategoryMenu.fill;


public class BazaarInstantBuyMenu {

    public static final HashMap<UUID,Integer> amount = new HashMap<>();

    public static void open(SkyblockPlayer player, BazaarEntry item){
        String titleStart = item.title.split("➔")[1];

        Inventory inventory = new Inventory(InventoryType.CHEST_4_ROW,
                StringUtility.text("<dark_gray>" + titleStart + " ➔ Instant Buy"));

        fill(inventory);
        addButtons(inventory,item,player);
        player.openInventory(inventory);

        inventory.eventNode().addListener(InventoryPreClickEvent.class,event -> {
            event.setCancelled(true);
            SkyblockPlayer player1 =(SkyblockPlayer) event.getPlayer();

            if(event.getSlot() == 16){
                //TODO: Open Sign Amount Thingy
                return;
            }
            if(event.getSlot() % 2 != 0 && event.getSlot() < 10 && event.getSlot() > 14) return;

            int amount = -1;
            if(event.getSlot() == 10){
                amount = 1;
            }
            if(event.getSlot() == 12){
                amount = item.itemToSell.buildItemStack().maxStackSize();
            }
            if(event.getSlot() == 14){
                int space = 0;
                for(ItemStack itemStack : player.getInventory().getItemStacks()){
                    if(itemStack.isAir()){
                        space += item.itemToSell.buildItemStack().maxStackSize();
                    }
                }
                amount = space;
            }
            if(amount > 0) {
                if (canBuy(player1, item,amount)) {
                    player1.getActiveProfileData().playerCoins -= getCost(item, amount);
                    ItemStack stack = item.itemToSell.buildItemStack();
                    if(amount > stack.maxStackSize()){
                        int am = amount;
                        while(am > 0){
                            ItemStack itemStack = item.itemToSell.buildItemStack().withAmount(
                                    Math.min(stack.maxStackSize(),am)
                            );
                            am -= Math.min(stack.maxStackSize(),am);
                            player.getInventory().addItemStack(itemStack);
                        }
                    }
                    player1.getInventory().addItemStack(stack.withAmount(amount));
                } else {
                    player1.sendMessage(
                            StringUtility.text("<red>You cannot afford this item.")
                    );
                }
            }
        });
    }

    private static double getCost(BazaarEntry item, int amount) {
        return BZPriceRegistry.getBuy(item) * amount;
    }

    public static boolean canBuy(SkyblockPlayer player,BazaarEntry item,int amount){
        double price = BZPriceRegistry.getBuy(item) * amount;
        return player.getActiveProfileData().playerCoins > price;
    }

    public static boolean canBuy(SkyblockPlayer player,BazaarEntry item){
        return canBuy(player,item,1);
    }

    public static void addButtons(Inventory inventory, BazaarEntry item,SkyblockPlayer player){
        double basePrice = BZPriceRegistry.getBuy(item);
        ItemStack single = item.itemToSell.buildItemStack().
                withCustomName(StringUtility.text("<green>Buy only<yellow> one<green>!"))
                .withLore(
                        StringUtility.text("<dark_gray> " + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Amount: <green>1<gray>x"),
                        Component.empty(),
                        StringUtility.text("<gray>Price: <gold> " + basePrice + " coins"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to buy now!")
                );
        inventory.setItemStack(10,single);

        ItemStack stack = item.itemToSell.buildItemStack().
                withCustomName(StringUtility.text("<green>Buy a stack!"))
                .withLore(
                        StringUtility.text("<dark_gray> " + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Amount: <green>"+ item.itemToSell.buildItemStack().maxStackSize() + "<gray>x"),
                        Component.empty(),
                        StringUtility.text("<gray>Per unit:<gold> " + basePrice + " coins"),
                        StringUtility.text("<gray>Price: <gold> " + (64*basePrice) + " coins"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to buy now!")
                );
        inventory.setItemStack(12,stack);

        int space = 0;
        for(ItemStack itemStack : player.getInventory().getItemStacks()){
            if(itemStack.isAir()){
                space += item.itemToSell.buildItemStack().maxStackSize();
            }
        }
        ItemStack inv = ItemStack.of(Material.CHEST)
                .withCustomName(StringUtility.text("<green>Fill my inventory!"))
                .withLore(
                        StringUtility.text("<dark_gray> " + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Amount: <green>" + space + "<gray>x"),
                        Component.empty(),
                        StringUtility.text("<gray>Per unit:<gold> " + basePrice + " coins"),
                        StringUtility.text("<gray>Price: <gold> " + (space*basePrice) + " coins"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to buy now!")
                );
        inventory.setItemStack(14,inv);
        List<Component> lore = new ArrayList<>(List.of(
                StringUtility.text("<dark_gray>Buy Order Quantity"),
                Component.empty(),
                StringUtility.text("<gray>Buy up to <green>71,680<gray>x."),
                Component.empty(),
                StringUtility.text("<yellow>Click to specify!")
        ));
        if(amount.containsKey(player.getUuid())){
            //TODO: Add Custom Lore
        }
        ItemStack custom = ItemStack.of(Material.OAK_SIGN)
                .withCustomName(StringUtility.text("<green>Custom Amount"))
                .withLore(
                        lore
                );
        inventory.setItemStack(16,custom);
    }

}
