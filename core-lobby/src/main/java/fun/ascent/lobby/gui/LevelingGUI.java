package fun.ascent.lobby.gui;

import fun.ascent.common.StringUtility;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LevelingGUI extends InventoryGUI {

    private static final int LEVELS_PER_PAGE = 25;
    private static final int[] REWARD_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            19, 20, 21, 22, 23, 24, 25
    };

    private static final double[] MULTIPLIERS = {1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
    private static final int[] MULTIPLIER_LEVELS = {10, 25, 50, 75, 100, 125, 150, 250};

    private int page = 0;

    public LevelingGUI() {
        super("Ascent Leveling", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        Player player = e.player();
        UUID uuid = player.getUuid();

        int currentLevel = getAscentLevel(uuid);
        double progress = getProgressToNextLevel(uuid);
        long xpNeeded = getXpForNextLevel(currentLevel) - getXpInCurrentLevel(uuid);

        // ── Populate level rewards ───────────────────────────────────────
        populateLevelRewards(currentLevel);

        // ── Previous Page (slot 18) ──────────────────────────────────────
        if (page > 0) {
            set(new GUIClickableItem(18) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    page--;
                    open(player);
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Page " + page, Material.ARROW, 1);
                }
            });
        }

        // ── Next Page (slot 26) ──────────────────────────────────────────
        if (page < getMaxPages() - 1) {
            set(new GUIClickableItem(26) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    page++;
                    open(player);
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Page " + (page + 2), Material.ARROW, 1);
                }
            });
        }

        // ── Coin Multipliers (slots 36-43) ───────────────────────────────
        for (int i = 0; i < MULTIPLIERS.length; i++) {
            int slot = 36 + i;
            double mult = MULTIPLIERS[i];
            int reqLevel = MULTIPLIER_LEVELS[i];
            boolean unlocked = currentLevel >= reqLevel;

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack(
                            "<gold>" + mult + "x <green>Coin Multiplier",
                            Material.GOLD_BLOCK, 1,
                            "",
                            "<gray>Increases the amount of coins you",
                            "<gray>earn when playing games.",
                            "",
                            "<dark_gray><italic>Automatically unlocks upon reaching",
                            "<dark_gray><italic>the required level.",
                            "",
                            unlocked ? "<green>Unlocked!" : "<red>Requires Level " + reqLevel
                    );
                }
            });
        }

        // ── Veteran Rewards (slot 44) ────────────────────────────────────
        set(new GUIClickableItem(44) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                if (currentLevel >= 100) {
                    player.sendMessage(StringUtility.text("<gold>Veteran Rewards coming soon!"));
                } else {
                    player.sendMessage(StringUtility.text("<red>You must be Ascent Level 100 or higher!"));
                }
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                boolean canAccess = currentLevel >= 100;
                return ItemStackCreator.getStack(
                        "<gold>Veteran Rewards",
                        Material.BEACON, 1,
                        "<gray>Rewards for the most dedicated",
                        "<gray>players!",
                        "",
                        canAccess ? "<yellow>Click to view!" : "<red>You must be Ascent Level 100 or",
                        canAccess ? "" : "<red>higher to access this menu!"
                );
            }
        });

        // ── Go Back (slot 48) ────────────────────────────────────────────
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

        // ── Ascent Leveling Info (slot 49) ──────────────────────────────
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                String progressBar = createProgressBar(progress);
                int progressPercent = (int) (progress * 100);

                return ItemStackCreator.getStack(
                        "<green>Ascent Leveling",
                        Material.BREWING_STAND, 1,
                        "<gray>Playing games and completing quests",
                        "<gray>will reward you with <dark_aqua>Ascent",
                        "<dark_aqua>Experience<gray>, which is required to",
                        "<gray>level up and acquire new perks and",
                        "<gray>rewards!",
                        "",
                        "<dark_aqua>Ascent Level <green>" + currentLevel + " <dark_aqua>" + progressBar + " <dark_aqua>" + progressPercent + "%",
                        "",
                        "<gray>Experience until next level: <dark_aqua>" + StringUtility.commaify(xpNeeded)
                );
            }
        });

        // ── Quest Log (slot 50) ──────────────────────────────────────────
        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<gold>Quest Log coming soon!"));
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Quest Log",
                        Material.ENCHANTED_BOOK, 1,
                        "<gray>Completing quests will reward you",
                        "<gray>with <gold>Coins<gray>, <dark_aqua>Ascent Experience <gray>and",
                        "<gray>more!",
                        "",
                        "<gray>Talk to <aqua>Quest Masters <gray>located in",
                        "<gray>game lobbies to accept quests.",
                        "",
                        "<yellow>Click to view quest progress!"
                );
            }
        });

        updateItemStacks(getInventory(), player);
    }

    // ── Level reward population ──────────────────────────────────────────

    private void populateLevelRewards(int playerLevel) {
        int startLevel = page * LEVELS_PER_PAGE + 1;

        for (int i = 0; i < REWARD_SLOTS.length; i++) {
            int level = startLevel + i;
            int slot = REWARD_SLOTS[i];
            set(createLevelRewardItem(slot, level, playerLevel));
        }
    }

    private GUIItem createLevelRewardItem(int slot, int level, int playerLevel) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                boolean claimed = playerLevel >= level;
                String nameColor = claimed ? "<red>" : "<green>";

                List<String> lore = new ArrayList<>();
                lore.add("");

                // Default reward: coins
                long coins = 40000L + (level * 1000L);
                lore.add("<dark_gray>+<gold>" + StringUtility.commaify(coins) + " <gray>Arcade Coins");
                lore.add("");
                lore.add("<dark_gray><italic>Arcade Coins can be exchanged for");
                lore.add("<dark_gray><italic>other game coins inside the Arcade");
                lore.add("<dark_gray><italic>Lobby.");

                // Special rewards at certain levels
                if (level == 10) {
                    lore.add("");
                    lore.add("<dark_gray>+<gray>1.5x Coin Multiplier");
                } else if (level == 25) {
                    lore.add("");
                    lore.add("<dark_gray>+<gray>2.0x Coin Multiplier");
                } else if (level == 50) {
                    lore.add("");
                    lore.add("<dark_gray>+<gray>2.5x Coin Multiplier");
                } else if (level == 100) {
                    lore.add("");
                    lore.add("<dark_gray>+<gray>Veteran Status");
                }

                lore.add("");
                if (claimed) {
                    lore.add("<green>You have already claimed this reward!");
                } else {
                    lore.add("<yellow>Reach Level " + level + " to claim!");
                }

                return ItemStackCreator.getStack(
                        nameColor + "Ascent Level Reward " + level,
                        claimed ? Material.MINECART : Material.CHEST_MINECART,
                        1,
                        lore.toArray(new String[0])
                );
            }
        };
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private String createProgressBar(double progress) {
        int filled = (int) (progress * 40);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < 40; i++) {
            bar.append(i < filled ? "|" : "<dark_gray>|");
        }
        return bar.toString();
    }

    private int getMaxPages() {
        return 10; // 250 levels / 25 per page
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    // ── Data helpers ─────────────────────────────────────────────────────

    private static int getAscentLevel(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "profile.level", null);
        if (val instanceof Number n) return n.intValue();
        return 1;
    }

    private static double getProgressToNextLevel(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "profile.levelProgress", null);
        if (val instanceof Number n) return n.doubleValue();
        return 0.0;
    }

    private static long getXpForNextLevel(int level) {
        return 10000L + (level * 2500L);
    }

    private static long getXpInCurrentLevel(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "profile.currentLevelXp", null);
        if (val instanceof Number n) return n.longValue();
        return 0;
    }
}
