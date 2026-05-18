package fun.ascent.skyblock.menus;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.crafting.RecipeRegistry;
import fun.ascent.skyblock.crafting.gui.GUIRecipeBook;
import fun.ascent.skyblock.menus.command.skyblockMenu.EquipmentMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.player.collections.gui.CollectionMenuFormat;
import fun.ascent.skyblock.player.collections.gui.CollectionOverviewMenu;
import fun.ascent.skyblock.player.level.gui.SkyblockLevelMenu;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.skill.gui.SkillOverviewMenu;
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
    private static final int LEVELING_SLOT = 22;
    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, miniMessage().deserialize("SkyBlock Menu"));

        fill(inv);

        ProfilePlayer plProfile = player.getActiveProfileData();
        if(plProfile == null)return;

        inv.setItemStack(INFO_SLOT, getProfileItem(plProfile));
        inv.setItemStack(SKILLS_SLOT,getSkillsItem());
        inv.setItemStack(COLLS_SLOT,getCollectionItem(plProfile));
        inv.setItemStack(RECIPE_SLOT,getRecipeItem(plProfile));
        inv.setItemStack(LEVELING_SLOT,getLevelingItem(plProfile));

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
            } else if (slot == SKILLS_SLOT) {
                SkillOverviewMenu.open(player);
            } else if (slot == RECIPE_SLOT) {
                GUIRecipeBook.open(player);
            } else if (slot == LEVELING_SLOT) {
                SkyblockLevelMenu.open(player);
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
        
        int unlocked = plProfile.skyblockPlayer.getActiveProfile().unlockedRecipes.size();
        int total = RecipeRegistry.getTotalRecipesCount();
        double percent = total == 0 ? 0 : (double) unlocked / total * 100;

        lore.add(miniMessage().deserialize("<gray>Recipe Book Unlocked: <yellow>" + String.format("%.1f", percent) + "<gold>%").decoration(TextDecoration.ITALIC,false));
        
        CollectionMenuFormat.addProgress(lore, unlocked, total, "");

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
        int total = CollectionRegistry.getTotalCollectionsCount();
        double percent = total == 0 ? 0 : (double) unlocked / total * 100;
        
        lore.add(miniMessage().deserialize("<gray>Collections unlocked: <yellow>" + String.format("%.1f", percent) + "<gold>%").decoration(TextDecoration.ITALIC,false));
        
        CollectionMenuFormat.addProgress(lore, unlocked, total, "");

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
        
        return ItemStackCreator.getStackHead(
                miniMessage().deserialize("<green>Your SkyBlock Profile"),
                plProfile.skyblockPlayer.getSkin(),
                1,
                lore
        ).build();
    }

    private static ItemStack getLevelingItem(ProfilePlayer plProfile) {
        List<Component> lore = new ArrayList<>();
        int level = plProfile.level.curLevel;
        double currentXp = plProfile.level.progress.curProgress;
        int reqXp = plProfile.level.progress.reqProgress;
        int nextLevel = level + 1;

        lore.add(miniMessage().deserialize("<gray>Your SkyBlock Level: <dark_gray>[" + SkyblockLevel.getLevelColour(level) + level + "<dark_gray>]").decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<gray>Determine how far you've").decoration(TextDecoration.ITALIC, false));
        lore.add(miniMessage().deserialize("<gray>progressed in SkyBlock and earn").decoration(TextDecoration.ITALIC, false));
        lore.add(miniMessage().deserialize("<gray>rewards from completing unique").decoration(TextDecoration.ITALIC, false));
        lore.add(miniMessage().deserialize("<gray>tasks.").decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<gray>Progress to Level " + nextLevel + ":").decoration(TextDecoration.ITALIC, false));
        
        int totalBars = 20;
        double percent = currentXp / reqXp;
        int completed = (int) Math.round(percent * totalBars);
        completed = Math.clamp(completed, 0, totalBars);
        int remaining = totalBars - completed;
        String bar = "<dark_aqua>" + "▬".repeat(completed) + "<white>" + "▬".repeat(remaining) + " <aqua>" + (int) currentXp + "/" + reqXp + " XP";
        
        lore.add(miniMessage().deserialize(bar).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<dark_gray>Also accessible via /levels").decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(miniMessage().deserialize("<yellow>Click to view!").decoration(TextDecoration.ITALIC, false));

        return ItemStackCreator.getStackHead(
                miniMessage().deserialize("<green>SkyBlock Leveling"),
                "3255327dd8e90afad681a19231665bea2bd06065a09d77ac1408837f9e0b242",
                1,
                lore
        ).build();
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
