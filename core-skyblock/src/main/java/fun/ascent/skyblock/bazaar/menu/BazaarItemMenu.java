package fun.ascent.skyblock.bazaar.menu;

import fun.ascent.skyblock.bazaar.BazaarItem;
import fun.ascent.skyblock.bazaar.price.BazaarPriceRegistry;
import fun.ascent.skyblock.bazaar.price.Price;
import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public class BazaarItemMenu {

    public static void open(SkyblockPlayer player, BazaarItem item, BazaarCategory activeCategory, fun.ascent.skyblock.bazaar.BazaarItemFamily family) {
        SkyblockItem sbItem = ItemRegistry.getItem(item.getProductId());
        String displayName = sbItem != null ? sbItem.getDisplayName() : item.getProductId();
        
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, MiniMessage.miniMessage().deserialize("<dark_gray>" + displayName));
        
        ItemStack filler = ItemStack.builder(activeCategory.pane).customName(Component.empty()).build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItemStack(i, filler);
        }
        
        Price price = BazaarPriceRegistry.itemPrices.get(item.getProductId());
        double buyPrice = price != null ? price.getBuyPrice() : 0.0;
        double sellPrice = price != null ? price.getSellPrice() : 0.0;
        long buyVol = price != null ? price.getBuyVolume() : 0;
        long sellVol = price != null ? price.getSellVolume() : 0;
        
        Material material = sbItem != null ? sbItem.getMaterial() : Material.STONE;
        ItemStack displayItem = ItemStack.builder(material)
                .customName(MiniMessage.miniMessage().deserialize("<yellow>" + displayName))
                .lore(List.of(
                    MiniMessage.miniMessage().deserialize("<gray>Buy Price: <gold>" + String.format("%.1f", buyPrice)),
                    MiniMessage.miniMessage().deserialize("<gray>Sell Price: <gold>" + String.format("%.1f", sellPrice)),
                    MiniMessage.miniMessage().deserialize("<gray>Buy Volume: <green>" + buyVol),
                    MiniMessage.miniMessage().deserialize("<gray>Sell Volume: <green>" + sellVol)
                ))
                .build();
        inventory.setItemStack(13, displayItem);
        
        ItemStack buyInstantly = ItemStack.builder(Material.GOLD_INGOT)
                .customName(MiniMessage.miniMessage().deserialize("<green>Buy Instantly"))
                .lore(List.of(MiniMessage.miniMessage().deserialize("<gray>Click to buy!")))
                .build();
        inventory.setItemStack(29, buyInstantly);
        
        ItemStack sellInstantly = ItemStack.builder(Material.IRON_INGOT)
                .customName(MiniMessage.miniMessage().deserialize("<red>Sell Instantly"))
                .lore(List.of(MiniMessage.miniMessage().deserialize("<gray>Click to sell!")))
                .build();
        inventory.setItemStack(33, sellInstantly);
        
        ItemStack back = ItemStack.builder(Material.ARROW)
                .customName(MiniMessage.miniMessage().deserialize("<green>Go Back"))
                .lore(List.of(MiniMessage.miniMessage().deserialize("<gray>To " + (family != null ? family.name : activeCategory.name))))
                .build();
        inventory.setItemStack(49, back);

        inventory.eventNode().addListener(net.minestom.server.event.inventory.InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            net.minestom.server.entity.Player p = event.getPlayer();
            
            if (slot == 49) {
                if (family != null) {
                    BazaarFamilyMenu.open((SkyblockPlayer) p, activeCategory, family);
                } else {
                    BazaarCategoryMenu.open((SkyblockPlayer) p, activeCategory, 1);
                }
            } else if (slot == 29 || slot == 33) {
                p.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Transactions coming soon!"));
            }
        });

        player.openInventory(inventory);
    }
}
