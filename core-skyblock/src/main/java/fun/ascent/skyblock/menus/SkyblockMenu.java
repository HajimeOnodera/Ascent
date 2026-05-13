package fun.ascent.skyblock.menus;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.menus.command.skyblockMenu.EquipmentMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.gui.CollectionOverviewMenu;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.stats.Stat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.*;

public class SkyblockMenu {

    private static final int INFO_SLOT = 13;
    private static final int CLOSE_SLOT = 49;
    private static final int SKILLS_SLOT = 19;
    private static final int COLLS_SLOT = 20;
    private static final int RECIPE_SLOT = 21;
    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, miniMessage().deserialize("SkyBlock Menu"));

        fill(inv);

        ProfilePlayer plProfile = player.getActiveProfileData();
        if(plProfile == null)return;

        inv.setItemStack(INFO_SLOT, getProfileItem(plProfile));
        inv.setItemStack(SKILLS_SLOT,getSkillsItem());
        inv.setItemStack(COLLS_SLOT,getCollectionItem(plProfile));
        inv.setItemStack(RECIPE_SLOT,getRecipeItem(plProfile));

        // Close button
        inv.setItemStack(CLOSE_SLOT, ItemStack.builder(Material.BARRIER)
                .customName(miniMessage().deserialize("<red>Close"))
                .build());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == CLOSE_SLOT) {
                player.closeInventory();
            } else if (slot == INFO_SLOT) {
                EquipmentMenu.open(player);
            } else if (slot == COLLS_SLOT) {
                CollectionOverviewMenu.open(player);
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack getRecipeItem(ProfilePlayer plProfile) {
        List<Component> lore = new ArrayList<>();
        lore.add(miniMessage().deserialize("<gray>Through your adventure, you will").decoration(TextDecoration.ITALIC,false));
        lore.add(miniMessage().deserialize("<gray>unlock recipes for all kinds of").decoration(TextDecoration.ITALIC,false));
        lore.add(miniMessage().deserialize("<gray>special items! You can view how to").decoration(TextDecoration.ITALIC,false));
        lore.add(miniMessage().deserialize("<gray>craft these items here."));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<gray>Recipe Book Unlocked: <yellow>x.y<gold>%").decoration(TextDecoration.ITALIC,false));
        lore.add(miniMessage().deserialize("<white>────────────").decoration(TextDecoration.ITALIC,false).decoration(TextDecoration.STRIKETHROUGH,true).append(miniMessage().deserialize(
                "<yellow> x<gold>/<yellow>y"
        ).decoration(TextDecoration.ITALIC,false).decoration(TextDecoration.STRIKETHROUGH,false)));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<dark_gray>Also accessible via /recipes.").decoration(TextDecoration.ITALIC,false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<yellow>Click to view!").decoration(TextDecoration.ITALIC,false));

        return ItemStackCreator.createNamedItemStack(Material.BOOK,
                        miniMessage().deserialize("<green>Recipe Book"))
                .lore(lore).build();
    }

    private static ItemStack getCollectionItem(ProfilePlayer plProfile) {
        List<Component> lore = new ArrayList<>();
        lore.add(miniMessage().deserialize("<gray>View all of the items available in").decoration(TextDecoration.ITALIC,false));
        lore.add(miniMessage().deserialize("<gray>SkyBlock. Collect more of an item to").decoration(TextDecoration.ITALIC,false));
        lore.add(miniMessage().deserialize("<gray>unlock rewards on your way to").decoration(TextDecoration.ITALIC,false));
        lore.add(miniMessage().deserialize("<gray>becoming a master of SkyBlock!").decoration(TextDecoration.ITALIC,false));
        lore.add(Component.empty());
        
        int unlocked = plProfile.skyblockPlayer.getActiveProfile().unlockedCollections.size();
        int total = fun.ascent.skyblock.player.collections.CollectionRegistry.getTotalCollectionsCount();
        double percent = total == 0 ? 0 : (double) unlocked / total * 100;
        
        lore.add(miniMessage().deserialize("<gray>Collections unlocked: <yellow>" + String.format("%.1f", percent) + "<gold>%").decoration(TextDecoration.ITALIC,false));
        
        // Progress bar (restored style)
        int barLength = 20;
        int filled = total == 0 ? 0 : (int) (percent / 100 * barLength);
        
        String barStr = "<green>" + "─".repeat(filled) + "<white>" + "─".repeat(barLength - filled);
        
        lore.add(miniMessage().deserialize(barStr)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.STRIKETHROUGH, true)
                .append(miniMessage().deserialize(" <yellow>" + unlocked + "<gold>/<yellow>" + total)
                        .decoration(TextDecoration.STRIKETHROUGH, false)));

        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<yellow>Click to view!").decoration(TextDecoration.ITALIC,false));
        return ItemStackCreator.createNamedItemStack(Material.PAINTING,
                miniMessage().deserialize("<green>Collections"))
                .lore(lore).build();
    }

    private static ItemStack getSkillsItem() {
        List<Component> lore = new ArrayList<>();
        lore.add(miniMessage().deserialize("<gray>View your Skill progression and").decoration(TextDecoration.ITALIC,false));
        lore.add(miniMessage().deserialize("<gray>rewards.").decoration(TextDecoration.ITALIC,false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<dark_gray>Also accessible via /skills").decoration(TextDecoration.ITALIC,false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<yellow> Click to view!").decoration(TextDecoration.ITALIC,false));
        return ItemStackCreator.
                createNamedItemStack(Material.DIAMOND_SWORD,
                        StringUtility.text("<green>Your Skills"))
                .lore(lore).build();
    }

    private static ItemStack getProfileItem(ProfilePlayer plProfile){
        List<Component> lore = new ArrayList<>();
        String[] stats = new String[]{"speed","strength","defense","critical_chance","critical_damage","health","intelligence"};
        lore.add(miniMessage().deserialize("<gray>View your equipment, stats, and more!").decoration(TextDecoration.ITALIC,false));
        lore.add(Component.empty());
        for(String stat : stats) {
            Stat stat1 = plProfile.stats.get(stat);
            if(stat1 == null)continue;
            lore.add(stat1.getText(1));
        }

        lore.add(miniMessage().deserialize("<dark_grey> and more...").decoration(TextDecoration.ITALIC,false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<dark_gray>Also accessible via /stats").decoration(TextDecoration.ITALIC,false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<yellow>Click to view!").decoration(TextDecoration.ITALIC,false));
        return ItemStack.builder(Material.PLAYER_HEAD)
                .customName(miniMessage().deserialize("<green>Your SkyBlock Profile").decoration(TextDecoration.ITALIC,false))
                .lore(lore)
                .build();
    }

    private static void fill(Inventory inventory) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty())
                .build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItemStack(i, filler);
        }
    }
}
