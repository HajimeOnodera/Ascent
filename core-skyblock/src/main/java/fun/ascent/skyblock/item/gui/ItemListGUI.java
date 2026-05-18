package fun.ascent.skyblock.item.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.SkyblockItem;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemListGUI extends InventoryGUI {

    private final List<SkyblockItem> filteredItems;
    private int page = 0;
    private Player player;

    public ItemListGUI(String categoryName) {
        super("Items: " + categoryName, InventoryType.CHEST_6_ROW);
        this.filteredItems = filterByCategory(categoryName, ItemRegistry.getAllItems());
    }

    public static List<SkyblockItem> filterByCategory(String categoryName, Collection<SkyblockItem> allItems) {
        return allItems.stream()
                .filter(item -> {
                    String id = item.getItemId().toUpperCase();
                    String name = item.getDisplayName().toLowerCase();
                    String desc = "";
                    if (item.convertToItemData() != null && item.convertToItemData().description() != null) {
                        desc = item.convertToItemData().description().toLowerCase();
                    }
                    ItemType type = item.getItemType();

                    return switch (categoryName) {
                        case "Accessory" ->
                                type.isAccessory() || name.contains("talisman") || name.contains("ring") || name.contains("artifact") || name.contains("relic") || name.contains("badge");
                        case "Admin Items" ->
                                item.getRarity() == Rarity.ADMIN || id.startsWith("ADMIN_") || name.contains("admin");
                        case "Armor" -> type.isArmor();
                        case "Booster" ->
                                id.contains("BOOSTER") || name.contains("booster cookie") || desc.contains("booster");
                        case "Brewing Ingredient" ->
                                id.contains("BREWING") || desc.contains("brewing ingredient") || name.contains("glowstone dust") || name.contains("redstone") || name.contains("nether wart");
                        case "Consumable" ->
                                type == ItemType.BAIT || type == ItemType.ARROW || type == ItemType.ARROW_POISON || name.contains("potion") || desc.contains("consume") || name.contains("candy") || name.contains("cookie") || type == ItemType.PET_ITEM;
                        case "Cosmetic" ->
                                type == ItemType.COSMETIC || name.contains("skin") || name.contains("cosmetic");
                        case "Crafting Material" ->
                                name.contains("material") || desc.contains("crafting material") || id.contains("FRAGMENT") || id.contains("CRYSTAL") || id.contains("ESSENCE") || id.contains("SPORE") || id.contains("CATALYST") || id.contains("INGOT") || id.contains("LEATHER") || id.contains("SHARD");
                        case "Drill Part" ->
                                id.contains("DRILL_PART") || (name.contains("drill") && (name.contains("engine") || name.contains("fuel tank") || name.contains("part")));
                        case "Equipment" -> type.isEquipment();
                        case "Fishing Net" -> id.contains("FISHING_NET") || name.contains("fishing net");
                        case "Forgeable Item" ->
                                desc.contains("forge") || desc.contains("forgeable") || id.contains("FORGE");
                        case "Hunting Tool" ->
                                id.contains("HUNTING_TOOL") || name.contains("hunting tool") || name.contains("lasso");
                        case "Item Sack" -> id.contains("SACK") || name.contains("sack");
                        case "Lasso" -> id.contains("LASSO") || name.contains("lasso");
                        case "Memento" -> id.contains("MEMENTO") || name.contains("memento");
                        case "Minion" -> id.contains("MINION") || name.contains("minion");
                        case "Pet" -> id.contains("PET") || name.contains("pet") || type == ItemType.PET_ITEM;
                        case "Potion" ->
                                item.getMaterial() == Material.POTION || item.getMaterial() == Material.SPLASH_POTION || item.getMaterial() == Material.LINGERING_POTION || id.contains("POTION") || name.contains("potion");
                        case "Power Stone" ->
                                id.contains("POWER_STONE") || name.contains("power stone") || name.equals("beating heart") || name.equals("dark orb") || name.equals("endstone sherd");
                        case "Reforge Stone" ->
                                type == ItemType.REFORGE_STONE || id.contains("REFORGE_STONE") || name.contains("reforge stone");
                        case "Rift Item" -> id.contains("RIFT_") || desc.contains("rift");
                        case "Rift Timecharm" -> id.contains("TIMECHARM") || name.contains("timecharm");
                        case "Salt" -> id.contains("SALT") || name.contains("salt");
                        case "Shears" ->
                                type == ItemType.SHEARS || item.getMaterial() == Material.SHEARS || name.contains("shears");
                        case "Tool" ->
                                type.isTool() || name.contains("pickaxe") || name.contains("drill") || name.contains("shovel") || name.contains("hoe") || name.contains("shears");
                        case "Trap" -> id.contains("TRAP") || name.contains("trap");
                        case "Vanilla Item" ->
                                id.contains("VANILLA") || (!id.contains("_") && !id.equals("NONE") && item.getMaterial() != Material.PAPER && item.getMaterial() != Material.PLAYER_HEAD);
                        case "Wand" -> type == ItemType.WAND || name.contains("wand");
                        case "Weapon" -> type.isWeapon();
                        default -> true;
                    };
                })
                .sorted((a, b) -> a.getDisplayName().compareToIgnoreCase(b.getDisplayName()))
                .toList();
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        this.player = e.player();
        render();
    }

    private void render() {
        getInventory().clear();
        if (player == null) return;

        // Fill bottom row with dark gray stained glass pane except page buttons
        for (int i = 45; i < 54; i++) {
            if (i != 48 && i != 49 && i != 50) {
                set(i, FILLER_ITEM);
            }
        }

        int itemsPerPage = 45;
        int maxPage = (int) Math.ceil(filteredItems.size() / (double) itemsPerPage) - 1;
        if (page > maxPage) page = maxPage;
        if (page < 0) page = 0;

        int start = page * itemsPerPage;
        int end = Math.min(start + itemsPerPage, filteredItems.size());

        int slot = 0;
        for (int i = start; i < end; i++) {
            SkyblockItem item = filteredItems.get(i);
            set(new GUIClickableItem(slot++) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    player.getInventory().addItemStack(item.buildItemStack(player));
                    player.sendMessage(StringUtility.text("<green>Gave you 1x " + item.getDisplayName()));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    ItemStack stack = item.buildItemStack(p);
                    List<Component> lore = new ArrayList<>();
                    if (stack.get(DataComponents.LORE) != null) {
                        lore.addAll(stack.get(DataComponents.LORE));
                    }
                    lore.add(Component.empty());
                    lore.add(StringUtility.text("<yellow>Click to receive this item!"));

                    ItemStack.Builder builder = ItemStack.builder(stack.material())
                            .set(DataComponents.LORE, lore);

                    if (stack.get(DataComponents.CUSTOM_NAME) != null) {
                        builder.set(DataComponents.CUSTOM_NAME, stack.get(DataComponents.CUSTOM_NAME));
                    }
                    if (stack.get(DataComponents.CUSTOM_DATA) != null) {
                        builder.set(DataComponents.CUSTOM_DATA, stack.get(DataComponents.CUSTOM_DATA));
                    }
                    if (stack.get(DataComponents.PROFILE) != null) {
                        builder.set(DataComponents.PROFILE, stack.get(DataComponents.PROFILE));
                    }
                    if (stack.get(DataComponents.DYED_COLOR) != null) {
                        builder.set(DataComponents.DYED_COLOR, stack.get(DataComponents.DYED_COLOR));
                    }
                    if (stack.get(DataComponents.ATTRIBUTE_MODIFIERS) != null) {
                        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, stack.get(DataComponents.ATTRIBUTE_MODIFIERS));
                    }
                    if (stack.get(DataComponents.TOOLTIP_DISPLAY) != null) {
                        builder.set(DataComponents.TOOLTIP_DISPLAY, stack.get(DataComponents.TOOLTIP_DISPLAY));
                    }
                    if (stack.get(DataComponents.UNBREAKABLE) != null) {
                        builder.set(DataComponents.UNBREAKABLE, stack.get(DataComponents.UNBREAKABLE));
                    }

                    return builder;
                }
            });
        }

        // Fill any remaining slots up to 45 with null/air
        for (int i = slot; i < itemsPerPage; i++) {
            set(i, null);
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
        } else {
            set(48, FILLER_ITEM);
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
        } else {
            set(50, FILLER_ITEM);
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
