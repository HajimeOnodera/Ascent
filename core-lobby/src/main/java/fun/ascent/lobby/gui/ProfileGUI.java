package fun.ascent.lobby.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.common.gui.GUIItem;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import fun.ascent.database.PlayerRepository;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.UUID;

public class ProfileGUI extends InventoryGUI {

    private static final int[] COLOURED_PANE_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16, 17
    };

    public ProfileGUI() {
        super("My Profile", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        Player player = e.player();
        UUID uuid = player.getUuid();
        User user = UserManager.getUser(uuid);
        Rank rank = user.getRank();

        int level = getAscentLevel(uuid);
        int achievementPoints = getAchievementPoints(uuid);
        double progress = getProgressToNextLevel(uuid);
        long xpNeeded = getXpForNextLevel(level) - getXpInCurrentLevel(uuid);

        // Orange pane divider (row 1)
        for (int slot : COLOURED_PANE_SLOTS) {
            set(slot, ItemStackCreator.createNamedItemStack(Material.ORANGE_STAINED_GLASS_PANE));
        }

        // Player Head (slot 2)
        PlayerSkin skin = player.getSkin();
        set(new GUIItem(2) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                if (skin != null) {
                    return ItemStackCreator.getStackHead(
                            rank.getPrefix() + player.getUsername(),
                            skin, 1,
                            "<gray>Ascent Level: <gold>" + level,
                            "<gray>Achievement Points: <yellow>" + StringUtility.commaify(achievementPoints),
                            "<gray>Guild: <aqua>None"
                    );
                } else {
                    return ItemStackCreator.getStack(
                            rank.getPrefix() + player.getUsername(),
                            Material.PLAYER_HEAD, 1,
                            "<gray>Ascent Level: <gold>" + level,
                            "<gray>Achievement Points: <yellow>" + StringUtility.commaify(achievementPoints),
                            "<gray>Guild: <aqua>None"
                    );
                }
            }
        });

        // Friends (slot 3)
        set(new GUIClickableItem(3) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<gold>Friends browser coming soon!"));
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStackHead(
                        "<green>Friends",
                        "e063eedb2184354bd43a19deffba51b53dd6b7222f8388caa239cabcdce84",
                        1,
                        "<gray>View your Ascent friends' profiles,",
                        "<gray>and interact with your online friends!",
                        "",
                        "<yellow>Click to view!"
                );
            }
        });

        // Party (slot 4)
        set(new GUIClickableItem(4) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<gold>Party management coming soon!"));
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStackHead(
                        "<green>Party",
                        "667963ca1ffdc24a10b397ff8161d0da82d6a3f4788d5f67f1a9f9bfbc1eb1",
                        1,
                        "<gray>Create a party and join up with",
                        "<gray>other players to play games",
                        "<gray>together!",
                        "",
                        "<yellow>Click to manage!"
                );
            }
        });

        // Guild (slot 5)
        set(new GUIItem(5) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStackHead(
                        "<green>Guild",
                        "fe8b59f8cce510809427c3843cf575fae8fe6a8b7d1560dd46958d148563815",
                        1,
                        "<gray>Form a guild with other Ascent",
                        "<gray>players to conquer game modes and",
                        "<gray>work towards common Ascent",
                        "<gray>rewards."
                );
            }
        });

        // Recent Players (slot 6)
        set(new GUIItem(6) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStackHead(
                        "<green>Recent Players",
                        "9993a356809532d696841a37a0549b81b159b79a7b2919cff4e5abdfea83d66",
                        1,
                        "<gray>View players you have played recent",
                        "<gray>games with."
                );
            }
        });

        // Go to Housing (slot 20)
        set(new GUIItem(20) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Go to Housing",
                        Material.DARK_OAK_DOOR, 1
                );
            }
        });

        // Social Media (slot 21)
        set(new GUIItem(21) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStackHead(
                        "<green>Social Media",
                        "3685a0be743e9067de95cd8c6d1ba21ab21d37371b3d597211bb75e43279",
                        1,
                        "<gray>Click to edit your Social Media links."
                );
            }
        });

        // Character Information (slot 22)
        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                if (skin != null) {
                    return ItemStackCreator.getStackHead(
                            "<green>Character Information",
                            skin, 1,
                            "<gray>Rank: " + rank.getPrefix().trim(),
                            "<gray>Level: <gold>" + level,
                            "<gray>Experience until next Level: <gold>" + StringUtility.commaify(xpNeeded),
                            "<gray>Achievement Points: <yellow>" + StringUtility.commaify(achievementPoints),
                            "<gray>Mystery Dust: <aqua>0",
                            "<gray>Quests Completed: <gold>0",
                            "<gray>Karma: <light_purple>0",
                            "",
                            "<yellow>Click to see the store link."
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "<green>Character Information",
                            Material.PLAYER_HEAD, 1,
                            "<gray>Rank: " + rank.getPrefix().trim(),
                            "<gray>Level: <gold>" + level,
                            "<gray>Achievement Points: <yellow>" + StringUtility.commaify(achievementPoints)
                    );
                }
            }
        });

        // Stats Viewer (slot 23)
        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Stats Viewer",
                        Material.PAPER, 1,
                        "<gray>Showcases your stats for each",
                        "<gray>game and an overview of all.",
                        "",
                        "<gray>Players ranked <aqua>MVP <gray>or higher",
                        "<gray>can use <white>/stats (username) <gray>to view",
                        "<gray>other players' stats.",
                        "",
                        "<yellow>Click to view your stats!"
                );
            }
        });

        // Coin Boosters (slot 24)
        set(new GUIItem(24) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Coin Boosters",
                        Material.POTION, 1,
                        "<gray>Activate your personal and",
                        "<gray>network boosters for extra",
                        "<gray>coins.",
                        "",
                        "<yellow>Click to activate boosters!"
                );
            }
        });

        // Customize Appearances (slot 29)
        set(new GUIItem(29) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Customize Appearances",
                        Material.LEATHER_CHESTPLATE, 1,
                        "",
                        "<gray>Customize the following visual options",
                        "<gray>for your player!",
                        "<white>- MVP+ Rank Color",
                        "<white>- Punch Messages",
                        "<white>- Glow",
                        "<white>- Status",
                        "",
                        "<yellow>Click to view!"
                );
            }
        });

        // Achievements (slot 30)
        set(new GUIClickableItem(30) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new AchievementMenuGUI().open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Achievements",
                        Material.DIAMOND, 1,
                        "<gray>Track your progress as you unlock",
                        "<gray>Achievements and rack up points.",
                        "",
                        "<gray>Total Points: <yellow>" + StringUtility.commaify(achievementPoints),
                        "",
                        "<yellow>Click to view your achievements!"
                );
            }
        });

        // Ascent Leveling (slot 31)
        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new LevelingGUI().open(player);
            }

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
                        "<dark_aqua>Ascent Level <green>" + level + " <dark_aqua>" + progressBar + " <dark_aqua>" + progressPercent + "%",
                        "",
                        "<gray>Experience until next level: <dark_aqua>" + StringUtility.commaify(xpNeeded),
                        "",
                        "<yellow>Click to see your rewards!"
                );
            }
        });

        // Quests and Challenges (slot 32)
        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<gold>Quests and Challenges coming soon!"));
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Quests and Challenges",
                        Material.ENCHANTED_BOOK, 1,
                        "<gray>Completing quests and challenges",
                        "<gray>will reward you with <gold>Coins<gray>, <dark_aqua>Ascent",
                        "<dark_aqua>Experience <gray>and more!",
                        "",
                        "<gray>You can complete a maximum of <green>15 ",
                        "<gray>challenges every day.",
                        "",
                        "<gray>Challenges completed today: <green>0",
                        "",
                        "<yellow>Click to view Quests and Challenges."
                );
            }
        });

        // Settings and Visibility (slot 33)
        set(new GUIItem(33) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Settings and Visibility",
                        Material.COMPARATOR, 1,
                        "<gray>Allows you to edit and control",
                        "<gray>various personal settings.",
                        "",
                        "<yellow>Click to edit your settings!"
                );
            }
        });

        // Recent Games (slot 39)
        set(new GUIItem(39) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Recent Games",
                        Material.BOOK, 1,
                        "<gray>View your recently played games.",
                        "",
                        "<yellow>Click to view!"
                );
            }
        });

        // Account Status (slot 40)
        set(new GUIItem(40) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Account Status",
                        Material.ANVIL, 1,
                        "<gray>Check your punishment history and",
                        "<gray>see where you stand.",
                        "",
                        "<yellow>Click to view!"
                );
            }
        });

        // Select Language (slot 41)
        set(new GUIItem(41) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStackHead(
                        "<green>Select Language",
                        "98daa1e3ed94ff3e33e1d4c6e43f024c47d78a57ba4d38e75e7c9264106",
                        1,
                        "<gray>Change your language.",
                        "",
                        "<gray>Currently available:",
                        "<gray>   - <white>English",
                        "",
                        "<gray>More languages coming soon!",
                        "",
                        "<yellow>Click to change your language!"
                );
            }
        });

        // Store (slot 49)
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Ascent Store",
                        Material.GOLD_INGOT, 1,
                        "<gray>View the Store from right",
                        "<gray>here in-game!",
                        "",
                        "<yellow>Click to view!"
                );
            }
        });

        // Event Shop (slot 53)
        set(new GUIItem(53) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Event Shop",
                        Material.EMERALD, 1,
                        "<gray>Level up during events by playing",
                        "<gray>games and completing quests.",
                        "",
                        "<gray>Earn <white>Event Silver <gray>when you gain an",
                        "<gray>Event Level. <white>Silver <gray>can be used to",
                        "<gray>purchase event-themed cosmetics!",
                        "",
                        "<yellow>Click to view shop!"
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

    private String createProgressBar(double progress) {
        int filled = (int) (progress * 40);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < 40; i++) {
            bar.append(i < filled ? "|" : "<dark_gray>|");
        }
        return bar.toString();
    }

    private static int getAscentLevel(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "profile.level", null);
        if (val instanceof Number n) return n.intValue();
        return 1;
    }

    private static int getAchievementPoints(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "achievements.points", null);
        if (val instanceof Number n) return n.intValue();
        return 0;
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
