package fun.ascent.skyblock.item.gui;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.item.ItemRegistry;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ItemCategoryGUI extends InventoryGUI {

    private static class Category {
        final String name;
        final Material icon;
        final String description;

        Category(String name, Material icon, String description) {
            this.name = name;
            this.icon = icon;
            this.description = description;
        }
    }

    private static final Category[] CATEGORIES = {
            new Category("Accessory", Material.GOLD_NUGGET, "View all accessories and talismans"),
            new Category("Admin Items", Material.COMMAND_BLOCK, "View all administrator items"),
            new Category("Armor", Material.DIAMOND_CHESTPLATE, "View all armor pieces"),
            new Category("Booster", Material.COOKIE, "View all booster items"),
            new Category("Brewing Ingredient", Material.BREWING_STAND, "View all brewing ingredients"),
            new Category("Consumable", Material.APPLE, "View all consumable items"),
            new Category("Cosmetic", Material.PAINTING, "View all cosmetic items and skins"),
            new Category("Crafting Material", Material.NETHER_STAR, "View all crafting materials"),
            new Category("Drill Part", Material.PRISMARINE_SHARD, "View all drill parts and upgrades"),
            new Category("Equipment", Material.LEATHER, "View all equipment pieces"),
            new Category("Fishing Net", Material.COBWEB, "View all fishing nets"),
            new Category("Forgeable Item", Material.FURNACE, "View all forgeable items"),
            new Category("Hunting Tool", Material.IRON_SWORD, "View all hunting tools"),
            new Category("Item Sack", Material.BUNDLE, "View all item sacks"),
            new Category("Lasso", Material.LEAD, "View all lasso items"),
            new Category("Memento", Material.BOOK, "View all mementos"),
            new Category("Minion", Material.ARMOR_STAND, "View all minions"),
            new Category("Pet", Material.BONE, "View all pets"),
            new Category("Potion", Material.POTION, "View all potions"),
            new Category("Power Stone", Material.REDSTONE, "View all power stones"),
            new Category("Reforge Stone", Material.ANVIL, "View all reforge stones"),
            new Category("Rift Item", Material.ENDER_EYE, "View all Rift items"),
            new Category("Rift Timecharm", Material.CLOCK, "View all Rift timecharms"),
            new Category("Salt", Material.SUGAR, "View all salt items"),
            new Category("Shears", Material.SHEARS, "View all shears"),
            new Category("Tool", Material.DIAMOND_PICKAXE, "View all tools"),
            new Category("Trap", Material.TRIPWIRE_HOOK, "View all traps"),
            new Category("Vanilla Item", Material.GRASS_BLOCK, "View all vanilla items"),
            new Category("Wand", Material.STICK, "View all wands"),
            new Category("Weapon", Material.DIAMOND_SWORD, "View all weapons")
    };

    private static final int[] SLOTS = {
            10, 11, 12, 13, 14, 15, 16, 17,
            19, 20, 21, 22, 23, 24, 25, 26,
            28, 29, 30, 31, 32, 33, 34, 35,
            37, 38, 39, 40, 41, 42
    };

    public ItemCategoryGUI() {
        super("Item Browser - Categories", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);

        for (int i = 0; i < CATEGORIES.length; i++) {
            Category cat = CATEGORIES[i];
            int slot = SLOTS[i];
            int count = ItemListGUI.filterByCategory(cat.name, ItemRegistry.getAllItems()).size();

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent event, Player player) {
                    new ItemListGUI(cat.name).open(player);
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack(
                            "<green>" + cat.name,
                            cat.icon,
                            1,
                            "<gray>" + cat.description,
                            "",
                            "<gray>Loaded Items: <yellow>" + count + " items",
                            "<yellow>Click to browse category!"
                    );
                }
            });
        }

        set(new GUIClickableItem(49) {
            @Override
            public void run(InventoryPreClickEvent event, Player player) {
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack("<red>Close", Material.BARRIER, 1, "<gray>Close the item browser");
            }
        });

        updateItemStacks(getInventory(), e.player());
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
