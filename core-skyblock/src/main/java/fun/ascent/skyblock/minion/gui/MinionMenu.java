package fun.ascent.skyblock.minion.gui;

import fun.ascent.skyblock.minion.MinionItems;
import fun.ascent.skyblock.minion.MinionManager;
import fun.ascent.skyblock.minion.SkyblockMinion;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class MinionMenu {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final int IDEAL_LAYOUT_SLOT = 3;
    private static final int INFO_SLOT = 4;
    private static final int NEXT_TIER_SLOT = 5;
    private static final int SKIN_SLOT = 10;
    private static final int FUEL_SLOT = 19;
    private static final int SHIPPING_SLOT = 28;
    private static final int UPGRADE_ONE_SLOT = 37;
    private static final int UPGRADE_TWO_SLOT = 46;
    private static final int COLLECT_SLOT = 48;
    private static final int QUICK_UPGRADE_SLOT = 50;
    private static final int PICKUP_SLOT = 53;
    private static final int[] STORAGE_SLOTS = {21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 39, 40, 41, 42, 43};
    private static final Map<Inventory, SkyblockMinion> OPEN_MENUS = new HashMap<>();
    private static boolean updaterStarted;

    private MinionMenu() {
    }

    public static void open(SkyblockPlayer player, SkyblockMinion minion) {
        ensureUpdater();
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, minion.getType().getDisplayName());
        render(inventory, minion);
        OPEN_MENUS.put(inventory, minion);

        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            if (!(event.getPlayer() instanceof SkyblockPlayer clickingPlayer)) {
                return;
            }
            event.setCancelled(true);

            if (event.getSlot() == COLLECT_SLOT) {
                List<ItemStack> collected = minion.collectAll();
                if (collected == null) {
                    clickingPlayer.sendMessage(MINI_MESSAGE.deserialize("<red>This minion has nothing to collect yet.</red>"));
                    return;
                }
                for (ItemStack stack : collected) {
                    clickingPlayer.getInventory().addItemStack(stack);
                }
                clickingPlayer.sendMessage(MINI_MESSAGE.deserialize("<green>Collected resources from your minion.</green>"));
                render(inventory, minion);
                return;
            }

            if (event.getSlot() == QUICK_UPGRADE_SLOT) {
                if (!minion.canUpgrade()) {
                    clickingPlayer.sendMessage(MINI_MESSAGE.deserialize("<red>This minion is already max tier.</red>"));
                    return;
                }
                minion.upgrade();
                clickingPlayer.sendMessage(MINI_MESSAGE.deserialize("<green>Your minion is now Tier " + minion.getTier() + ".</green>"));
                render(inventory, minion);
                return;
            }

            if (event.getSlot() == PICKUP_SLOT) {
                OPEN_MENUS.remove(inventory);
                MinionManager.removeMinion(minion);
                clickingPlayer.getInventory().addItemStack(minion.toPlacementItem());
                clickingPlayer.sendMessage(MINI_MESSAGE.deserialize("<yellow>You picked up your minion.</yellow>"));
                clickingPlayer.closeInventory();
                return;
            }

            if (event.getSlot() == IDEAL_LAYOUT_SLOT) {
                clickingPlayer.sendMessage(MINI_MESSAGE.deserialize("<green>Ideal Layout: <white>" + layoutText(minion) + "</white>"));
                return;
            }

            if (event.getSlot() == NEXT_TIER_SLOT) {
                if (!minion.canUpgrade()) {
                    clickingPlayer.sendMessage(MINI_MESSAGE.deserialize("<red>This minion is already max tier.</red>"));
                    return;
                }
                clickingPlayer.sendMessage(MINI_MESSAGE.deserialize("<yellow>Use Quick-Upgrade to upgrade this minion instantly for now.</yellow>"));
            }
        });

        player.openInventory(inventory);
    }

    private static void render(Inventory inventory, SkyblockMinion minion) {
        fill(inventory);

        inventory.setItemStack(IDEAL_LAYOUT_SLOT, ItemStack.builder(Material.REDSTONE_TORCH)
                .customName(MINI_MESSAGE.deserialize("<green>Ideal Layout"))
                .lore(List.of(
                        MINI_MESSAGE.deserialize("<gray>View the most efficient spot for"),
                        MINI_MESSAGE.deserialize("<gray>this minion to be placed in."),
                        Component.empty(),
                        MINI_MESSAGE.deserialize("<yellow>Click to view!")
                ))
                .build());

        inventory.setItemStack(INFO_SLOT, ItemStack.builder(minion.getType().getIcon())
                .customName(MINI_MESSAGE.deserialize("<green>" + minion.getType().getDisplayName() + " " + MinionItems.roman(minion.getTier())))
                .lore(buildInfoLore(minion))
                .build());

        inventory.setItemStack(NEXT_TIER_SLOT, ItemStack.builder(Material.GOLD_INGOT)
                .customName(MINI_MESSAGE.deserialize("<green>Next Tier"))
                .lore(buildNextTierLore(minion))
                .build());

        inventory.setItemStack(SKIN_SLOT, placeholder(Material.LIME_STAINED_GLASS_PANE, "<green>Minion Skin Slot",
                "<gray>You can insert a Minion Skin",
                "<gray>here to change the appearance of",
                "<gray>your minion."));

        inventory.setItemStack(FUEL_SLOT, placeholder(Material.ORANGE_STAINED_GLASS_PANE, "<green>Fuel",
                "<gray>Increase the speed of your",
                "<gray>minion by adding minion fuel",
                "<gray>items here.",
                "",
                "<red>Not available yet in this",
                "<red>Minestom port."));

        inventory.setItemStack(SHIPPING_SLOT, placeholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "<green>Automated Shipping",
                "<gray>Add a hopper-style upgrade here",
                "<gray>to auto-sell generated items after",
                "<gray>the minion inventory is full.",
                "",
                "<red>Not available yet in this",
                "<red>Minestom port."));

        inventory.setItemStack(UPGRADE_ONE_SLOT, placeholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "<green>Upgrade Slot",
                "<gray>You can improve your minion by",
                "<gray>adding a minion upgrade item",
                "<gray>here."));

        inventory.setItemStack(UPGRADE_TWO_SLOT, placeholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "<green>Upgrade Slot",
                "<gray>You can improve your minion by",
                "<gray>adding a minion upgrade item",
                "<gray>here."));

        inventory.setItemStack(COLLECT_SLOT, ItemStack.builder(Material.CHEST)
                .customName(MINI_MESSAGE.deserialize("<green>Collect All"))
                .lore(List.of(MINI_MESSAGE.deserialize("<yellow>Click to collect all items!")))
                .build());

        inventory.setItemStack(QUICK_UPGRADE_SLOT, ItemStack.builder(Material.DIAMOND)
                .customName(MINI_MESSAGE.deserialize("<green>Quick-Upgrade Minion"))
                .lore(buildQuickUpgradeLore(minion))
                .build());

        inventory.setItemStack(PICKUP_SLOT, ItemStack.builder(Material.BEDROCK)
                .customName(MINI_MESSAGE.deserialize("<green>Pickup Minion"))
                .lore(List.of(MINI_MESSAGE.deserialize("<yellow>Click to pickup!")))
                .build());

        renderStorage(inventory, minion);
    }

    private static void fill(Inventory inventory) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty())
                .build();
        for (int slot = 0; slot <= 20; slot++) {
            inventory.setItemStack(slot, filler);
        }
        for (int slot : new int[]{26, 27, 29, 35, 36, 38, 44, 45, 47, 49, 51, 52}) {
            inventory.setItemStack(slot, filler);
        }
        for (int slot = 44; slot < inventory.getSize(); slot++) {
            inventory.setItemStack(slot, filler);
        }
    }

    private static List<Component> buildInfoLore(SkyblockMinion minion) {
        List<Component> lore = new ArrayList<>();
        lore.add(MINI_MESSAGE.deserialize("<gray>Place this minion and it will"));
        lore.add(MINI_MESSAGE.deserialize("<gray>start " + minion.getType().getPlacementDescription()));
        lore.add(MINI_MESSAGE.deserialize("<gray>" + minion.getType().getLayoutHint()));
        lore.add(MINI_MESSAGE.deserialize("<gray>Minions also work when you are"));
        lore.add(MINI_MESSAGE.deserialize("<gray>offline!"));
        lore.add(Component.empty());
        lore.add(MINI_MESSAGE.deserialize("<gray>Time Between Actions: <green>" + minion.getData().getActionDelaySeconds() + "s</green>"));
        lore.add(MINI_MESSAGE.deserialize("<gray>Max Storage: <yellow>" + minion.getData().getMaxStorage() + "</yellow>"));
        lore.add(MINI_MESSAGE.deserialize("<gray>Resources Generated: <aqua>" + minion.getTotalGenerated() + "</aqua>"));
        lore.add(MINI_MESSAGE.deserialize(minion.hasIdealLayout() ? "<gray>Status: <green>Ready</green>" : "<gray>Status: <red>This location is not perfect!</red>"));
        return lore;
    }

    private static List<Component> buildNextTierLore(SkyblockMinion minion) {
        List<Component> lore = new ArrayList<>();
        lore.add(MINI_MESSAGE.deserialize("<gray>View the items required to"));
        lore.add(MINI_MESSAGE.deserialize("<gray>upgrade this minion to the next tier."));
        lore.add(Component.empty());
        if (minion.canUpgrade()) {
            lore.add(MINI_MESSAGE.deserialize("<gray>Time Between Actions: <dark_gray>" + minion.getData().getActionDelaySeconds() + "s <white>-> <green>" + minion.getType().getActionDelaySeconds(minion.getTier() + 1) + "s"));
            lore.add(MINI_MESSAGE.deserialize("<gray>Max Storage: <dark_gray>" + minion.getData().getMaxStorage() + " <white>-> <green>" + minion.getType().getMaxStorage(minion.getTier() + 1)));
            lore.add(Component.empty());
            lore.add(MINI_MESSAGE.deserialize("<yellow>Click to view!"));
            return lore;
        }
        lore.add(MINI_MESSAGE.deserialize("<green>Time Between Actions: " + minion.getData().getActionDelaySeconds() + "s"));
        lore.add(MINI_MESSAGE.deserialize("<green>Max Storage: " + minion.getData().getMaxStorage()));
        lore.add(Component.empty());
        lore.add(MINI_MESSAGE.deserialize("<red>Max level."));
        return lore;
    }

    private static List<Component> buildQuickUpgradeLore(SkyblockMinion minion) {
        List<Component> lore = new ArrayList<>();
        lore.add(MINI_MESSAGE.deserialize("<gray>Click here to upgrade your"));
        lore.add(MINI_MESSAGE.deserialize("<gray>minion to the next tier."));
        lore.add(Component.empty());
        if (minion.canUpgrade()) {
            lore.add(MINI_MESSAGE.deserialize("<gray>Time Between Actions: <dark_gray>" + minion.getData().getActionDelaySeconds() + "s <white>-> <green>" + minion.getType().getActionDelaySeconds(minion.getTier() + 1) + "s"));
            lore.add(MINI_MESSAGE.deserialize("<gray>Max Storage: <dark_gray>" + minion.getData().getMaxStorage() + " <white>-> <green>" + minion.getType().getMaxStorage(minion.getTier() + 1)));
            lore.add(Component.empty());
            lore.add(MINI_MESSAGE.deserialize("<yellow>Recipe Cost Preview: <white>" + minion.getUpgradeCost() + " " + prettify(minion.getType().getOutputMaterial().name()) + "</white>"));
            lore.add(MINI_MESSAGE.deserialize("<yellow>Click to upgrade!</yellow>"));
            return lore;
        }
        lore.add(MINI_MESSAGE.deserialize("<green>Time Between Actions: " + minion.getData().getActionDelaySeconds() + "s"));
        lore.add(MINI_MESSAGE.deserialize("<green>Max Storage: " + minion.getData().getMaxStorage()));
        lore.add(Component.empty());
        lore.add(MINI_MESSAGE.deserialize("<red>Max level."));
        return lore;
    }

    private static void renderStorage(Inventory inventory, SkyblockMinion minion) {
        ItemStack locked = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
                .customName(MINI_MESSAGE.deserialize("<yellow>Storage locked"))
                .build();
        int unlockedSlots = minion.getData().getStorageSlots();
        List<ItemStack> stored = minion.getStoredStacks();
        for (int index = 0; index < STORAGE_SLOTS.length; index++) {
            if (index >= unlockedSlots) {
                inventory.setItemStack(STORAGE_SLOTS[index], locked);
                continue;
            }
            if (index >= stored.size()) {
                inventory.setItemStack(STORAGE_SLOTS[index], ItemStack.AIR);
                continue;
            }
            inventory.setItemStack(STORAGE_SLOTS[index], stored.get(index));
        }
    }

    private static ItemStack placeholder(Material material, String name, String... loreLines) {
        List<Component> lore = new ArrayList<>();
        for (String line : loreLines) {
            lore.add(line.isEmpty() ? Component.empty() : MINI_MESSAGE.deserialize(line));
        }
        return ItemStack.builder(material)
                .customName(MINI_MESSAGE.deserialize(name))
                .lore(lore)
                .build();
    }

    private static String layoutText(SkyblockMinion minion) {
        if (minion.getType().getIdealLayout() != null) {
            return minion.getType().getIdealLayout();
        }
        return minion.getType().getLayoutHint();
    }

    private static String prettify(String value) {
        return value.toLowerCase().replace('_', ' ');
    }

    private static void ensureUpdater() {
        if (updaterStarted) {
            return;
        }
        updaterStarted = true;
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            Iterator<Map.Entry<Inventory, SkyblockMinion>> iterator = OPEN_MENUS.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Inventory, SkyblockMinion> entry = iterator.next();
                Inventory inventory = entry.getKey();
                SkyblockMinion minion = entry.getValue();
                if (inventory.getViewers().isEmpty()) {
                    iterator.remove();
                    continue;
                }
                render(inventory, minion);
            }
        }).repeat(TaskSchedule.tick(20)).schedule();
    }
}
