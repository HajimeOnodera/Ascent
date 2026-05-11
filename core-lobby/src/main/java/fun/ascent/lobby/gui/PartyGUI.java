package fun.ascent.lobby.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.common.gui.GUIItem;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import fun.ascent.database.PartyLookup;
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

public class PartyGUI extends InventoryGUI {

    private static final int[] COLOURED_PANE_SLOTS = {9, 10, 11, 12, 13, 14, 15, 16, 17};
    private static final int MEMBERS_PER_PAGE = 18;
    private static final int[] MEMBER_SLOTS = {
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44
    };

    private final int currentPage;
    private final SortType sortType;
    private final boolean sortReversed;
    private final String searchFilter;

    public PartyGUI() {
        this(1, SortType.DEFAULT, false, null);
    }

    public PartyGUI(int page, SortType sortType, boolean sortReversed, String searchFilter) {
        super("Party", InventoryType.CHEST_6_ROW);
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

        // Blue pane divider (row 1)
        for (int slot : COLOURED_PANE_SLOTS) {
            set(slot, ItemStackCreator.createNamedItemStack(Material.BLUE_STAINED_GLASS_PANE));
        }

        // ── Top Row Tabs ─────────────────────────────────────────────────

        // Player Head (slot 2) - go back
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

        // Friends tab (slot 3)
        set(new GUIClickableItem(3) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new FriendsGUI().open(player);
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

        // Party tab (slot 4) - currently viewing (enchanted)
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.enchant(ItemStackCreator.getStackHead(
                        "<green>Party",
                        "667963ca1ffdc24a10b397ff8161d0da82d6a3f4788d5f67f1a9f9bfbc1eb1",
                        1,
                        "<gray>Create a party and join up with",
                        "<gray>other players to play games",
                        "<gray>together!",
                        "",
                        "<yellow>Currently viewing!"
                ));
            }
        });

        // Guild tab (slot 5)
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

        // Recent Players tab (slot 6)
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

        // ── Party Content ────────────────────────────────────────────────

        PartyLookup.PartyInfo partyInfo = PartyLookup.getPartyForPlayer(uuid);

        if (partyInfo == null) {
            setNotInPartyItems();
        } else {
            setInPartyItems(player, partyInfo);
        }

        updateItemStacks(getInventory(), player);
    }

    // ── Not in party layout ──────────────────────────────────────────────

    private void setNotInPartyItems() {
        // Create Party (slot 31 - center)
        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<green>Use <white>/party invite <name> <green>to invite a player!"));
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Create Party",
                        Material.MAGENTA_TERRACOTTA, 1,
                        "",
                        "<yellow>Click to invite a player to your",
                        "<yellow>party"
                );
            }
        });
    }

    // ── In party layout ──────────────────────────────────────────────────

    private void setInPartyItems(Player player, PartyLookup.PartyInfo partyInfo) {
        UUID uuid = player.getUuid();
        Set<UUID> onlineUuids = MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream().map(Player::getUuid).collect(Collectors.toSet());

        // Determine role
        PartyLookup.PartyMember selfMember = partyInfo.members().stream()
                .filter(m -> m.uuid().equals(uuid))
                .findFirst().orElse(null);
        boolean isLeader = selfMember != null && "LEADER".equals(selfMember.role());

        // Build display entries
        List<MemberDisplayEntry> allMembers = partyInfo.members().stream()
                .map(m -> {
                    String name = getPlayerName(m.uuid());
                    boolean online = onlineUuids.contains(m.uuid());
                    return new MemberDisplayEntry(m.uuid(), name, m.role(), online, m.joined());
                })
                .collect(Collectors.toList());

        // Apply search filter
        List<MemberDisplayEntry> filteredMembers;
        if (searchFilter != null && !searchFilter.isEmpty()) {
            String lower = searchFilter.toLowerCase();
            filteredMembers = allMembers.stream()
                    .filter(m -> m.name.toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        } else {
            filteredMembers = allMembers;
        }

        // Sort
        Comparator<MemberDisplayEntry> comparator = switch (sortType) {
            case DEFAULT -> Comparator.comparing((MemberDisplayEntry me) -> !me.online)
                    .thenComparing(me -> me.name.toLowerCase());
            case ALPHABETICAL -> Comparator.comparing(me -> me.name.toLowerCase());
            case LAST_ONLINE -> Comparator.comparing((MemberDisplayEntry me) -> me.online ? 0 : 1);
        };
        if (sortReversed) comparator = comparator.reversed();
        filteredMembers.sort(comparator);

        // Pagination
        int totalPages = Math.max(1, (int) Math.ceil((double) filteredMembers.size() / MEMBERS_PER_PAGE));
        int page = Math.min(currentPage, totalPages);
        int startIndex = (page - 1) * MEMBERS_PER_PAGE;
        int endIndex = Math.min(startIndex + MEMBERS_PER_PAGE, filteredMembers.size());
        List<MemberDisplayEntry> pageEntries = filteredMembers.subList(startIndex, endIndex);

        // ── Action buttons (Row 2) ───────────────────────────────────────

        // Invite Player (slot 18)
        set(new GUIClickableItem(18) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<green>Use <white>/party invite <name> <green>to invite a player!"));
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<green>Invite Player",
                        Material.WRITABLE_BOOK, 1,
                        "<gray>Invites a player to your party."
                );
            }
        });

        // Remove Player (slot 19)
        set(new GUIClickableItem(19) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(StringUtility.text("<green>Use <white>/party kick <name> <green>to kick a player!"));
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack(
                        "<red>Remove Player",
                        Material.BARRIER, 1,
                        "<gray>Removes a player from your party."
                );
            }
        });

        if (isLeader) {
            // Warp Party (slot 20) - leader only
            set(new GUIClickableItem(20) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    player.sendMessage(StringUtility.text("<green>Use <white>/party warp <green>to warp all party members!"));
                    player.closeInventory();
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack(
                            "<green>Warp Party",
                            Material.NETHER_BRICK, 1,
                            "<gray>Teleports all party members to your",
                            "<gray>lobby."
                    );
                }
            });

            // Disband Party (slot 21) - leader only
            set(new GUIClickableItem(21) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    player.sendMessage(StringUtility.text("<green>Use <white>/party disband <green>to disband the party!"));
                    player.closeInventory();
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack(
                            "<green>Disband Party",
                            Material.TNT, 1,
                            "<gray>Breaks up the current party."
                    );
                }
            });
        } else {
            // Leave Party (slot 20) - non-leader
            set(new GUIClickableItem(20) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    player.sendMessage(StringUtility.text("<green>Use <white>/party leave <green>to leave the party!"));
                    player.closeInventory();
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack(
                            "<red>Leave Party",
                            Material.BARRIER, 1,
                            "<gray>Leave the current party."
                    );
                }
            });
        }

        // Change Sort (slot 25)
        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                SortType nextSort = switch (sortType) {
                    case DEFAULT -> SortType.ALPHABETICAL;
                    case ALPHABETICAL -> SortType.LAST_ONLINE;
                    case LAST_ONLINE -> SortType.DEFAULT;
                };
                new PartyGUI(1, nextSort, sortReversed, searchFilter).open(player);
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

        // Search Players (slot 26)
        int matchingCount = filteredMembers.size();
        set(new GUIClickableItem(26) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                if (searchFilter != null && !searchFilter.isEmpty()) {
                    new PartyGUI(1, sortType, sortReversed, null).open(player);
                } else {
                    player.sendMessage(StringUtility.text("<gray>Search is available for larger parties."));
                }
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                if (searchFilter != null && !searchFilter.isEmpty()) {
                    return ItemStackCreator.enchant(ItemStackCreator.getStack(
                            "<green>Search: <yellow>" + searchFilter,
                            Material.OAK_SIGN, 1,
                            "<gray>Currently filtering by: <white>" + searchFilter,
                            "<gray>Showing <yellow>" + matchingCount + " <gray>matching members",
                            "",
                            "<yellow>Click to clear the search filter."
                    ));
                } else {
                    return ItemStackCreator.getStack(
                            "<green>Search Players",
                            Material.OAK_SIGN, 1,
                            "<gray>Search for a player by name",
                            "<gray>in your party.",
                            "",
                            "<yellow>Click to search!"
                    );
                }
            }
        });

        // ── Member heads (slots 27-44) ───────────────────────────────────

        for (int i = 0; i < MEMBER_SLOTS.length; i++) {
            int slot = MEMBER_SLOTS[i];
            if (i < pageEntries.size()) {
                MemberDisplayEntry entry = pageEntries.get(i);
                set(createMemberItem(slot, entry));
            }
        }

        // ── Pagination (row 5) ───────────────────────────────────────────

        if (page > 1) {
            set(new GUIClickableItem(45) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    new PartyGUI(currentPage - 1, sortType, sortReversed, searchFilter).open(player);
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

        // Page Info (slot 49)
        int totalMemberCount = allMembers.size();
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                if (searchFilter != null && !searchFilter.isEmpty()) {
                    return ItemStackCreator.getStack(
                            "<green>Page " + page + "/" + totalPages,
                            Material.BOOK, 1,
                            "<gray>Showing <yellow>" + matchingCount + " <gray>of <yellow>" + totalMemberCount + " <gray>members",
                            "<gray>Searching for: <white>" + searchFilter
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "<green>Page " + page + "/" + totalPages,
                            Material.BOOK, 1,
                            "<gray>Total members: <yellow>" + totalMemberCount
                    );
                }
            }
        });

        if (page < totalPages) {
            set(new GUIClickableItem(53) {
                @Override
                public void run(InventoryPreClickEvent e, Player player) {
                    new PartyGUI(currentPage + 1, sortType, sortReversed, searchFilter).open(player);
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
    }

    // ── Member head item builder ─────────────────────────────────────────

    private GUIItem createMemberItem(int slot, MemberDisplayEntry entry) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(Player p) {
                List<String> lore = new ArrayList<>();

                int memberLevel = getPlayerLevel(entry.uuid);
                int memberPoints = getPlayerAchievementPoints(entry.uuid);

                lore.add("<gray>Ascent Level: <gold>" + memberLevel);
                lore.add("<gray>Achievement Points: <yellow>" + StringUtility.commaify(memberPoints));
                lore.add("<gray>Guild: <aqua>None");
                lore.add("");

                // Role indicator with color
                String roleDisplay = switch (entry.role) {
                    case "LEADER" -> "<gold>Party Leader";
                    case "MODERATOR" -> "<dark_purple>Party Moderator";
                    default -> "<gray>Party Member";
                };
                lore.add(roleDisplay);

                // Online/offline status
                if (entry.online) {
                    lore.add("<green>Online");
                } else {
                    lore.add("<red>Offline");
                }

                // Name with role-based coloring
                String nameColor = entry.online ? "<green>" : "<gray>";
                String displayName;
                String rankPrefix = getPlayerRankPrefix(entry.uuid);
                if (rankPrefix != null && !rankPrefix.isEmpty()) {
                    displayName = rankPrefix + entry.name;
                } else {
                    displayName = nameColor + entry.name;
                }

                // Use online player's skin if available
                Player onlinePlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(entry.uuid);
                if (onlinePlayer != null && onlinePlayer.getSkin() != null) {
                    return ItemStackCreator.getStackHead(displayName, onlinePlayer.getSkin(), 1,
                            lore.toArray(new String[0]));
                } else {
                    return ItemStackCreator.getStackHead(displayName,
                            "18614241b980319c02f5ee3ae1a7fc7ebf8b3fdd5301ed3d4e2159a80dae1d2c",
                            1, lore.toArray(new String[0]));
                }
            }
        };
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
        Player onlinePlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (onlinePlayer != null) return onlinePlayer.getUsername();

        Object val = PlayerRepository.getField(uuid, "profile.name", null);
        if (val instanceof String s) return s;

        return uuid.toString().substring(0, 8);
    }

    private static String getPlayerRankPrefix(UUID uuid) {
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

    // ── Member display entry ─────────────────────────────────────────────

    private record MemberDisplayEntry(
            UUID uuid,
            String name,
            String role,
            boolean online,
            boolean joined
    ) {}
}
