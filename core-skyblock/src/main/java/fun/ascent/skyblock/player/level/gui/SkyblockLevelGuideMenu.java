package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.skill.SkillType;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class SkyblockLevelGuideMenu {

    private static final int SKILLS_SLOT = 19;
    private static final int ACCESSORIES_SLOT = 20;
    private static final int MINIONS_SLOT = 21;
    private static final int COLLECTIONS_SLOT = 22;

    private static final int INFO_SLOT = 50;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    private static final int[] BORDER_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 51, 52, 53
    };

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "SkyBlock Guide");
        fillBackground(inv);
        ItemStack greenGlass = ItemStack.builder(Material.LIME_STAINED_GLASS_PANE)
                .customName(Component.empty())
                .build();
        for (int slot : BORDER_SLOTS) {
            inv.setItemStack(slot, greenGlass);
        }

        ProfilePlayer profile = player.getActiveProfileData();
        if (profile != null) {
            inv.setItemStack(SKILLS_SLOT, buildSkillsItem(player));
            inv.setItemStack(ACCESSORIES_SLOT, buildAccessoriesItem(player));
            inv.setItemStack(MINIONS_SLOT, buildMinionsItem(player));
            inv.setItemStack(COLLECTIONS_SLOT, buildCollectionsItem(player));
        }

        inv.setItemStack(INFO_SLOT, buildInfoItem());
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
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack buildSkillsItem(SkyblockPlayer player) {
        var skillData = player.getSkillData();
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Level up your main Skills to increase your"));
        lore.add(text("<gray>SkyBlock Level and unlock key attributes."));
        lore.add(Component.empty());
        lore.add(text("<green>Starter Skills Milestones (Level IV):"));

        if (skillData != null) {
            int farm = (int) skillData.getLevel(SkillType.FARMING);
            int mine = (int) skillData.getLevel(SkillType.MINING);
            int combat = (int) skillData.getLevel(SkillType.COMBAT);
            int forage = (int) skillData.getLevel(SkillType.FORAGING);
            int fish = (int) skillData.getLevel(SkillType.FISHING);
            int enchant = (int) skillData.getLevel(SkillType.ENCHANTING);

            lore.add(text("  " + (farm >= 4 ? "<green>✔ <dark_gray>Farming Skill IV" : "<red>✖ <white>Farming Skill IV <yellow>(" + farm + "/4)")));
            lore.add(text("  " + (mine >= 4 ? "<green>✔ <dark_gray>Mining Skill IV" : "<red>✖ <white>Mining Skill IV <yellow>(" + mine + "/4)")));
            lore.add(text("  " + (combat >= 4 ? "<green>✔ <dark_gray>Combat Skill IV" : "<red>✖ <white>Combat Skill IV <yellow>(" + combat + "/4)")));
            lore.add(text("  " + (forage >= 4 ? "<green>✔ <dark_gray>Foraging Skill IV" : "<red>✖ <white>Foraging Skill IV <yellow>(" + forage + "/4)")));
            lore.add(text("  " + (fish >= 4 ? "<green>✔ <dark_gray>Fishing Skill IV" : "<red>✖ <white>Fishing Skill IV <yellow>(" + fish + "/4)")));
            lore.add(text("  " + (enchant >= 4 ? "<green>✔ <dark_gray>Enchanting Skill IV" : "<red>✖ <white>Enchanting Skill IV <yellow>(" + enchant + "/4)")));
        } else {
            lore.add(text("  <red>✖ Farming Skill IV"));
            lore.add(text("  <red>✖ Mining Skill IV"));
            lore.add(text("  <red>✖ Combat Skill IV"));
        }

        return ItemStack.builder(Material.DIAMOND_SWORD)
                .customName(text("<green>Skills Checklist"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildAccessoriesItem(SkyblockPlayer player) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Collect and store unique accessories"));
        lore.add(text("<gray>in your bag to gain stat bonuses."));
        lore.add(Component.empty());
        lore.add(text("<green>Recommended Starter Accessories:"));
        lore.add(text("  <gray>• Farming Talisman"));
        lore.add(text("  <gray>• Zombie Talisman"));
        lore.add(text("  <gray>• Mine Affinity Talisman"));
        lore.add(text("  <gray>• Village Affinity Talisman"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Claim unique accessories to earn XP!"));

        return ItemStack.builder(Material.CHEST)
                .customName(text("<green>Accessories Checklist"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildMinionsItem(SkyblockPlayer player) {
        ProfilePlayer profile = player.getActiveProfileData();
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Craft unique minions to automate production"));
        lore.add(text("<gray>and unlock additional minion slots."));
        lore.add(Component.empty());

        if (profile != null && profile.skyblockPlayer != null && player.getActiveProfile() != null) {
            var active = player.getActiveProfile();
            lore.add(text("<gray>Minion Slots: <green>" + active.minionSlots));
            lore.add(text("<gray>Minions Crafted: <green>" + active.minionsCrafted));
            lore.add(text("<gray>Unique Minions: <green>" + active.uniqueMinionsCrafted.size()));
        } else {
            lore.add(text("<gray>Minion Slots: <green>5"));
            lore.add(text("<gray>Unique Minions: <green>0"));
        }

        return ItemStack.builder(Material.IRON_PICKAXE)
                .customName(text("<green>Minions Checklist"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildCollectionsItem(SkyblockPlayer player) {
        var active = player.getActiveProfile();
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Progress through material collections by"));
        lore.add(text("<gray>gathering resources and unlocking recipes."));
        lore.add(Component.empty());

        int unlockedCount = active != null ? active.unlockedCollections.size() : 0;
        lore.add(text("<gray>Unlocked Collections: <green>" + unlockedCount));
        lore.add(Component.empty());
        lore.add(text("<green>Featured Collections:"));
        lore.add(text("  <gray>• Cobblestone Collection"));
        lore.add(text("  <gray>• Wheat Collection"));
        lore.add(text("  <gray>• Oak Wood Foraging Collection"));

        return ItemStack.builder(Material.WHEAT)
                .customName(text("<green>Collections Checklist"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildInfoItem() {
        return ItemStack.builder(Material.REDSTONE_TORCH)
                .customName(text("<green>About SkyBlock Guide"))
                .lore(List.of(
                        text("<gray>The SkyBlock Guide provides customized"),
                        text("<gray>checklist recommendations to unlock"),
                        text("<gray>additional SkyBlock levels, attributes,"),
                        text("<gray>and stat upgrades.")
                ))
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
