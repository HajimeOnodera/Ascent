package fun.ascent.skyblock.bazaar.ui;

import fun.ascent.skyblock.bazaar.BazaarData;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BazaarCategoryMenu {

    public static final HashMap<SkyblockPlayer,BazaarEntry> curCategory = new HashMap<>();
    private static final List<Integer> utilitySlots = Arrays.asList(47,49,50,51,52);
    public static void openMenu(SkyblockPlayer player, BazaarEntry category){
        if(category.parent != null){
            System.err.println("[ERROR] User Tried to open category menu for a non category");
            return;
        }
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW,
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
        addUtility(inventory);
        curCategory.put(player,category);
        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> event.setCancelled(true));
        inventory.eventNode().addListener(InventoryClickEvent.class, click -> mouseClick(click.getSlot(), click.getClickedItem(), click.getClickType(),(SkyblockPlayer) click.getPlayer(), click.getInventory()));
        player.openInventory(inventory);
    }

    private static void mouseClick(int slot, ItemStack clickedItem, ClickType clickType, SkyblockPlayer player, AbstractInventory inventory) {
        if(slot % 9 == 0){
            if(slot == 45){
                //TODO: Open Search Bar
                player.sendMessage("Opening Search Bar after its done");
                return;
            }
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
                if(category.slot == slot){
                    player.sendMessage(
                            MiniMessage.miniMessage().deserialize("<green>Opened GUI of Item " + child.itemName)
                    );
                    return;
                }
            }
        }
        if(utilitySlots.contains(slot)){
            //TODO: Utility Stuff
        }

    }


    public static void addUtility(Inventory inventory){
        // Search Bar
        ItemStack search = ItemStack.of(Material.OAK_SIGN)
                .withCustomName(MiniMessage.miniMessage().deserialize("<green>Search"))
                .withLore(
                        MiniMessage.miniMessage().deserialize("<gray>Find products by name!"),
                        Component.empty(),
                        MiniMessage.miniMessage().deserialize("<yellow>Click to search!")
                );
        inventory.setItemStack(45, search);


        //TODO: Add Other Items
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
    }
    public static void addCategories(Inventory inventory,BazaarEntry curCategory){
        BazaarData data = BazaarRegistry.bazaarItemList;
        for(int i = 0; i < data.bazaarData.size();i++){
            int slot = 9 * i;
            BazaarEntry entry = data.bazaarData.get(i);
            inventory.setItemStack(slot,entry.getStack(curCategory.id.equals(entry.id)));
        }
    }
}
