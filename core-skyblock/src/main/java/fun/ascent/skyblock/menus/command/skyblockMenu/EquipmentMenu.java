package fun.ascent.skyblock.menus.command.skyblockMenu;

import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.StatCategory;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class EquipmentMenu {

    private static final int HELD_ITEM_SLOT = 2;
    private static final int HELMET_SLOT = 11;
    private static final int CHESTPLATE_SLOT = 20;
    private static final int LEGGINGS_SLOT = 29;
    private static final int BOOTS_SLOT = 38;
    private static final int PET_SLOT = 47;

    private static final int NECKLACE_SLOT = 10;
    private static final int CLOAK_SLOT = 19;
    private static final int BELT_SLOT = 28;
    private static final int GLOVES_SLOT = 37;

    private static final int COMBAT_STATS_SLOT = 14;
    private static final int MINING_STATS_SLOT = 15;
    private static final int FARMING_STATS_SLOT = 16;
    private static final int FORAGING_STATS_SLOT = 23;
    private static final int FISHING_STATS_SLOT = 24;
    private static final int MISC_STATS_SLOT = 25;
    private static final int HUNTING_STATS_SLOT = 32;
    private static final int WISDOM_STATS_SLOT = 34;

    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;
    private static final int POTIONS_SLOT = 50;
    private static final int ACHIEVEMENTS_SLOT = 51;

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private static Component t(String text) {
        return MM.deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, t("Your Equipment and Stats"));

        fill(inv);

        placeEquipment(inv, player);
        placeStatCategories(inv, player);
        placeBottomRow(inv);

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == CLOSE_SLOT) {
                player.closeInventory();
            } else if (slot == BACK_SLOT) {
                fun.ascent.skyblock.menus.SkyblockMenu.open(player);
            }
        });

        player.openInventory(inv);
    }

    private static void placeEquipment(Inventory inv, SkyblockPlayer player) {
        inv.setItemStack(HELD_ITEM_SLOT, equipmentDisplayItem(player, EquipmentSlot.MAIN_HAND, "Held Item"
        ));
        inv.setItemStack(HELMET_SLOT, equipmentDisplayItem(player, EquipmentSlot.HELMET, "Helmet"
        ));
        inv.setItemStack(CHESTPLATE_SLOT, equipmentDisplayItem(player, EquipmentSlot.CHESTPLATE, "Chestplate"
        ));
        inv.setItemStack(LEGGINGS_SLOT, equipmentDisplayItem(player, EquipmentSlot.LEGGINGS, "Leggings"
        ));
        inv.setItemStack(BOOTS_SLOT, equipmentDisplayItem(player, EquipmentSlot.BOOTS, "Boots"
        ));

        inv.setItemStack(NECKLACE_SLOT, emptySlotItem("Necklace"));
        inv.setItemStack(CLOAK_SLOT, emptySlotItem("Cloak"));
        inv.setItemStack(BELT_SLOT, emptySlotItem("Belt"));
        inv.setItemStack(GLOVES_SLOT, emptySlotItem("Gloves"));

        inv.setItemStack(PET_SLOT, emptySlotItem("Pet"));
    }

    private static ItemStack equipmentDisplayItem(SkyblockPlayer player, EquipmentSlot slot, String slotName) {
        ItemStack equipped = player.getEquipment(slot);
        if (!equipped.isAir() && ItemNBT.isSkyblockItem(equipped)) {
            return equipped;
        }

        return emptySlotItem(slotName);
    }

    private static ItemStack emptySlotItem(String slotName) {
        List<Component> lore = new ArrayList<>();
        return ItemStack.builder(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                .customName(t("<dark_gray>Empty " + slotName + " Slot"))
                .lore(lore)
                .build();
    }

    private static void placeStatCategories(Inventory inv, SkyblockPlayer player) {
        inv.setItemStack(COMBAT_STATS_SLOT, buildStatItem(player, StatCategory.COMBAT,
                "<red>Combat Stats", Material.STONE_SWORD));

        inv.setItemStack(MINING_STATS_SLOT, buildStatItem(player, StatCategory.MINING,
                "<gold>Mining Stats", Material.STONE_PICKAXE));
        inv.setItemStack(FARMING_STATS_SLOT, buildStatItem(player, StatCategory.FARMING,
                "<green>Farming Stats", Material.GOLDEN_HOE));
        inv.setItemStack(FORAGING_STATS_SLOT, buildStatItem(player, StatCategory.FORAGING,
                "<dark_green>Foraging Stats", Material.JUNGLE_SAPLING));
        inv.setItemStack(FISHING_STATS_SLOT, buildStatItem(player, StatCategory.FISHING,
                "<aqua>Fishing Stats", Material.FISHING_ROD));
        inv.setItemStack(MISC_STATS_SLOT, buildStatItem(player, StatCategory.MISC,
                "<light_purple>Misc Stats", Material.CLOCK));
        inv.setItemStack(HUNTING_STATS_SLOT, buildStatItem(player, StatCategory.HUNTING,
                "<yellow>Hunting Stats", Material.LEAD));
        inv.setItemStack(WISDOM_STATS_SLOT, buildStatItem(player, StatCategory.WISDOM,
                "<dark_aqua>Wisdom Stats", Material.BOOK));
    }

    private static String getCategoryDescription(StatCategory category) {
        return switch (category) {
            case COMBAT -> "Stats that influence how much\ndamage you take and deal when in\ncombat.";
            case MINING -> "Stats that influence what you can\nbreak, how quickly you can break it,\nand how many drops you receive\nwhen mining.";
            case FARMING -> "Stats that influence how many drops\nyou receive and how many pests\nspawn when farming.";
            case FORAGING -> "Stats that include how many drops\nyou receive when foraging.";
            case FISHING -> "Stats that influence what you catch\nand how quickly you catch it while\nfishing.";
            case MISC -> "Stats augment various aspects\nof your gameplay.";
            case HUNTING -> "Stats that affect your hunting\nabilities and loot from\nhunting creatures.";
            case WISDOM -> "Stats that influence how quickly you\nhunt mobs and how many shards you\nget for doing so.";
            default -> "";
        };
    }

    private static ItemStack buildStatItem(SkyblockPlayer player, StatCategory category,
                                           String title, Material material) {
        List<Component> lore = new ArrayList<>();

        String desc = getCategoryDescription(category);
        if (!desc.isEmpty()) {
            for (String line : desc.split("\n")) {
                lore.add(t("<gray>" + line));
            }
            lore.add(Component.empty());
        }

        List<Component> statLines = new ArrayList<>();
        for (Stats stat : Stats.values()) {
            if (stat.getStatCategory() != category) continue;
            double value = player.playerStat(stat);
            if (value == 0 && stat.getBaseStat() == 0) continue;
            String suffix = stat.getStatIntType() ? "%" : "";
            statLines.add(t(" " + stat.getStatSymbol() + " " + stat.getStatFormattedDisplay()
                    + "<white> " + (int) value + suffix));
        }

        if (statLines.isEmpty()) {
            lore.add(t(" <dark_gray>You do not have any stats to show in"));
            lore.add(t(" <dark_gray>this category!"));
        } else {
            lore.addAll(statLines);
            lore.add(Component.empty());
            lore.add(t("<yellow>Click for details!"));
        }
        return ItemStack.builder(material)
                .customName(t(title))
                .lore(lore)
                .build();
    }

    private static void placeBottomRow(Inventory inv) {
        inv.setItemStack(BACK_SLOT, ItemStack.builder(Material.ARROW)
                .customName(t("<green>Go Back"))
                .build());
        inv.setItemStack(CLOSE_SLOT, ItemStack.builder(Material.BARRIER)
                .customName(t("<red>Close"))
                .build());
        inv.setItemStack(POTIONS_SLOT, ItemStack.builder(Material.POTION)
                .customName(t("<green>Active Potion Effects"))
                .lore(List.of(t("<gray>Coming soon fr")))
                .build());
        inv.setItemStack(ACHIEVEMENTS_SLOT, ItemStack.builder(Material.DIAMOND)
                .customName(t("<green>SkyBlock Achievements"))
                .lore(List.of(t("<gray>Coming soon fr")))
                .build());
    }

    private static void fill(Inventory inventory) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty())
                .build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItemStack(i, filler);
        }
    }
}
