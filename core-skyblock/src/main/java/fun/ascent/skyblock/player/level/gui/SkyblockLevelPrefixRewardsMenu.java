package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyBlockLevelRequirement;
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

public class SkyblockLevelPrefixRewardsMenu {
    private static final int[] SLOTS = new int[]{
            19, 20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33
    };

    private static final int TITLE_SLOT = 4;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "Prefix Color Rewards");
        fillBackground(inv);

        int level = player.getActiveProfileData() != null ? player.getActiveProfileData().level.curLevel : 0;
        SkyBlockLevelRequirement req = SkyBlockLevelRequirement.getLevel(level);

        // Title item
        inv.setItemStack(TITLE_SLOT, buildTitleItem(req, level));

        // Prefix items
        int index = 0;
        for (Map.Entry<SkyBlockLevelRequirement, String> entry : SkyBlockLevelRequirement.getAllPrefixChanges().entrySet()) {
            if (index >= SLOTS.length) break;
            SkyBlockLevelRequirement levelReq = entry.getKey();
            int slot = SLOTS[index];

            inv.setItemStack(slot, buildPrefixItem(player, levelReq, level));
            index++;
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

    private static ItemStack buildTitleItem(SkyBlockLevelRequirement req, int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>New colors for your level prefix"));
        lore.add(text("<gray>shown in TAB and in chat!"));
        lore.add(Component.empty());
        lore.add(text("<gray>Next Reward:"));

        if (req != null) {
            Map.Entry<SkyBlockLevelRequirement, String> nextPrefix = req.getNextPrefixChange();
            if (nextPrefix == null) {
                lore.add(text("<red>No more rewards!"));
            } else {
                lore.add(text("  " + nextPrefix.getValue() + "Level Prefix <gray>at Level " + nextPrefix.getKey().asInt()));
            }
            lore.add(Component.empty());

            int unlockedCount = req.getPreviousPrefixChanges().size();
            int totalCount = SkyBlockLevelRequirement.getAllPrefixChanges().size();
            String unlockedPercentage = String.format("%.2f", (unlockedCount / (double) totalCount) * 100);
            lore.add(text("<gray>Unlocked: <aqua>" + unlockedPercentage + "%"));
            
            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) ((unlockedCount / (double) totalCount) * maxBarLength);
            String completedLoadingBar = "<aqua><st>" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength)) + "</st></aqua>";
            String uncompletedLoadingBar = "<gray><st>" + baseLoadingBar.substring(Math.min(completedLength, maxBarLength)) + "</st></gray>";
            lore.add(text(completedLoadingBar + uncompletedLoadingBar + " <yellow>" + unlockedCount + "</yellow><gold>/</gold><yellow>" + totalCount));
        }

        return ItemStack.builder(Material.GRAY_DYE)
                .customName(text("<green>Prefix Color Rewards"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildPrefixItem(SkyblockPlayer player, SkyBlockLevelRequirement levelReq, int currentLevel) {
        boolean unlocked = currentLevel >= levelReq.asInt();
        String display = levelReq.getPrefix() + levelReq.getPrefixDisplay();

        List<Component> lore = new ArrayList<>();
        lore.add(text("<dark_gray>Level " + levelReq.asInt()));
        lore.add(Component.empty());
        lore.add(text("<gray>Preview: <white>[" + levelReq.getPrefix() + levelReq.asInt() + "<white>] " + player.getName()));
        lore.add(Component.empty());

        if (unlocked) {
            lore.add(text("<green>You have unlocked this reward!"));
        } else {
            lore.add(text("<gray>Levels left to unlock: <aqua>" + (levelReq.asInt() - currentLevel)));
        }

        Material mat = levelReq.getPrefixItem();
        if (mat == null || mat == Material.AIR) {
            mat = Material.BONE_MEAL;
        }

        return ItemStack.builder(mat)
                .customName(text(display))
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
