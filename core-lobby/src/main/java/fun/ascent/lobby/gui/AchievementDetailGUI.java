package fun.ascent.lobby.gui;

import fun.ascent.common.achievement.AchievementCategory;
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

import java.util.UUID;

public class AchievementDetailGUI extends InventoryGUI {

    private final AchievementCategory category;

    public AchievementDetailGUI(AchievementCategory category) {
        super(category.getDisplayName() + " Achievements", InventoryType.CHEST_4_ROW);
        this.category = category;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        Player player = e.player();
        UUID uuid = player.getUuid();

        int totalUnlocked = getCategoryCompleted(uuid);
        int totalCount = category.getAchievements().length;
        int totalPoints = getCategoryPoints(uuid);
        int maxPoints = category.totalPoints();
        double unlockedPercent = totalCount > 0 ? (totalUnlocked * 100.0 / totalCount) : 0;
        double pointsPercent = maxPoints > 0 ? (totalPoints * 100.0 / maxPoints) : 0;

        // Challenge Achievements (slot 11)
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new ChallengeAchievementsGUI(category).open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Challenge Achievements",
                        Material.DIAMOND, 1,
                        "<dark_gray>" + category.getDisplayName(),
                        "<gray>Unlocked: <aqua>" + totalUnlocked + "<gray>/<aqua>" + totalCount + " <dark_gray>(" + (int) unlockedPercent + "%)",
                        "<gray>Points: <yellow>" + totalPoints + "<gray>/<yellow>" + maxPoints + " <dark_gray>(" + (int) pointsPercent + "%)",
                        "",
                        "<gray>Challenge achievements may be",
                        "<gray>completed a single time.",
                        "",
                        "<yellow>Click to view achievements!"
                );
            }
        });

        // Tiered Achievements (slot 15)
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Tiered Achievements",
                        Material.DIAMOND_BLOCK, 1,
                        "<dark_gray>" + category.getDisplayName(),
                        "<gray>Unlocked: <aqua>0<gray>/<aqua>0 <dark_gray>(0%)",
                        "<gray>Points: <yellow>0<gray>/<yellow>0 <dark_gray>(0%)",
                        "",
                        "<gray>Tiered achievements are completed",
                        "<gray>over multiple tiers.",
                        "",
                        "<yellow>Click to view achievements!"
                );
            }
        });

        // Go Back (slot 30)
        set(new GUIClickableItem(30) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new AchievementMenuGUI().open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack("<green>Go Back", Material.ARROW, 1, "<gray>To Achievements");
            }
        });

        // Total Completion (slot 31)
        set(new GUIItem(31) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Total Completion",
                        category.getIcon(), 1,
                        "<dark_gray>" + category.getDisplayName(),
                        "<gray>Unlocked: <aqua>" + totalUnlocked + "<gray>/<aqua>" + totalCount + " <dark_gray>(" + (int) unlockedPercent + "%)",
                        "<gray>Points: <yellow>" + totalPoints + "<gray>/<yellow>" + maxPoints + " <dark_gray>(" + (int) pointsPercent + "%)"
                );
            }
        });

        // Seasonal Challenge Achievements (slot 32)
        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Seasonal Challenge Achievements",
                        Material.MAGMA_CREAM, 1,
                        "<gray>View challenge achievements for " + category.getDisplayName(),
                        "<gray>that are exclusive to seasonal",
                        "<gray>events.",
                        "",
                        "<dark_gray>These achievements do not count",
                        "<dark_gray>towards total game completion.",
                        "",
                        "<yellow>Click to view achievements!"
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
