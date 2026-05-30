package fun.ascent.skyblock.player.bestiary.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.bestiary.BestiaryFamily;
import fun.ascent.skyblock.player.bestiary.BestiaryMilestones;
import fun.ascent.skyblock.player.bestiary.BestiaryProgress;
import fun.ascent.skyblock.player.bestiary.BestiarySection;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

import java.util.List;

public final class BestiarySectionMenu {

    private static final int[] DISPLAY_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };
    private static final int TITLE_SLOT = 4;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    private BestiarySectionMenu() {
    }

    public static void open(SkyblockPlayer player, BestiarySection section) {
        ProfilePlayer profile = player.getActiveProfileData();
        if (profile == null) {
            return;
        }

        BestiaryProgress progress = profile.getBestiaryProgress();
        List<BestiaryFamily> families = section.families();
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW,
                "Bestiary -> " + StringUtility.stripColor(section.coloredName()));

        BestiaryMenuFormat.fill(inventory);
        inventory.setItemStack(TITLE_SLOT, BestiaryMenuFormat.icon(
                section.coloredName(),
                section.iconMaterial(),
                section.iconTexture(),
                BestiaryMenuFormat.sectionLore(progress, section)
        ));
        inventory.setItemStack(BACK_SLOT, BestiaryMenuFormat.backButton("Bestiary"));
        inventory.setItemStack(CLOSE_SLOT, BestiaryMenuFormat.closeButton());

        for (int i = 0; i < families.size() && i < DISPLAY_SLOTS.length; i++) {
            BestiaryFamily family = families.get(i);
            int kills = family.totalKills(progress);
            int tier = BestiaryMilestones.currentTier(family, kills);

            if (kills > 0) {
                String title = "<green>" + family.displayName() + (tier > 0 ? " " + StringUtility.getAsRomanNumeral(tier) : "");
                inventory.setItemStack(DISPLAY_SLOTS[i], BestiaryMenuFormat.icon(
                        title,
                        family.iconMaterial(),
                        family.iconTexture(),
                        BestiaryMenuFormat.familyLore(progress, family, true)
                ));
            } else {
                inventory.setItemStack(DISPLAY_SLOTS[i], BestiaryMenuFormat.icon(
                        "<red>" + family.displayName(),
                        family.iconMaterial(),
                        family.iconTexture(),
                        List.of(
                                fun.ascent.common.StringUtility.text("<gray>Kill a mob belonging to this Family to"),
                                fun.ascent.common.StringUtility.text("<gray>unlock it in your Bestiary!")
                        )
                ));
            }
        }

        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            if (!(event.getPlayer() instanceof SkyblockPlayer clickingPlayer)) {
                return;
            }
            event.setCancelled(true);

            int slot = event.getSlot();
            if (slot == CLOSE_SLOT) {
                clickingPlayer.closeInventory();
                return;
            }
            if (slot == BACK_SLOT) {
                BestiaryOverviewMenu.open(clickingPlayer);
                return;
            }

            for (int i = 0; i < families.size() && i < DISPLAY_SLOTS.length; i++) {
                if (slot == DISPLAY_SLOTS[i] && families.get(i).unlocked(progress)) {
                    BestiaryFamilyMenu.open(clickingPlayer, section, families.get(i));
                    return;
                }
            }
        });

        player.openInventory(inventory);
    }
}
