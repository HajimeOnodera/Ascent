package fun.ascent.skyblock.bazaar.ui.extras;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.bazaar.ui.BazaarCategoryMenu;
import fun.ascent.skyblock.bazaar.ui.BazaarChildCategoryMenu;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fun.ascent.skyblock.bazaar.ui.BazaarChildCategoryMenu.fill;

public class SellInvConfirmMenu {


    public static HashMap<UUID,BazaarEntry> categories = new HashMap<>();
    public static HashMap<UUID,BazaarEntry> prevCategories = new HashMap<>();

    public static void open(SkyblockPlayer player, BazaarEntry prevCategory,BazaarEntry category){
        categories.put(player.getUuid(),category);
        prevCategories.put(player.getUuid(),prevCategory);
        Inventory inv = new Inventory(InventoryType.CHEST_4_ROW,
                StringUtility.text("<gray>Are you sure?"));

        fill(inv);
        addButtons(inv,category,player);


        player.openInventory(inv);

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
           event.setCancelled(true);
           if(event.getSlot() == 11){
               if(categories.containsKey(event.getPlayer().getUuid())){
                   BazaarCategoryMenu.sellInventory((SkyblockPlayer) event.getPlayer(),categories.get(event.getPlayer().getUuid()),
                           prevCategories.get(event.getPlayer().getUuid()));
               }
               return;
           }
           if(event.getSlot() == 15){
               if(prevCategories.containsKey(event.getPlayer().getUuid())){
                   BazaarEntry prevCat = prevCategories.get(event.getPlayer().getUuid());
                   if(prevCat.parent != null){
                       BazaarChildCategoryMenu.open(player,prevCat);
                   }else{
                       BazaarCategoryMenu.openMenu(player,prevCat);
                   }
               }
           }
        });
    }

    private static void addButtons(Inventory inv, BazaarEntry category,SkyblockPlayer player) {
        ItemStack cancel = ItemStack.of(Material.RED_TERRACOTTA)
                .withCustomName(StringUtility.text("<red>Cancel")).withLore(List.of());
        inv.setItemStack(15, cancel);

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
            totalCoins += amount * pricePerUnit;
            itemsSold += amount;
        }

        ItemStack confirm = ItemStack.of(Material.HOPPER)
                .withCustomName(StringUtility.text("<gold>Selling whole inventory"))
                .withLore(
                        StringUtility.text("<gray>You sell: <green>" + itemsSold + "<gray>x products"),
                        StringUtility.text("<gray>You earn: <gold>" + totalCoins + " coins"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to confirm!")
                );
        inv.setItemStack(11, confirm);
    }

}
