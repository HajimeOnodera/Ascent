package fun.ascent.skyblock.player.collections.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionCategory;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static fun.ascent.common.StringUtility.text;

public class CollectionCategoryMenu {

    private static final int[] ITEM_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player, CollectionCategory category) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text(category.getName() + " Collections"));

        CollectionMenuFormat.fill(inv);

        inv.setItemStack(4, ItemStack.builder(category.getIcon())
                .customName(text("<green>" + category.getName() + " Collections"))
                .build());

        List<CollectionCategory.ItemCollection> collections = category.getCollections();
        for (int i = 0; i < collections.size() && i < ITEM_SLOTS.length; i++) {
            inv.setItemStack(ITEM_SLOTS[i], buildCollectionItem(player, collections.get(i)));
        }

        inv.setItemStack(BACK_SLOT, CollectionMenuFormat.backButton("Collection Menu"));
        inv.setItemStack(CLOSE_SLOT, CollectionMenuFormat.closeButton());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            if (!(event.getPlayer() instanceof SkyblockPlayer pl)) return;
            event.setCancelled(true);

            int slot = event.getSlot();
            if (slot == CLOSE_SLOT) {
                pl.closeInventory();
                return;
            }
            if (slot == BACK_SLOT) {
                CollectionOverviewMenu.open(pl);
                return;
            }

            for (int i = 0; i < collections.size() && i < ITEM_SLOTS.length; i++) {
                if (slot == ITEM_SLOTS[i]) {
                    CollectionTierMenu.open(pl, collections.get(i));
                    return;
                }
            }
        });

        player.openInventory(inv);
    }

    private static String getRomanNumeral(int number) {
        if (number <= 0) return "";
        return switch(number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(number);
        };
    }

    private static ItemStack buildCollectionItem(SkyblockPlayer player, CollectionCategory.ItemCollection collection) {
        int progress = player.getActiveProfile().unlockedCollections.getOrDefault(collection.itemId(), 0);
        int currentTier = collection.getTierFromProgress(progress);

        if (progress == 0) {
            List<Component> lore = new ArrayList<>();
            lore.add(text("<gray>Find this item to add it to your"));
            lore.add(text("<gray>collection and unlock collection"));
            lore.add(text("<gray>rewards!"));

            return ItemStack.builder(Material.GRAY_DYE)
                    .customName(text("<red>" + collection.name()))
                    .lore(lore)
                    .build();
        }

        CollectionCategory.CollectionReward nextReward = collection.getRewardAtTier(currentTier + 1);

        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>View all your " + collection.name() + " Collection"));
        lore.add(text("<gray>progress and rewards!"));
        lore.add(Component.text(" "));

        if (nextReward != null) {
            double ratio = nextReward.requirement() <= 0 ? 1.0 : Math.clamp((double) progress / nextReward.requirement(), 0.0, 1.0);
            double percent = ratio * 100;

            lore.add(text("<gray>Progress to " + collection.name() + " " + getRomanNumeral(currentTier + 1) + ": <yellow>" + String.format(Locale.US, "%.1f", percent) + "<gold>%"));
            
            String baseBar = "───────────────";
            int segments = baseBar.length();
            int filled = (int) Math.round(ratio * segments);
            String completed = filled > 0 ? "§2§m" + baseBar.substring(0, Math.min(filled, segments)) : "";
            String remaining = "§f§m" + baseBar.substring(Math.min(filled, segments));
            lore.add(text(completed + remaining + "§r <yellow>" + progress + "<gold>/<yellow>" + nextReward.requirement()));
            
            lore.add(Component.text(" "));
            lore.add(text("<gray>" + collection.name() + " " + getRomanNumeral(currentTier + 1) + " Rewards:"));
            nextReward.unlocks().forEach(u -> lore.add(text("  " + u.getDisplay())));
        } else {
            lore.add(text("<green>MAXED OUT!"));
        }

        // Use GRAY_DYE if locked, otherwise use the actual material icon
        Material material = progress > 0 ? collection.icon() : Material.GRAY_DYE;
        String romanStr = currentTier > 0 ? " " + getRomanNumeral(currentTier) : "";

        return ItemStack.builder(material)
                .customName(text("<yellow>" + collection.name() + romanStr))
                .lore(lore)
                .build();
    }
}
