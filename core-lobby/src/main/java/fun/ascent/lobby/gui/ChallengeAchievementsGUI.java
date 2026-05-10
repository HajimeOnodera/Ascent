package fun.ascent.lobby.gui;

import fun.ascent.common.gui.GUIItem;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.database.PlayerRepository;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.UUID;

public class ChallengeAchievementsGUI extends InventoryGUI {

    private static final int[] ACHIEVEMENT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
    };

    private final AchievementCategory category;

    public ChallengeAchievementsGUI(AchievementCategory category) {
        super(category.getDisplayName() + " Challenge", InventoryType.CHEST_6_ROW);
        this.category = category;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        Player player = e.player();
        UUID uuid = player.getUuid();

        List<String> completedNames = getCompletedAchievementNames(uuid);

        // Individual achievements
        AchievementCategory.AchievementDef[] achievements = category.getAchievements();
        for (int i = 0; i < achievements.length && i < ACHIEVEMENT_SLOTS.length; i++) {
            AchievementCategory.AchievementDef ach = achievements[i];
            int slot = ACHIEVEMENT_SLOTS[i];
            boolean completed = completedNames.contains(ach.name().toLowerCase().replace(" ", "_"));

            if (completed) {
                set(slot, ItemStackCreator.enchant(ItemStackCreator.getStack(
                        "<green>" + ach.name(),
                        Material.DIAMOND, 1,
                        "<dark_gray>" + category.getDisplayName(),
                        "",
                        "<gray>" + ach.description(),
                        "",
                        "<gray>Points: <yellow>" + ach.points(),
                        "",
                        "<green>Unlocked!"
                )));
            } else {
                set(slot, ItemStackCreator.getStack(
                        "<red>" + ach.name(),
                        Material.COAL, 1,
                        "<dark_gray>" + category.getDisplayName(),
                        "",
                        "<gray>" + ach.description(),
                        "",
                        "<gray>Points: <yellow>" + ach.points(),
                        "",
                        "<red>Not Unlocked"
                ));
            }
        }

        // Go Back (slot 48)
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new AchievementDetailGUI(category).open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack("<green>Go Back", Material.ARROW, 1, "<gray>To " + category.getDisplayName());
            }
        });

        // Summary (slot 49)
        int catUnlocked = getCategoryCompleted(uuid);
        int catTotal = achievements.length;
        int catPoints = getCategoryPoints(uuid);
        int catMaxPoints = category.totalPoints();
        double uPercent = catTotal > 0 ? (catUnlocked * 100.0 / catTotal) : 0;
        double pPercent = catMaxPoints > 0 ? (catPoints * 100.0 / catMaxPoints) : 0;

        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Challenge Achievement Summary",
                        category.getIcon(), 1,
                        "<dark_gray>" + category.getDisplayName(),
                        "<gray>Unlocked: <aqua>" + catUnlocked + "<gray>/<aqua>" + catTotal + " <dark_gray>(" + (int) uPercent + "%)",
                        "<gray>Points: <yellow>" + catPoints + "<gray>/<yellow>" + catMaxPoints + " <dark_gray>(" + (int) pPercent + "%)"
                );
            }
        });

        updateItemStacks(getInventory(), player);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @SuppressWarnings("unchecked")
    private List<String> getCompletedAchievementNames(UUID uuid) {
        Object val = PlayerRepository.getField(uuid,
                "achievements.categories." + category.name().toLowerCase() + ".list", null);
        if (val instanceof List<?> list) {
            return (List<String>) list;
        }
        return List.of();
    }

    private int getCategoryCompleted(UUID uuid) {
        Object val = PlayerRepository.getField(uuid,
                "achievements.categories." + category.name().toLowerCase() + ".completed", null);
        if (val instanceof Number n) return n.intValue();
        return 0;
    }

    private int getCategoryPoints(UUID uuid) {
        Object val = PlayerRepository.getField(uuid,
                "achievements.categories." + category.name().toLowerCase() + ".points", null);
        if (val instanceof Number n) return n.intValue();
        return 0;
    }
}
