package fun.ascent.skyblock.player.collections.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.minion.model.MinionType;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.*;

import static fun.ascent.common.StringUtility.text;

public class GUICraftedMinions {

    private static final int[] HEAD_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private static final int FILTER_SLOT = 8;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;
    private static final int STATS_SLOT = 50;
    private static final int NEXT_SLOT = 53;

    // Track active page and filter for players (key is player UUID)
    private static final Map<UUID, Integer> PLAYER_PAGES = new HashMap<>();
    private static final Map<UUID, Boolean> PLAYER_FILTER_CRAFTED = new HashMap<>();

    public static void open(SkyblockPlayer player) {
        UUID uuid = player.getUuid();
        PLAYER_PAGES.put(uuid, 0);
        PLAYER_FILTER_CRAFTED.put(uuid, false); // Default: All Minions
        openMenu(player);
    }

    private static void openMenu(SkyblockPlayer player) {
        UUID uuid = player.getUuid();
        int page = PLAYER_PAGES.getOrDefault(uuid, 0);
        boolean craftedOnly = PLAYER_FILTER_CRAFTED.getOrDefault(uuid, false);

        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("Crafted Minions"));

        // 1. Fill fillers
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE).customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) {
            inv.setItemStack(i, filler);
        }

        // 2. Clear head slots
        for (int slot : HEAD_SLOTS) {
            inv.setItemStack(slot, ItemStack.AIR);
        }

        // 3. Get sorted minions list
        List<MinionType> minions = Arrays.stream(MinionType.values())
                .filter(type -> {
                    String prefixGen = type.name().toUpperCase() + "_GENERATOR_";
                    String prefixMin = type.name().toUpperCase() + "_MINION_";
                    return fun.ascent.skyblock.item.ItemRegistry.getAllItems().stream().anyMatch(item -> {
                        String id = item.getItemId().toUpperCase();
                        return id.startsWith(prefixGen) || id.startsWith(prefixMin);
                    });
                })
                .sorted(Comparator.comparing(MinionType::getCategory).thenComparing(MinionType::getDisplayName))
                .toList();

        SkyblockProfile profile = player.getActiveProfile();

        // Filter if requested
        if (craftedOnly) {
            minions = minions.stream()
                    .filter(type -> hasCraftedAnyTier(profile, type))
                    .toList();
        }

        int totalMinionsCount = minions.size();
        int startIndex = page * HEAD_SLOTS.length;
        int endIndex = Math.min(startIndex + HEAD_SLOTS.length, totalMinionsCount);

        // Populate heads
        for (int i = startIndex; i < endIndex; i++) {
            int slotIdx = HEAD_SLOTS[i - startIndex];
            MinionType type = minions.get(i);
            inv.setItemStack(slotIdx, buildMinionHead(profile, type));
        }

        // 4. Filter Slot
        inv.setItemStack(FILTER_SLOT, buildFilterItem(craftedOnly));

        // 5. Back Slot
        if (page > 0) {
            inv.setItemStack(BACK_SLOT, ItemStack.builder(Material.ARROW)
                    .customName(text("§aPrevious Page"))
                    .lore(text("§7To Page " + page))
                    .build());
        } else {
            inv.setItemStack(BACK_SLOT, ItemStack.builder(Material.ARROW)
                    .customName(text("§aGo Back"))
                    .lore(text("§7To Collections"))
                    .build());
        }

        // 6. Close Slot
        inv.setItemStack(CLOSE_SLOT, ItemStack.builder(Material.BARRIER)
                .customName(text("§cClose"))
                .build());

        // 7. Stats Slot
        inv.setItemStack(STATS_SLOT, buildStatsItem(player));

        // 8. Next Slot
        boolean hasNextPage = endIndex < totalMinionsCount;
        if (hasNextPage) {
            inv.setItemStack(NEXT_SLOT, ItemStack.builder(Material.ARROW)
                    .customName(text("§aNext Page"))
                    .lore(text("§7To Page " + (page + 2)))
                    .build());
        }

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            if (event.getPlayer() instanceof SkyblockPlayer p) {
                handleClick(event, p, inv);
            }
        });

        player.openInventory(inv);
    }

    private static void handleClick(InventoryPreClickEvent event, SkyblockPlayer player, Inventory inv) {
        event.setCancelled(true);
        int slot = event.getSlot();
        UUID uuid = player.getUuid();

        int page = PLAYER_PAGES.getOrDefault(uuid, 0);
        boolean craftedOnly = PLAYER_FILTER_CRAFTED.getOrDefault(uuid, false);

        if (slot == CLOSE_SLOT) {
            player.closeInventory();
            return;
        }

        if (slot == BACK_SLOT) {
            if (page > 0) {
                PLAYER_PAGES.put(uuid, page - 1);
                openMenu(player);
            } else {
                CollectionOverviewMenu.open(player);
            }
            return;
        }

        if (slot == NEXT_SLOT) {
            ItemStack stack = inv.getItemStack(NEXT_SLOT);
            if (stack != null && stack.material() == Material.ARROW) {
                PLAYER_PAGES.put(uuid, page + 1);
                openMenu(player);
            }
            return;
        }

        if (slot == FILTER_SLOT) {
            PLAYER_FILTER_CRAFTED.put(uuid, !craftedOnly);
            PLAYER_PAGES.put(uuid, 0); // Reset page on filter toggle
            openMenu(player);
            player.playSound(net.kyori.adventure.sound.Sound.sound(net.minestom.server.sound.SoundEvent.UI_BUTTON_CLICK, net.kyori.adventure.sound.Sound.Source.MASTER, 1f, 1f));
            return;
        }

        // Check if head was clicked
        ItemStack clickedItem = inv.getItemStack(slot);
        if (clickedItem != null && clickedItem.material() == Material.PLAYER_HEAD) {
            player.playSound(net.kyori.adventure.sound.Sound.sound(net.minestom.server.sound.SoundEvent.BLOCK_NOTE_BLOCK_PLING, net.kyori.adventure.sound.Sound.Source.MASTER, 1f, 0.5f));
            player.sendMessage("§cMinion recipes view is coming soon!");
        }
    }

    private static boolean hasCraftedAnyTier(SkyblockProfile profile, MinionType type) {
        if (profile == null || profile.uniqueMinionsCrafted == null) return false;
        String prefixGen = type.name().toUpperCase() + "_GENERATOR_";
        String prefixMin = type.name().toUpperCase() + "_MINION_";
        for (String id : profile.uniqueMinionsCrafted) {
            if (id.startsWith(prefixGen) || id.startsWith(prefixMin)) {
                return true;
            }
        }
        return false;
    }

    private static ItemStack buildMinionHead(SkyblockProfile profile, MinionType type) {
        boolean craftedAny = hasCraftedAnyTier(profile, type);
        String colorPrefix = craftedAny ? "§a" : "§c";

        List<String> lore = new ArrayList<>();
        for (int tier = 1; tier <= 12; tier++) {
            String genId = type.name().toUpperCase() + "_GENERATOR_" + tier;
            String minId = type.name().toUpperCase() + "_MINION_" + tier;
            boolean craftedTier = profile != null && profile.uniqueMinionsCrafted != null
                    && (profile.uniqueMinionsCrafted.contains(genId) || profile.uniqueMinionsCrafted.contains(minId));

            if (craftedTier) {
                lore.add("§a✔ Tier " + fun.ascent.skyblock.minion.visual.MinionItems.roman(tier));
            } else {
                lore.add("§c✖ Tier " + fun.ascent.skyblock.minion.visual.MinionItems.roman(tier));
            }
        }

        lore.add("");
        lore.add("§eClick to view recipes!");

        return fun.ascent.common.item.ItemStackCreator.getStackHead(
                colorPrefix + type.getDisplayName(),
                new net.minestom.server.entity.PlayerSkin(type.getTexture(), null),
                1,
                lore.toArray(new String[0])
        ).build();
    }

    private static ItemStack buildFilterItem(boolean craftedOnly) {
        List<String> lore = new ArrayList<>();
        if (craftedOnly) {
            lore.add("§7Currently showing: §eCrafted Minions Only");
            lore.add("§7Shows only unique minions you");
            lore.add("§7have crafted.");
        } else {
            lore.add("§7Currently showing: §eAll Minions");
            lore.add("§7Shows all craftable minion tiers.");
        }
        lore.add("");
        lore.add("§eClick to toggle filter!");

        return fun.ascent.common.item.ItemStackCreator.getStackHead(
                "§aMinion Filter",
                "ebcc099f3a00ece0e5c4b31d31c828e52b06348d0a4eac11f3fcbef3c05cb407",
                1,
                lore.toArray(new String[0])
        ).build();
    }

    private static ItemStack buildStatsItem(SkyblockPlayer player) {
        SkyblockProfile profile = player.getActiveProfile();
        int minionsCrafted = profile != null ? profile.minionsCrafted : 1;
        int minionSlots = profile != null ? profile.minionSlots : 5;

        long registeredMinions = fun.ascent.skyblock.item.ItemRegistry.getAllItems().stream()
                .filter(item -> {
                    String id = item.getItemId().toUpperCase();
                    return id.matches(".*_(MINION|GENERATOR)_\\d+");
                })
                .count();
        int totalMinions = registeredMinions > 0 ? (int) registeredMinions : 713;

        List<String> lore = new ArrayList<>(List.of(
                "§7Unique Minions: §e" + minionsCrafted + "§7/§6" + totalMinions,
                "§7Placable Minions: §e" + minionSlots + "§7/§632"
        ));

        return ItemStack.builder(Material.REDSTONE_TORCH)
                .customName(text("§aMinion Statistics"))
                .lore(lore.stream().map(StringUtility::text).toList())
                .build();
    }
}
