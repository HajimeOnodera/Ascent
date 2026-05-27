package fun.ascent.skyblock.player.bestiary.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.bestiary.BestiaryProgress;
import fun.ascent.skyblock.player.bestiary.BestiarySection;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public final class BestiaryOverviewMenu {

    private static final int[] DISPLAY_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            29, 30, 32, 33
    };
    private static final int TITLE_SLOT = 4;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    private BestiaryOverviewMenu() {
    }

    public static void open(SkyblockPlayer player) {
        ProfilePlayer profile = player.getActiveProfileData();
        if (profile == null) {
            return;
        }

        BestiaryProgress progress = profile.getBestiaryProgress();
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, "Bestiary");
        BestiaryMenuFormat.fill(inventory);

        inventory.setItemStack(TITLE_SLOT, BestiaryMenuFormat.icon(
                "Bestiary",
                net.minestom.server.item.Material.WRITABLE_BOOK,
                null,
                BestiaryMenuFormat.overallLore(progress)
        ));
        inventory.setItemStack(BACK_SLOT, BestiaryMenuFormat.backButton("Combat Skill"));
        inventory.setItemStack(CLOSE_SLOT, BestiaryMenuFormat.closeButton());

        BestiarySection[] sections = BestiarySection.values();
        for (int i = 0; i < sections.length && i < DISPLAY_SLOTS.length; i++) {
            BestiarySection section = sections[i];
            inventory.setItemStack(DISPLAY_SLOTS[i], BestiaryMenuFormat.icon(
                    section.coloredName(),
                    section.iconMaterial(),
                    section.iconTexture(),
                    new java.util.ArrayList<>(fun.ascent.common.StringUtility.text(section.description()))
            ));
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
                fun.ascent.skyblock.player.skill.gui.SkillDetailMenu.open(clickingPlayer, fun.ascent.skyblock.player.skill.SkillType.COMBAT, 0);
                return;
            }

            for (int i = 0; i < sections.length && i < DISPLAY_SLOTS.length; i++) {
                if (slot == DISPLAY_SLOTS[i] && sections[i].openable()) {
                    BestiarySectionMenu.open(clickingPlayer, sections[i]);
                    return;
                }
            }
        });

        player.openInventory(inventory);
    }
}
