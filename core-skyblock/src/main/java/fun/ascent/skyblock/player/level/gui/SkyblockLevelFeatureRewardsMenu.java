package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.CustomLevelAward;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fun.ascent.common.StringUtility.text;

public class SkyblockLevelFeatureRewardsMenu {

    private static final int[] SLOTS = new int[]{
            19, 20, 21, 22, 23, 24, 25, 31
    };

    private static final int TITLE_SLOT = 4;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "Feature Rewards");
        fillBackground(inv);

        int level = player.getActiveProfileData() != null ? player.getActiveProfileData().level.curLevel : 0;

        inv.setItemStack(TITLE_SLOT, buildTitleItem(level));

        for (Map.Entry<CustomLevelAward, Integer> entry : CustomLevelAward.getAwards().entrySet()) {
            CustomLevelAward award = entry.getKey();
            Integer reqLevel = entry.getValue();
            int slot = SLOTS[award.ordinal()];

            inv.setItemStack(slot, buildAwardItem(award, reqLevel, level));
        }

        inv.setItemStack(BACK_SLOT, buildBackButton());
        inv.setItemStack(CLOSE_SLOT, buildCloseButton());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();

            if (slot == CLOSE_SLOT) {
                player.closeInventory();
            } else if (slot == BACK_SLOT) {
                player.closeInventory();
                SkyblockLevelRewardsMenu.open(player);
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack buildTitleItem(int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Specific game features such as the"));
        lore.add(text("<gray>Bazaar or Community Shop."));
        lore.add(Component.empty());
        lore.add(text("<gray>Next Reward:"));

        Map.Entry<Integer, List<CustomLevelAward>> nextAward = CustomLevelAward.getNextReward(level);
        if (nextAward == null) {
            lore.add(text("<red>No more rewards!"));
        } else {
            nextAward.getValue().forEach(award -> lore.add(text("  <gray>• " + award.getDisplay())));
            lore.add(text("<dark_gray>at Level " + nextAward.getKey()));
        }

        lore.add(Component.empty());
        
        int unlockedCount = CustomLevelAward.getFromLevel(level).size();
        int totalCount = CustomLevelAward.getTotalLevelAwards();
        String unlockedPercentage = String.format("%.2f", (unlockedCount / (double) totalCount) * 100);
        lore.add(text("<gray>Unlocked: <aqua>" + unlockedPercentage + "%"));
        
        String baseLoadingBar = "─────────────────";
        int maxBarLength = baseLoadingBar.length();
        int completedLength = (int) ((unlockedCount / (double) totalCount) * maxBarLength);
        String completedLoadingBar = "<aqua><st>" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength)) + "</st></aqua>";
        String uncompletedLoadingBar = "<gray><st>" + baseLoadingBar.substring(Math.min(completedLength, maxBarLength)) + "</st></gray>";
        lore.add(text(completedLoadingBar + uncompletedLoadingBar + " <yellow>" + unlockedCount + "</yellow><gold>/</gold><yellow>" + totalCount));

        return ItemStack.builder(Material.NETHER_STAR)
                .customName(text("<green>Feature Rewards"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildAwardItem(CustomLevelAward award, int reqLevel, int currentLevel) {
        boolean unlocked = currentLevel >= reqLevel;

        ItemStack.Builder builder = award.getItem();
        List<Component> lore = new ArrayList<>();
        lore.add(text("<dark_gray>Level " + reqLevel));
        lore.add(Component.empty());

        if (unlocked) {
            lore.add(text("<green>You have unlocked this reward!"));
        } else {
            lore.add(text("<gray>Levels left to Unlock: <aqua>" + (reqLevel - currentLevel)));
        }

        return builder
                .customName(text(award.getDisplay()))
                .lore(lore)
                .build();
    }

    private static ItemStack buildBackButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To Leveling Rewards")))
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
