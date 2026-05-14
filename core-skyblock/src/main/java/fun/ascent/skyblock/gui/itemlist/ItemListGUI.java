package fun.ascent.skyblock.gui.itemlist;

import fun.ascent.common.StringUtility;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.SkyblockItem;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class ItemListGUI extends InventoryGUI {

    private final String categoryName;
    private int page = 0;
    private Player player;

    public ItemListGUI(String categoryName, ItemType type) {
        super("Items: " + categoryName, InventoryType.CHEST_6_ROW);
        this.categoryName = categoryName;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        this.player = e.player();
        render();
    }

    private void render() {
        getInventory().clear();
        if (player == null) return;

        List<SkyblockItem> items = ItemRegistry.getAllItems().stream()
                .filter(item -> switch (categoryName) {
                    case "Weapons" -> item.getItemType().isWeapon();
                    case "Armor" -> item.getItemType().isArmor();
                    case "Tools" -> item.getItemType().isTool();
                    case "Accessories" -> item.getItemType().isAccessory();
                    case "Equipment" -> item.getItemType().isEquipment();
                    case "Consumables" ->
                            item.getItemType() == ItemType.BAIT || item.getItemType() == ItemType.ARROW || item.getItemType() == ItemType.ARROW_POISON;
                    case "Others" ->
                            !item.getItemType().isWeapon() && !item.getItemType().isArmor() && !item.getItemType().isTool() && !item.getItemType().isAccessory() && !item.getItemType().isEquipment();
                    default -> true;
                })
                .sorted((a, b) -> a.getDisplayName().compareToIgnoreCase(b.getDisplayName()))
                .toList();

        int itemsPerPage = 45;
        int maxPage = (int) Math.ceil(items.size() / (double) itemsPerPage) - 1;
        if (page > maxPage) page = maxPage;
        if (page < 0) page = 0;

        int start = page * itemsPerPage;
        int end = Math.min(start + itemsPerPage, items.size());

        int slot = 0;
        for (int i = start; i < end; i++) {
            SkyblockItem item = items.get(i);
            set(new GUIClickableItem(slot++) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    player.getInventory().addItemStack(item.buildItemStack(player));
                    player.sendMessage(StringUtility.text("<green>Gave you 1x " + item.getDisplayName()));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    ItemStack stack = item.buildItemStack(p);
                    // Add "Click to get" lore
                    List<Component> lore = new ArrayList<>(stack.get(DataComponents.LORE));
                    lore.add(Component.empty());
                    lore.add(StringUtility.text("<yellow>Click to receive this item!"));
                    return ItemStack.builder(stack.material())
                            .set(DataComponents.LORE, lore)
                            .set(DataComponents.CUSTOM_DATA, stack.get(DataComponents.CUSTOM_DATA))
                            .set(DataComponents.ITEM_NAME, stack.get(DataComponents.ITEM_NAME));
                }
            });
        }

        // Navigation
        if (page > 0) {
            set(new GUIClickableItem(48) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    page--;
                    render();
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Previous Page", Material.ARROW, 1, "<gray>Page " + page);
                }
            });
        }

        set(new GUIClickableItem(49) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new ItemCategoryGUI().open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack("<red>Go Back", Material.BARRIER, 1, "<gray>To Categories");
            }
        });

        if (page < maxPage) {
            set(new GUIClickableItem(50) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    page++;
                    render();
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Next Page", Material.ARROW, 1, "<gray>Page " + (page + 2));
                }
            });
        }

        updateItemStacks(getInventory(), player);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
