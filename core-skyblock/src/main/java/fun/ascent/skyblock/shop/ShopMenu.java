package fun.ascent.skyblock.shop;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.sound.SoundEvent;

import java.util.*;

public class ShopMenu {

    public static void open(SkyblockPlayer player, String shopId) {
        if (player == null) return;
        if(shopId == null || shopId.isEmpty()){
            System.out.println("[SHOP] Invalid SHOP ID");
            return;
        }
        ShopData data = ShopRegistry.get(shopId);
        if (data == null) return;

        Inventory inv = new Inventory(data.invType, MiniMessage.miniMessage().deserialize(data.shopTitle));
        Map<Integer, ShopPrice> slotMap = new HashMap<>();

        constructBorders(inv);

        int currentSlot = 10;
        for (ShopPrice priceData : data.getItems()) {
            while (currentSlot % 9 == 0 || currentSlot % 9 == 8) {
                currentSlot++;
            }
            if (currentSlot >= inv.getSize() - 9) break;

            inv.setItemStack(currentSlot, buildDisplayItem(priceData));
            slotMap.put(currentSlot, priceData);
            currentSlot++;
        }

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);

            if (event.getInventory() != inv) return;

            ShopPrice clickedItem = slotMap.get(event.getSlot());
            if (clickedItem != null) {
                handlePurchase(player, clickedItem);
            }
        });

        player.openInventory(inv);
    }

    private static void constructBorders(Inventory inv) {
        ItemStack fill = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
                .customName(Component.empty()).lore(Component.empty())
                .set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of(DataComponents.ATTRIBUTE_MODIFIERS)))
                .build();

        int size = inv.getSize();
        for (int i = 0; i < size; i++) {
            if (i < 9 || i >= size - 9 || i % 9 == 0 || i % 9 == 8) {
                inv.setItemStack(i, fill);
            }
        }
    }

    private static ItemStack buildDisplayItem(ShopPrice priceData) {
        if (priceData.item == null) {
            System.err.println("[Shop] WARNING: Tried to display a null SkyblockItem!");
            return ItemStack.of(Material.BARRIER).withCustomName(Component.text("BROKEN ITEM").color(NamedTextColor.RED));
        }

        ItemStack baseItem = priceData.item.buildItemStack();

        List<Component> baseLore = baseItem.get(DataComponents.LORE);
        List<Component> newLore = baseLore != null ? new ArrayList<>(baseLore) : new ArrayList<>();

        newLore.add(Component.empty());
        newLore.add(Component.text("Cost").color(NamedTextColor.GRAY));
        newLore.add(Component.text(priceData.price + " Coins").color(NamedTextColor.GOLD));
        newLore.add(Component.empty());
        newLore.add(Component.text("Click to buy!").color(NamedTextColor.YELLOW));

        return baseItem.withLore(newLore);
    }

    private static void handlePurchase(SkyblockPlayer player, ShopPrice itemData) {
        double playerCoins = player.getCoins();

        if (playerCoins >= itemData.price) {
            player.setCoins(playerCoins - itemData.price);
            if(itemData.item == null){
                player.sendMessage(Component.text("You cannot buy a null item").color(NamedTextColor.RED));
                return;
            }
            player.getInventory().addItemStack(itemData.item.buildItemStack());

            player.sendMessage(Component.text("You bought a " + itemData.item.getDisplayName() + " for " + itemData.price + " coins!").color(NamedTextColor.GREEN));
            player.playSound(Sound.sound(SoundEvent.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, 1f, 1f));
        } else {
            player.sendMessage(Component.text("You don't have enough coins!").color(NamedTextColor.RED));
            player.playSound(Sound.sound(SoundEvent.ENTITY_ENDERMAN_TELEPORT, Sound.Source.MASTER, 1f, 0.5f));
        }
    }
}
