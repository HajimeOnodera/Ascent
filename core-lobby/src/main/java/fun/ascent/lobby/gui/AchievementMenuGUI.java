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

import java.util.UUID;

public class AchievementMenuGUI extends InventoryGUI {

    public AchievementMenuGUI() {
        super("Achievements", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        Player player = e.player();
        UUID uuid = player.getUuid();

        int totalUnlocked = getTotalUnlocked(uuid);
        int totalAchievements = AchievementCategory.totalAchievementCount();
        int totalPoints = getAchievementPoints(uuid);
        int maxPoints = AchievementCategory.totalPointsAll();
        double unlockedPercent = totalAchievements > 0 ? (totalUnlocked * 100.0 / totalAchievements) : 0;
        double pointsPercent = maxPoints > 0 ? (totalPoints * 100.0 / maxPoints) : 0;

        // Category items
        AchievementCategory[] cats = AchievementCategory.values();
        int[] slots = {1, 2, 3, 4, 5, 6, 7, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        for (int i = 0; i < cats.length && i < slots.length; i++) {
            AchievementCategory cat = cats[i];
            int slot = slots[i];

            int catUnlocked = getCategoryUnlocked(uuid, cat);
            int catTotal = cat.getAchievements().length;
            int catPoints = getCategoryPoints(uuid, cat);
            int catMaxPoints = cat.totalPoints();
            double catUPercent = catTotal > 0 ? (catUnlocked * 100.0 / catTotal) : 0;
            double catPPercent = catMaxPoints > 0 ? (catPoints * 100.0 / catMaxPoints) : 0;

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    new AchievementDetailGUI(cat).open(player);
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack(
                            "<green>" + cat.getDisplayName() + " Achievements",
                            cat.getIcon(), 1,
                            "<gray>Unlocked: <aqua>" + catUnlocked + "<gray>/<aqua>" + catTotal + " <dark_gray>(" + (int) catUPercent + "%)",
                            "<gray>Points: <yellow>" + catPoints + "<gray>/<yellow>" + catMaxPoints + " <dark_gray>(" + (int) catPPercent + "%)",
                            "",
                            "<yellow>Click to view achievements!"
                    );
                }
            });
        }

        // Gold Achievement Menu (slot 45)
        set(new GUIItem(45) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                boolean unlocked = totalPoints >= 10000;
                return ItemStackCreator.getStack(
                        unlocked ? "<gold>Gold Achievement Menu" : "<red>Gold Achievement Menu",
                        Material.CLOCK, 1,
                        "<gray>Changes achievement unlocks within",
                        "<gray>the menu to gold.",
                        "",
                        unlocked ? "<green>Unlocked!" : "<red>Unlocked with <aqua>10,000 <red>Achievement Points"
                );
            }
        });

        // Go Back (slot 48)
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new ProfileGUI().open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack("<green>Go Back", Material.ARROW, 1, "<gray>To My Profile");
            }
        });

        // Overall Completion (slot 49)
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Ascent Achievements Completion",
                        Material.DIAMOND, 1,
                        "<gray>Player: " + player.getUsername(),
                        "<gray>Unlocked: <aqua>" + totalUnlocked + "<gray>/<aqua>" + totalAchievements + " <dark_gray>(" + (int) unlockedPercent + "%)",
                        "<gray>Points: <yellow>" + totalPoints + "<gray>/<yellow>" + maxPoints + " <dark_gray>(" + (int) pointsPercent + "%)",
                        "",
                        "<gray>Legacy Unlocked: <aqua>0",
                        "<gray>Legacy Points: <yellow>0"
                );
            }
        });

        // Achievement Tracking (slot 50)
        set(new GUIItem(50) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Achievements Tracking",
                        Material.REPEATER, 1,
                        "<gray>Track achievements to access them",
                        "<gray>quickly in this menu and get instant",
                        "<gray>feedback of your progress."
                );
            }
        });

        // Achievement Rewards (slot 51)
        set(new GUIClickableItem(51) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(net.kyori.adventure.text.Component.text("Achievement Rewards coming soon!"));
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<gold>Achievement Rewards",
                        Material.GOLD_INGOT, 1,
                        "<gray>Unlock exclusive rewards for",
                        "<gray>achievement hunting efforts."
                );
            }
        });

        // Search (slot 53)
        set(new GUIItem(53) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Search",
                        Material.OAK_SIGN, 1,
                        "<gray>Search for an achievement by name,",
                        "<gray>description or points value."
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

    private static int getAchievementPoints(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "achievements.points", null);
        if (val instanceof Number n) return n.intValue();
        return 0;
    }

    private static int getTotalUnlocked(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "achievements.completed", null);
        if (val instanceof Number n) return n.intValue();
        return 0;
    }

    private static int getCategoryUnlocked(UUID uuid, AchievementCategory cat) {
        Object val = PlayerRepository.getField(uuid,
                "achievements.categories." + cat.name().toLowerCase() + ".completed", null);
        if (val instanceof Number n) return n.intValue();
        return 0;
    }

    private static int getCategoryPoints(UUID uuid, AchievementCategory cat) {
        Object val = PlayerRepository.getField(uuid,
                "achievements.categories." + cat.name().toLowerCase() + ".points", null);
        if (val instanceof Number n) return n.intValue();
        return 0;
    }
}
