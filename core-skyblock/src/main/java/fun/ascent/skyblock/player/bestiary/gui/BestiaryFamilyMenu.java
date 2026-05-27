package fun.ascent.skyblock.player.bestiary.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.bestiary.BestiaryFamily;
import fun.ascent.skyblock.player.bestiary.BestiaryMilestones;
import fun.ascent.skyblock.player.bestiary.BestiaryMobType;
import fun.ascent.skyblock.player.bestiary.BestiaryProgress;
import fun.ascent.skyblock.player.bestiary.BestiarySection;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

import java.util.List;
import java.util.Map;

public final class BestiaryFamilyMenu {

    private static final Map<Integer, int[]> MOB_SLOTS = Map.of(
            1, new int[]{22},
            2, new int[]{21, 23},
            3, new int[]{20, 22, 24},
            4, new int[]{19, 21, 23, 25},
            5, new int[]{20, 21, 22, 23, 24},
            6, new int[]{21, 22, 23, 30, 31, 32},
            7, new int[]{19, 20, 21, 22, 23, 24, 25}
    );
    private static final int TITLE_SLOT = 4;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    private BestiaryFamilyMenu() {
    }

    public static void open(SkyblockPlayer player, BestiarySection section, BestiaryFamily family) {
        ProfilePlayer profile = player.getActiveProfileData();
        if (profile == null) {
            return;
        }

        BestiaryProgress progress = profile.getBestiaryProgress();
        int kills = family.totalKills(progress);
        int tier = BestiaryMilestones.currentTier(family, kills);

        Inventory inventory = new Inventory(
                InventoryType.CHEST_6_ROW,
                StringUtility.stripColor(section.coloredName()) + " -> " + family.displayName()
        );
        BestiaryMenuFormat.fill(inventory);

        inventory.setItemStack(TITLE_SLOT, BestiaryMenuFormat.icon(
                "<green>" + family.displayName() + (tier > 0 ? " " + StringUtility.getAsRomanNumeral(tier) : ""),
                family.iconMaterial(),
                family.iconTexture(),
                BestiaryMenuFormat.familyLore(progress, family, false)
        ));
        inventory.setItemStack(BACK_SLOT, BestiaryMenuFormat.backButton("Bestiary ➜ " + section.displayName()));
        inventory.setItemStack(CLOSE_SLOT, BestiaryMenuFormat.closeButton());

        List<BestiaryMobType> mobs = family.mobs();
        int[] slots = MOB_SLOTS.getOrDefault(mobs.size(), new int[]{22});
        for (int i = 0; i < mobs.size() && i < slots.length; i++) {
            BestiaryMobType mob = mobs.get(i);
            inventory.setItemStack(slots[i], BestiaryMenuFormat.icon(
                    "<gray>[<white>Lv" + mob.level() + "<gray>] <white>" + mob.displayName(),
                    mob.iconMaterial(),
                    mob.iconTexture(),
                    BestiaryMenuFormat.mobLore(progress, mob)
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
                BestiarySectionMenu.open(clickingPlayer, section);
            }
        });

        player.openInventory(inventory);
    }
}
