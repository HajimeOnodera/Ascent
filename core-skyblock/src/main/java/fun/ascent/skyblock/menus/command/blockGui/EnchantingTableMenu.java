package fun.ascent.skyblock.menus.command.blockGui;

import fun.ascent.common.StringUtility;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.enchantment.Enchantment;
import fun.ascent.skyblock.enchantment.EnchantmentNBT;
import fun.ascent.skyblock.enchantment.EnchantmentRegistry;
import fun.ascent.skyblock.enchantment.EnchantingItemResolver;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public final class EnchantingTableMenu {

    private static final int ITEM_SLOT = 19;
    private static final int STATUS_SLOT = 23;
    private static final int LABEL_SLOT = 28;
    private static final int BACK_SLOT = 45;
    private static final int BOOKSHELF_SLOT = 48;
    private static final int CLOSE_SLOT = 49;
    private static final int GUIDE_SLOT = 50;

    private static final int[] ENCHANT_LIST_SLOTS = {12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34};
    private static final int[] LEVEL_SELECT_SLOTS = {21, 22, 23, 24, 25, 30, 31, 32, 33, 34};

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private static final ItemStack FILLER = ItemStackCreator
            .createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build();

    private static final ItemStack LABEL_ITEM = ItemStackCreator.getStack(
            "<gold>Enchant Item", Material.ENCHANTING_TABLE, 1,
            List.of(t("<gray>Place the item you want to"), t("<gray>enchant in the slot to the right."))
    ).build();

    private static final ItemStack GUIDE_ITEM = ItemStackCreator.getStack(
            "<green>Enchanting Guide", Material.BOOK, 1,
            List.of(t("<gray>View all available enchantments"), t("<gray>and their effects."))
    ).build();

    private static final ItemStack PLACE_ITEM = ItemStackCreator.getStack(
            "<gray>Place an item!", Material.GRAY_DYE, 1,
            List.of(t("<gray>Put the item you want to"), t("<gray>enchant into the slot to the left."))
    ).build();

    private static final ItemStack INVALID_ITEM = ItemStackCreator.getStack(
            "<red>Invalid Item!", Material.RED_DYE, 1,
            List.of(t("<gray>This item cannot be enchanted."))
    ).build();

    private static final ItemStack BACK_ARROW = ItemStackCreator.getStack(
            "<green>Go Back", Material.ARROW, 1,
            List.of(t("<gray>Return to enchantment list."))
    ).build();

    private static final ItemStack CLOSE_BUTTON =
            ItemStackCreator.createNamedItemStack(Material.BARRIER, "<red>Close").build();

    private final SkyblockPlayer player;
    private final int bookshelfPower;
    private Inventory inventory;
    private EnchantmentRegistry selectedEnchantment;

    private EnchantingTableMenu(SkyblockPlayer player, int bookshelfPower) {
        this.player = player;
        this.bookshelfPower = bookshelfPower;
    }

    private static Component t(String s) {
        return MM.deserialize(s).decoration(TextDecoration.ITALIC, false);
    }

    public static void open(SkyblockPlayer player, Instance instance, Point tablePos) {
        int power = computeBookshelfPower(instance, tablePos);
        new EnchantingTableMenu(player, power).init();
    }

    public static int computeBookshelfPower(Instance instance, Point pos) {
        int power = 0;
        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (instance.getBlock(pos.blockX() + x, pos.blockY() + y, pos.blockZ() + z) == Block.BOOKSHELF) {
                        power++;
                    }
                }
            }
        }
        return Math.min(power, 60);
    }

    private void init() {
        buildInventory();
        player.openInventory(inventory);
    }

    private void buildInventory() {
        inventory = new Inventory(InventoryType.CHEST_6_ROW, titleComponent());
        fill();
        placeStaticItems();
        inventory.setItemStack(ITEM_SLOT, ItemStack.AIR);
        refresh();
        inventory.eventNode().addListener(InventoryPreClickEvent.class, this::onPreClick);
        inventory.eventNode().addListener(InventoryClickEvent.class, this::onClick);
        inventory.eventNode().addListener(InventoryCloseEvent.class, this::onClose);
    }

    private Component titleComponent() {
        return selectedEnchantment == null
                ? t("<dark_gray>Enchanting Table")
                : t("<dark_gray>Enchanting Table - " + selectedEnchantment.getDisplayName());
    }

    private void fill() {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItemStack(i, FILLER);
        }
    }

    private void placeStaticItems() {
        inventory.setItemStack(LABEL_SLOT, LABEL_ITEM);
        inventory.setItemStack(CLOSE_SLOT, CLOSE_BUTTON);
        inventory.setItemStack(GUIDE_SLOT, GUIDE_ITEM);
        inventory.setItemStack(BOOKSHELF_SLOT, bookshelfItem());
    }

    private ItemStack bookshelfItem() {
        return ItemStackCreator.getStack(
                "<green>Bookshelf Power", Material.BOOKSHELF, 1,
                List.of(
                        t("<gray>Current power: <green>" + bookshelfPower + "<gray>/60"),
                        t(""),
                        t("<gray>Surround the enchanting table"),
                        t("<gray>with bookshelves to increase power.")
                )
        ).build();
    }

    private ItemStack currentItem() {
        return inventory.getItemStack(ITEM_SLOT);
    }

    private void refresh() {
        clearSlots(ENCHANT_LIST_SLOTS);
        inventory.setItemStack(BACK_SLOT, FILLER);

        ItemStack item = currentItem();
        if (item.isAir()) {
            selectedEnchantment = null;
            inventory.setTitle(titleComponent());
            inventory.setItemStack(STATUS_SLOT, PLACE_ITEM);
            return;
        }

        SkyblockItem template = EnchantingItemResolver.resolve(item);

        if (template == null || !template.isEnchantable() || item.amount() > 1) {
            selectedEnchantment = null;
            inventory.setTitle(titleComponent());
            inventory.setItemStack(STATUS_SLOT, INVALID_ITEM);
            return;
        }

        List<EnchantmentRegistry> available = EnchantmentRegistry.forItemType(template.getItemType());
        if (available.isEmpty()) {
            selectedEnchantment = null;
            inventory.setTitle(titleComponent());
            inventory.setItemStack(STATUS_SLOT, INVALID_ITEM);
            return;
        }

        if (selectedEnchantment != null && !available.contains(selectedEnchantment)) {
            selectedEnchantment = null;
            inventory.setTitle(titleComponent());
        }

        if (selectedEnchantment == null) {
            inventory.setItemStack(STATUS_SLOT, FILLER);
            showEnchantmentList(available, item);
        } else {
            inventory.setItemStack(STATUS_SLOT, FILLER);
            showLevelSelector(item);
        }
    }

    private void showEnchantmentList(List<EnchantmentRegistry> available, ItemStack item) {
        List<EnchantmentRegistry> page = available.stream().limit(ENCHANT_LIST_SLOTS.length).toList();
        for (int i = 0; i < page.size(); i++) {
            inventory.setItemStack(ENCHANT_LIST_SLOTS[i], enchantmentBookItem(page.get(i), item));
        }
    }

    private ItemStack enchantmentBookItem(EnchantmentRegistry reg, ItemStack item) {
        Enchantment ench = reg.getEnchantment();
        int currentLevel = EnchantmentNBT.getEnchantmentLevel(item, reg);
        boolean unlocked = bookshelfPower >= ench.getBookshelfRequirement();

        List<Component> lore = new ArrayList<>();
        StringUtility.splitByWordAndLength(ench.getDescription(ench.getMinLevel()), 30)
                .forEach(line -> lore.add(t("<gray>" + line)));
        lore.add(t(""));

        if (currentLevel > 0) {
            lore.add(t("<green>  " + reg.getDisplayName() + " " + StringUtility.getAsRomanNumeral(currentLevel) + " <bold>Applied"));
        } else {
            lore.add(t("<red>  " + reg.getDisplayName() + " <bold>Missing"));
        }

        lore.add(t(""));
        lore.add(unlocked
                ? t("<yellow>Click to view levels!")
                : t("<red>Requires <bold>" + ench.getBookshelfRequirement() + "</bold> Bookshelf Power"));

        return ItemStackCreator.getStack("<green>" + reg.getDisplayName(), Material.ENCHANTED_BOOK, 1, lore).build();
    }

    private void showLevelSelector(ItemStack item) {
        inventory.setItemStack(BACK_SLOT, BACK_ARROW);

        Enchantment ench = selectedEnchantment.getEnchantment();
        int currentLevel = EnchantmentNBT.getEnchantmentLevel(item, selectedEnchantment);

        for (int level = ench.getMinLevel(); level <= ench.getMaxLevel(); level++) {
            int idx = level - 1;
            if (idx >= LEVEL_SELECT_SLOTS.length) {
                break;
            }
            int cost = ench.getLevelCosts().getOrDefault(level, 0);
            inventory.setItemStack(LEVEL_SELECT_SLOTS[idx], levelItem(level, cost, currentLevel));
        }
    }

    private ItemStack levelItem(int level, int cost, int currentLevel) {
        Enchantment ench = selectedEnchantment.getEnchantment();
        List<Component> lore = new ArrayList<>();
        StringUtility.splitByWordAndLength(ench.getDescription(level), 30)
                .forEach(line -> lore.add(t("<gray>" + line)));
        lore.add(t(""));

        if (currentLevel > level) {
            lore.add(t("<gray>Cost:"));
            lore.add(player.getLevel() >= cost ? t("<green>" + cost + " XP Levels") : t("<red>" + cost + " XP Levels"));
            lore.add(t(""));
            lore.add(t("<red>Higher level already present."));
            return ItemStackCreator.getStack(
                    "<blue>" + selectedEnchantment.getDisplayName() + " " + StringUtility.getAsRomanNumeral(level),
                    Material.GRAY_DYE, 1, lore
            ).build();
        }

        if (currentLevel == level) {
            lore.add(t("<green>Already applied"));
            lore.add(t(""));
        }

        lore.add(t("<gray>Cost:"));
        boolean canAfford = player.getLevel() >= cost;
        lore.add(canAfford ? t("<green>" + cost + " XP Levels") : t("<red>" + cost + " XP Levels"));
        lore.add(t(""));
        if (canAfford) {
            lore.add(currentLevel >= level ? t("<yellow>Click to remove!") : t("<yellow>Click to enchant!"));
        } else {
            lore.add(t("<red>Insufficient XP levels!"));
        }

        return ItemStackCreator.getStack(
                "<blue>" + selectedEnchantment.getDisplayName() + " " + StringUtility.getAsRomanNumeral(level),
                Material.ENCHANTED_BOOK, 1, lore
        ).build();
    }

    private void onPreClick(InventoryPreClickEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer clicker) || !clicker.equals(player)) {
            return;
        }

        int slot = event.getSlot();

        if (slot == ITEM_SLOT || slot >= inventory.getSize()) {
            return;
        }

        event.setCancelled(true);

        if (slot == CLOSE_SLOT) {
            player.closeInventory();
            return;
        }
        if (slot == BACK_SLOT && selectedEnchantment != null) {
            selectedEnchantment = null;
            inventory.setTitle(titleComponent());
            refresh();
            return;
        }

        if (selectedEnchantment == null) {
            handleEnchantListClick(slot);
        } else {
            handleLevelClick(slot);
        }
    }

    private void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if (slot == ITEM_SLOT) {
            MinecraftServer.getSchedulerManager().scheduleNextTick(this::refresh);
            return;
        }
        if (slot >= inventory.getSize()) {
            MinecraftServer.getSchedulerManager().scheduleNextTick(this::refresh);
        }
    }

    private void handleEnchantListClick(int slot) {
        ItemStack item = currentItem();
        for (int i = 0; i < ENCHANT_LIST_SLOTS.length; i++) {
            if (ENCHANT_LIST_SLOTS[i] != slot) {
                continue;
            }
            SkyblockItem template = EnchantingItemResolver.resolve(item);
            if (template == null) {
                return;
            }
            List<EnchantmentRegistry> available = EnchantmentRegistry.forItemType(template.getItemType());
            if (i >= available.size()) {
                return;
            }
            EnchantmentRegistry clicked = available.get(i);
            int req = clicked.getEnchantment().getBookshelfRequirement();
            if (bookshelfPower < req) {
                player.sendMessage(t("<red>You need <bold>" + req
                        + "</bold> Bookshelf Power to unlock " + clicked.getDisplayName() + "!"));
                return;
            }
            selectedEnchantment = clicked;
            inventory.setTitle(titleComponent());
            refresh();
            return;
        }
    }

    private void handleLevelClick(int slot) {
        ItemStack item = currentItem();
        if (item.isAir()) {
            selectedEnchantment = null;
            inventory.setTitle(titleComponent());
            refresh();
            return;
        }

        Enchantment ench = selectedEnchantment.getEnchantment();
        for (int i = 0; i < LEVEL_SELECT_SLOTS.length; i++) {
            if (LEVEL_SELECT_SLOTS[i] != slot) {
                continue;
            }
            int level = ench.getMinLevel() + i;
            if (level > ench.getMaxLevel()) {
                return;
            }
            if (inventory.getItemStack(slot).material() == Material.GRAY_DYE) {
                return;
            }
            int cost = ench.getLevelCosts().getOrDefault(level, 0);
            if (player.getLevel() < cost) {
                player.sendMessage(t("<red>You need <bold>" + cost + "</bold> XP levels!"));
                return;
            }
            int currentLevel = EnchantmentNBT.getEnchantmentLevel(item, selectedEnchantment);
            if (currentLevel < level) {
                inventory.setItemStack(ITEM_SLOT, EnchantmentNBT.applyEnchantment(item, player, selectedEnchantment, level));
                player.setLevel(player.getLevel() - cost);
                player.sendMessage(t("<green>Applied <bold>" + selectedEnchantment.getDisplayName()
                        + " " + StringUtility.getAsRomanNumeral(level) + "</bold> to your item!"));
            } else {
                inventory.setItemStack(ITEM_SLOT, EnchantmentNBT.removeEnchantment(item, player, selectedEnchantment));
                player.setLevel(player.getLevel() - cost);
                player.sendMessage(t("<red>Removed <bold>" + selectedEnchantment.getDisplayName()
                        + "</bold> from your item!"));
            }
            refresh();
            return;
        }
    }

    private void onClose(InventoryCloseEvent event) {
        ItemStack item = currentItem();
        if (!item.isAir()) {
            player.getInventory().addItemStack(item);
            inventory.setItemStack(ITEM_SLOT, ItemStack.AIR);
        }
        ItemStack cursor = player.getInventory().getCursorItem();
        if (!cursor.isAir()) {
            player.getInventory().addItemStack(cursor);
            player.getInventory().setCursorItem(ItemStack.AIR);
        }
    }

    private void clearSlots(int[] slots) {
        for (int s : slots) {
            if (s != ITEM_SLOT) {
                inventory.setItemStack(s, FILLER);
            }
        }
    }
}
