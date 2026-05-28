package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class SkyblockLevelEmblemsMenu {

    public static final Tag<String> ACTIVE_EMBLEM_TAG = Tag.String("active_emblem");

    private static final int DEFAULT_SLOT = 11;
    private static final int STAR_SLOT = 12;
    private static final int DIAMOND_SLOT = 13;
    private static final int CROWN_SLOT = 14;

    private static final int BACK_SLOT = 30;
    private static final int CLOSE_SLOT = 31;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_4_ROW, "Chat Emblems");
        fillBackground(inv);

        int level = player.getActiveProfileData() != null ? player.getActiveProfileData().level.curLevel : 0;
        String active = player.getTag(ACTIVE_EMBLEM_TAG);
        if (active == null) active = "";

        inv.setItemStack(DEFAULT_SLOT, buildDefaultItem(active.isEmpty()));
        inv.setItemStack(STAR_SLOT, buildStarItem(level, active.equals("⚝")));
        inv.setItemStack(DIAMOND_SLOT, buildDiamondItem(level, active.equals("❖")));
        inv.setItemStack(CROWN_SLOT, buildCrownItem(level, active.equals("👑")));

        // Navigation
        inv.setItemStack(BACK_SLOT, buildBackButton());
        inv.setItemStack(CLOSE_SLOT, buildCloseButton());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();

            if (slot == CLOSE_SLOT) {
                player.closeInventory();
            } else if (slot == BACK_SLOT) {
                player.closeInventory();
                SkyblockLevelMenu.open(player);
            } else if (slot == DEFAULT_SLOT) {
                player.setTag(ACTIVE_EMBLEM_TAG, "");
                playSuccessEffects(player);
                open(player);
            } else if (slot == STAR_SLOT) {
                if (level >= 5) {
                    player.setTag(ACTIVE_EMBLEM_TAG, "⚝");
                    playSuccessEffects(player);
                    open(player);
                } else {
                    playFailureEffects(player, 5);
                }
            } else if (slot == DIAMOND_SLOT) {
                if (level >= 10) {
                    player.setTag(ACTIVE_EMBLEM_TAG, "❖");
                    playSuccessEffects(player);
                    open(player);
                } else {
                    playFailureEffects(player, 10);
                }
            } else if (slot == CROWN_SLOT) {
                if (level >= 15) {
                    player.setTag(ACTIVE_EMBLEM_TAG, "👑");
                    playSuccessEffects(player);
                    open(player);
                } else {
                    playFailureEffects(player, 15);
                }
            }
        });

        player.openInventory(inv);
    }

    private static void playSuccessEffects(SkyblockPlayer player) {
        player.playSound(Sound.sound(
                Key.key("entity.experience_orb.pickup"),
                Sound.Source.PLAYER, 0.5f, 1.5f));
        player.sendMessage(text("<green>Selected chat emblem successfully!"));
    }

    private static void playFailureEffects(SkyblockPlayer player, int reqLevel) {
        player.playSound(Sound.sound(
                Key.key("entity.enderman.teleport"),
                Sound.Source.PLAYER, 0.5f, 0.5f));
        player.sendMessage(text("<red>You must be at least SkyBlock Level " + reqLevel + " to use this emblem!"));
    }

    private static ItemStack buildDefaultItem(boolean isActive) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Do not display any emblem inside your"));
        lore.add(text("<gray>Level prefix brackets in chat."));
        lore.add(Component.empty());
        lore.add(text(isActive ? "<green>SELECTED" : "<yellow>Click to select!"));

        return ItemStack.builder(Material.STONE_BUTTON)
                .customName(text("<green>Default (No Emblem)"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildStarItem(int currentLevel, boolean isActive) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Showcase a starter star emblem ⚝ next"));
        lore.add(text("<gray>to your level inside chat brackets."));
        lore.add(Component.empty());
        lore.add(text("<gray>Required Level: <gold>5"));
        lore.add(text("<gray>Status: " + (currentLevel >= 5 ? "<green>Unlocked" : "<red>Locked")));
        lore.add(Component.empty());
        lore.add(text(isActive ? "<green>SELECTED" : currentLevel >= 5 ? "<yellow>Click to select!" : "<red>Unlocks at Level 5"));

        return ItemStack.builder(Material.NETHER_STAR)
                .customName(text("<green>Star Emblem (⚝)"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildDiamondItem(int currentLevel, boolean isActive) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Showcase a diamond emblem ❖ next"));
        lore.add(text("<gray>to your level inside chat brackets."));
        lore.add(Component.empty());
        lore.add(text("<gray>Required Level: <gold>10"));
        lore.add(text("<gray>Status: " + (currentLevel >= 10 ? "<green>Unlocked" : "<red>Locked")));
        lore.add(Component.empty());
        lore.add(text(isActive ? "<green>SELECTED" : currentLevel >= 10 ? "<yellow>Click to select!" : "<red>Unlocks at Level 10"));

        return ItemStack.builder(Material.DIAMOND)
                .customName(text("<green>Diamond Emblem (❖)"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildCrownItem(int currentLevel, boolean isActive) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Showcase an elite crown emblem 👑 next"));
        lore.add(text("<gray>to your level inside chat brackets."));
        lore.add(Component.empty());
        lore.add(text("<gray>Required Level: <gold>15"));
        lore.add(text("<gray>Status: " + (currentLevel >= 15 ? "<green>Unlocked" : "<red>Locked")));
        lore.add(Component.empty());
        lore.add(text(isActive ? "<green>SELECTED" : currentLevel >= 15 ? "<yellow>Click to select!" : "<red>Unlocks at Level 15"));

        return ItemStack.builder(Material.GOLDEN_CARROT)
                .customName(text("<green>Crown Emblem (👑)"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildBackButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To SkyBlock Levels")))
                .build();
    }

    private static ItemStack buildCloseButton() {
        return ItemStack.builder(Material.BARRIER)
                .customName(text("<red>Close"))
                .build();
    }

    private static void fillBackground(Inventory inv) {
        ItemStack pane = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty())
                .build();
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItemStack(i, pane);
        }
    }
}
