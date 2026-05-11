package fun.ascent.lobby.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.common.gui.GUIItem;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import fun.ascent.database.FriendLookup;
import fun.ascent.database.PlayerRepository;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.*;
import java.util.stream.Collectors;

public class FriendsGUI extends InventoryGUI {

    private static final int[] COLOURED_PANE_SLOTS = {9, 10, 11, 12, 13, 14, 15, 16, 17};
    private static final int FRIENDS_PER_PAGE = 18;
    private static final int[] FRIEND_SLOTS = {
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44
    };

    private final int currentPage;
    private final SortType sortType;
    private final boolean sortReversed;
    private final String searchFilter;

    public FriendsGUI() {
        this(1, SortType.DEFAULT, false, null);
    }

    public FriendsGUI(int page, SortType sortType, boolean sortReversed, String searchFilter) {
        super("Friends", InventoryType.CHEST_6_ROW);
        this.currentPage = page;
        this.sortType = sortType;
        this.sortReversed = sortReversed;
        this.searchFilter = searchFilter;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        Player player = e.player();
        UUID uuid = player.getUuid();
        User user = UserManager.getUser(uuid);
        Rank rank = user.getRank();
        int level = getPlayerLevel(uuid);
        int achievementPoints = getPlayerAchievementPoints(uuid);

        // Magenta pane divider (row 1)
        for (int slot : COLOURED_PANE_SLOTS) {
            set(slot, ItemStackCreator.createNamedItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }

        // Fetch friend data
        List<FriendLookup.FriendEntry> allFriendsRaw = FriendLookup.getFriendEntries(uuid);

        // Resolve online players
        Set<UUID> onlineUuids = MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream().map(Player::getUuid).collect(Collectors.toSet());

        // Build display entries
        List<FriendDisplayEntry> allFriends = allFriendsRaw.stream()
                .map(f -> {
                    String name = getPlayerName(f.uuid());
                    boolean online = onlineUuids.contains(f.uuid());
                    return new FriendDisplayEntry(f.uuid(), name, f.nickname(),
                            f.bestFriend(), online, f.addedTimestamp());
                })
                .collect(Collectors.toList());

        // Apply search filter
        List<FriendDisplayEntry> filteredFriends;
        if (searchFilter != null && !searchFilter.isEmpty()) {
            String lower = searchFilter.toLowerCase();
            filteredFriends = allFriends.stream()
                    .filter(f -> f.name.toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        } else {
            filteredFriends = allFriends;
        }

        // Sort
        Comparator<FriendDisplayEntry> comparator = switch (sortType) {
            case DEFAULT -> Comparator.comparing((FriendDisplayEntry fe) -> !fe.online)
                    .thenComparing(fe -> fe.name.toLowerCase());
            case ALPHABETICAL -> Comparator.comparing(fe -> fe.name.toLowerCase());
            case LAST_ONLINE -> Comparator.comparing((FriendDisplayEntry fe) -> fe.addedTimestamp).reversed();
        };
        if (sortReversed) comparator = comparator.reversed();
        filteredFriends.sort(comparator);

        // Pagination
        int totalPages = Math.max(1, (int) Math.ceil((double) filteredFriends.size() / FRIENDS_PER_PAGE));
        int page = Math.min(currentPage, totalPages);
        int startIndex = (page - 1) * FRIENDS_PER_PAGE;
        int endIndex = Math.min(startIndex + FRIENDS_PER_PAGE, filteredFriends.size());
        List<FriendDisplayEntry> pageEntries = filteredFriends.subList(startIndex, endIndex);

        // ── Player Head (slot 2) ─────────────────────────────────────────
        PlayerSkin skin = player.getSkin();
        set(new GUIClickableItem(2) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new ProfileGUI().open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                if (skin != null) {
                    return ItemStackCreator.getStackHead(
                            rank.getPrefix() + player.getUsername(),
                            skin, 1,
                            "<gray>Ascent Level: <gold>" + level,
                            "<gray>Achievement Points: <yellow>" + StringUtility.commaify(achievementPoints),
                            "<gray>Guild: <aqua>None",
                            "",
                            "<yellow>Click to go back!"
                    );
                } else {
                    return ItemStackCreator.getStack(
                            rank.getPrefix() + player.getUsername(),
                            Material.PLAYER_HEAD, 1,
                            "<gray>Ascent Level: <gold>" + level,
                            "<yellow>Click to go back!"
                    );
                }
            }
        });

        // ── Friends Tab (slot 3) - current tab ───────────────────────────
        set(new GUIItem(3) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.enchant(ItemStackCreator.getStackHead(
                        "<green>Friends",
                        "e063eedb2184354bd43a19deffba51b53dd6b7222f8388caa239cabcdce84",
                        1,
                        "<gray>View your Ascent friends' profiles,",
                        "<gray>and interact with your online friends!",
                        "",
                        "<yellow>Currently viewing!"
                ));
            }
        });

        // ── Party Tab (slot 4) ─────────────────────────────────────────────
        set(new GUIClickableItem(4) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new PartyGUI().open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStackHead(
                        "<green>Party",
                        "667963ca1ffdc24a10b397ff8161d0da82d6a3f4788d5f67f1a9f9bfbc1eb1",
                        1,
                        "<gray>Create a party and join up with",
                        "<gray>other players to play games",
                        "<gray>together!",
                        "",
                        "<yellow>Click to view!"
                );
            }
        });

        // ── Guild Tab (slot 5) ───────────────────────────────────────────
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

        // ── Recent Players Tab (slot 6) ──────────────────────────────────
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

        // ── Add Friend (slot 18) ─────────────────────────────────────────
        set(new GUIClickableItem(18) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<green>Use <white>/friend add <name> <green>to add a friend!"));
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Add Friend",
                        Material.WRITABLE_BOOK, 1,
                        "<gray>Click to add a friend to your friend",
                        "<gray>list.",
                        "",
                        "<gray>Friends can see what each other",
                        "<gray>are doing on the network, and can",
                        "<gray>see which each other are online.",
                        "",
                        "<yellow>Click to add a friend!"
                );
            }
        });

        // ── Remove Friend (slot 19) ──────────────────────────────────────
        set(new GUIClickableItem(19) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<green>Use <white>/friend remove <name> <green>to remove a friend!"));
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<red>Remove Friend",
                        Material.BARRIER, 1,
                        "<gray>Click to remove a current friend",
                        "<gray>from your friend list.",
                        "",
                        "<yellow>Click to remove a friend!"
                );
            }
        });

        // ── Change Sort (slot 25) ────────────────────────────────────────
        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                // Cycle through sort types
                SortType nextSort = switch (sortType) {
                    case DEFAULT -> SortType.ALPHABETICAL;
                    case ALPHABETICAL -> SortType.LAST_ONLINE;
                    case LAST_ONLINE -> SortType.DEFAULT;
                };
                new FriendsGUI(1, nextSort, sortReversed, searchFilter).open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                String currentSortName = switch (sortType) {
                    case DEFAULT -> "Default";
                    case ALPHABETICAL -> "Alphabetical";
                    case LAST_ONLINE -> "Last Online";
                };
                String orderText = sortReversed ? "Reversed" : "Normal";

                return ItemStackCreator.getStack(
                        "<green>Change sort",
                        Material.HOPPER, 1,
                        "<gray>Current sort: <aqua>" + currentSortName,
                        "<gray>Sorting order: <aqua>" + orderText,
                        "",
                        "<aqua>Default<gray>: Alphabetical order, but",
                        "<gray>show online players first",
                        "<aqua>Alphabetical<gray>: Show everyone",
                        "<gray>listed from A-Z",
                        "<aqua>Last Online<gray>: Sorts by who was",
                        "<gray>most recently online",
                        "",
                        "<yellow>LEFT CLICK <gray>to change between",
                        "<gray>all the available sorting options.",
                        "",
                        "<yellow>RIGHT CLICK <gray>to reverse the",
                        "<gray>current order!"
                );
            }
        });

        // ── Search Players (slot 26) ─────────────────────────────────────
        int matchingCount = filteredFriends.size();
        set(new GUIClickableItem(26) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                if (searchFilter != null && !searchFilter.isEmpty()) {
                    // Clear filter
                    new FriendsGUI(1, sortType, sortReversed, null).open(player);
                } else {
                    player.sendMessage(StringUtility.text("<green>Use <white>/friend list <green>to search friends!"));
                }
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                if (searchFilter != null && !searchFilter.isEmpty()) {
                    return ItemStackCreator.enchant(ItemStackCreator.getStack(
                            "<green>Search: <yellow>" + searchFilter,
                            Material.OAK_SIGN, 1,
                            "<gray>Currently filtering by: <white>" + searchFilter,
                            "<gray>Showing <yellow>" + matchingCount + " <gray>matching friends",
                            "",
                            "<yellow>Click to clear the search filter."
                    ));
                } else {
                    return ItemStackCreator.getStack(
                            "<green>Search Players",
                            Material.OAK_SIGN, 1,
                            "<gray>Search for a player by name",
                            "<gray>in your friends list.",
                            "",
                            "<yellow>Click to search!"
                    );
                }
            }
        });

        // ── Friend heads (slots 27-44) ───────────────────────────────────
        for (int i = 0; i < FRIEND_SLOTS.length; i++) {
            int slot = FRIEND_SLOTS[i];
            if (i < pageEntries.size()) {
                FriendDisplayEntry entry = pageEntries.get(i);
                set(createFriendItem(slot, entry));
            }
        }

        // ── Previous Page (slot 45) ──────────────────────────────────────
        if (page > 1) {
            set(new GUIClickableItem(45) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    new FriendsGUI(currentPage - 1, sortType, sortReversed, searchFilter).open(player);
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack(
                            "<green>Previous Page",
                            Material.ARROW, 1,
                            "<gray>Page " + (currentPage - 1) + "/" + totalPages
                    );
                }
            });
        }

        // ── Page Info (slot 49) ──────────────────────────────────────────
        int totalFriendCount = allFriends.size();
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                if (searchFilter != null && !searchFilter.isEmpty()) {
                    return ItemStackCreator.getStack(
                            "<green>Page " + page + "/" + totalPages,
                            Material.BOOK, 1,
                            "<gray>Showing <yellow>" + matchingCount + " <gray>of <yellow>" + totalFriendCount + " <gray>friends",
                            "<gray>Searching for: <white>" + searchFilter
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "<green>Page " + page + "/" + totalPages,
                            Material.BOOK, 1,
                            "<gray>Total friends: <yellow>" + totalFriendCount
                    );
                }
            }
        });

        // ── Next Page (slot 53) ──────────────────────────────────────────
        if (page < totalPages) {
            set(new GUIClickableItem(53) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    new FriendsGUI(currentPage + 1, sortType, sortReversed, searchFilter).open(player);
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack(
                            "<green>Next Page",
                            Material.ARROW, 1,
                            "<gray>Page " + (currentPage + 1) + "/" + totalPages
                    );
                }
            });
        }

        updateItemStacks(getInventory(), player);
    }

    // ── Friend head item builder ─────────────────────────────────────────

    private GUIClickableItem createFriendItem(int slot, FriendDisplayEntry entry) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text(
                        "<yellow>Viewing profile of <white>" + entry.name + "<yellow>..."
                ));
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                List<String> lore = new ArrayList<>();

                // Level and achievement points
                int friendLevel = getPlayerLevel(entry.uuid);
                int friendPoints = getPlayerAchievementPoints(entry.uuid);
                lore.add("<gray>Ascent Level: <gold>" + friendLevel);
                lore.add("<gray>Achievement Points: <yellow>" + StringUtility.commaify(friendPoints));
                lore.add("<gray>Guild: <aqua>None");
                lore.add("");

                // Online status
                if (entry.online) {
                    lore.add("<green>Online");
                } else {
                    String lastSeenText = formatLastSeen(entry.addedTimestamp);
                    lore.add("<gray>Last Online: <aqua>" + lastSeenText);
                }

                // Best friend badge
                if (entry.bestFriend) {
                    lore.add("");
                    lore.add("<gold>Best Friend");
                }

                lore.add("");
                lore.add("<yellow>Left-click to view profile");
                lore.add("<yellow>Shift-click to remove friend");

                // Name coloring: green if online, gray if offline, gold prefix for best friend
                String nameColor = entry.online ? "<green>" : "<gray>";
                String displayName = entry.bestFriend
                        ? "<gold>" + nameColor + entry.name
                        : nameColor + entry.name;

                // Try to get player's skin from the rank prefix
                String rankPrefix = getPlayerRankPrefix(entry.uuid);
                if (rankPrefix != null && !rankPrefix.isEmpty()) {
                    displayName = rankPrefix + entry.name;
                }

                // Use online player's skin if available, otherwise default head
                Player onlinePlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(entry.uuid);
                if (onlinePlayer != null && onlinePlayer.getSkin() != null) {
                    return ItemStackCreator.getStackHead(displayName, onlinePlayer.getSkin(), 1,
                            lore.toArray(new String[0]));
                } else {
                    // Default Steve texture
                    return ItemStackCreator.getStackHead(displayName,
                            "8667ba71b85a4004af54457a9734eed7e09dcc6abe4dd49f4c11d4c8e3c91cfe",
                            1, lore.toArray(new String[0]));
                }
            }
        };
    }

    // ── Time formatter ───────────────────────────────────────────────────

    private static String formatLastSeen(long timestamp) {
        if (timestamp <= 0) return "Unknown";

        long secondsAgo = (System.currentTimeMillis() - timestamp) / 1000;
        if (secondsAgo < 60) return secondsAgo + " seconds ago";

        long minutesAgo = secondsAgo / 60;
        if (minutesAgo < 60) return minutesAgo + " minute" + (minutesAgo != 1 ? "s" : "") + " ago";

        long hoursAgo = minutesAgo / 60;
        if (hoursAgo < 24) {
            long remMin = minutesAgo % 60;
            return hoursAgo + " hour" + (hoursAgo != 1 ? "s" : "") + ", " + remMin + " minute" + (remMin != 1 ? "s" : "") + " ago";
        }

        long daysAgo = hoursAgo / 24;
        if (daysAgo < 30) {
            long remHours = hoursAgo % 24;
            return daysAgo + " day" + (daysAgo != 1 ? "s" : "") + ", " + remHours + " hour" + (remHours != 1 ? "s" : "") + " ago";
        }

        long monthsAgo = daysAgo / 30;
        return monthsAgo + " month" + (monthsAgo != 1 ? "s" : "") + " ago";
    }

    // ── Data helpers ─────────────────────────────────────────────────────

    private static int getPlayerLevel(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "profile.level", null);
        if (val instanceof Number n) return n.intValue();
        return 1;
    }

    private static int getPlayerAchievementPoints(UUID uuid) {
        Object val = PlayerRepository.getField(uuid, "achievements.points", null);
        if (val instanceof Number n) return n.intValue();
        return 0;
    }

    private static String getPlayerName(UUID uuid) {
        // Try online player first
        Player onlinePlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (onlinePlayer != null) return onlinePlayer.getUsername();

        // Try database
        Object val = PlayerRepository.getField(uuid, "profile.name", null);
        if (val instanceof String s) return s;

        return uuid.toString().substring(0, 8);
    }

    private static String getPlayerRankPrefix(UUID uuid) {
        // Try online player first
        Player onlinePlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (onlinePlayer != null) {
            User user = UserManager.getUser(uuid);
            return user.getRank().getPrefix();
        }
        return "";
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    // ── Sort type enum ───────────────────────────────────────────────────

    public enum SortType {
        DEFAULT, ALPHABETICAL, LAST_ONLINE
    }

    // ── Friend display entry ─────────────────────────────────────────────

    private record FriendDisplayEntry(
            UUID uuid,
            String name,
            String nickname,
            boolean bestFriend,
            boolean online,
            long addedTimestamp
    ) {}
}
