package fun.ascent.skyblock.menus;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.gui.RefreshingGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.skyblock.crafting.gui.CraftingMenu;
import fun.ascent.skyblock.crafting.gui.GUIRecipeBook;
import fun.ascent.skyblock.menus.command.skyblockMenu.EquipmentMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.gui.CollectionOverviewMenu;
import fun.ascent.skyblock.player.level.gui.SkyblockLevelMenu;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.skill.gui.SkillOverviewMenu;
import fun.ascent.skyblock.player.stats.Stat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.*;

@SuppressWarnings("deprecation")
public class SkyblockMenu extends InventoryGUI implements RefreshingGUI {

    public SkyblockMenu() {
        super("SkyBlock Menu", InventoryType.CHEST_6_ROW);
    }

    public static void open(SkyblockPlayer player) {
        new SkyblockMenu().open((Player) player);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);

        set(GUIClickableItem.getCloseItem(49));

        ProfilePlayer plProfile = ((SkyblockPlayer) e.player()).getActiveProfileData();
        if (plProfile == null) return;

        setYourProfile(plProfile);
        setSkyblockLeveling(plProfile);
        setYourBags();
        setPets();
        setRecipeBook();
        setStorage();
        setQuests();
        setCalendar();
        setSkills();
        setCollections();
        setCraftingTable();
        setFastTravel();
        setProfileManagement();
    }

    private void setYourProfile(ProfilePlayer plProfile) {
        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                EquipmentMenu.open((SkyblockPlayer) player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>View your equipment, stats, and more!").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                
                String[] stats = new String[]{"health", "defense", "speed", "strength", "intelligence", "critical_chance", "critical_damage"};
                for (String statName : stats) {
                    Stat stat = plProfile.stats.get(statName);
                    if (stat == null) continue;
                    lore.add(stat.getText(0));
                }
                
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStackHead(
                        miniMessage().deserialize("<green>Your SkyBlock Profile"),
                        player.getSkin(),
                        1,
                        lore
                );
            }
        });
    }

    private void setSkyblockLeveling(ProfilePlayer plProfile) {
        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                SkyblockLevelMenu.open((SkyblockPlayer) player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                int level = plProfile.level.curLevel;
                int nextLevel = level + 1;
                String curLevelDisplay = "<dark_gray>[" + SkyblockLevel.getLevelColour(level) + level + "<dark_gray>]";
                String nextLevelDisplay = "<dark_gray>[" + SkyblockLevel.getLevelColour(nextLevel) + nextLevel + "<dark_gray>]";

                lore.add(miniMessage().deserialize("<gray>Your SkyBlock Level: " + curLevelDisplay).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Determine how far you've").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>progressed in SkyBlock and earn").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>rewards from completing unique").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>tasks.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Progress to Level " + nextLevelDisplay).decoration(TextDecoration.ITALIC, false));

                int totalBars = 20;
                double percent = plProfile.level.progress.reqProgress == 0 ? 0 : (double) plProfile.level.progress.curProgress / plProfile.level.progress.reqProgress;
                int completed = (int) Math.round(percent * totalBars);
                completed = Math.clamp(completed, 0, totalBars);
                int remaining = totalBars - completed;
                String bar = "<dark_aqua>" + "▬".repeat(completed) + "<white>" + "▬".repeat(remaining) + " <aqua>" + plProfile.level.progress.curProgress + "/" + plProfile.level.progress.reqProgress + " XP";
                lore.add(miniMessage().deserialize(bar).decoration(TextDecoration.ITALIC, false));
                
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStackHead(
                        miniMessage().deserialize("<green>SkyBlock Leveling"),
                        "3255327dd8e90afad681a19231665bea2bd06065a09d77ac1408837f9e0b242",
                        1,
                        lore
                );
            }
        });
    }

    private void setYourBags() {
        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Different bags allow you to store").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>many different items inside!").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStackHead(
                        miniMessage().deserialize("<green>Your Bags"),
                        "961a918c0c49ba8d053e522cb91abc74689367b4d8aa06bfc1ba9154730985ff",
                        1,
                        lore
                );
            }
        });
    }

    private void setPets() {
        set(new GUIClickableItem(30) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>View and manage all of your").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>Pets.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Level up your pets faster by").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>gaining XP in their favourite").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>skill!").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Selected pet: <red>None").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Pets"),
                        Material.BONE,
                        1,
                        lore
                );
            }
        });
    }

    private void setRecipeBook() {
        set(new GUIClickableItem(21) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                GUIRecipeBook.open((SkyblockPlayer) player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Through your adventure, you will").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>unlock recipes for all kinds of").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>special items! You can view how to").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>craft these items here.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());

                int unlocked = ((SkyblockPlayer) player).getActiveProfile().unlockedRecipes.size();
                int total = 50;
                double percent = (double) unlocked / total * 100;
                int bars = (int) Math.round(percent / 5);
                bars = Math.clamp(bars, 0, 20);
                String progressBar = "<yellow>" + "▬".repeat(bars) + "<gray>" + "▬".repeat(20 - bars);

                lore.add(miniMessage().deserialize("<gray>Recipes Unlocked: <yellow>" + unlocked + "/" + total + " <gray>(" + (int) percent + "%)").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize(progressBar).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Recipe Book"),
                        Material.BOOK,
                        1,
                        lore
                );
            }
        });
    }

    private void setStorage() {
        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Store global items that you").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>want to access at any time").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>from anywhere here.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Storage"),
                        Material.CHEST,
                        1,
                        lore
                );
            }
        });
    }

    private void setQuests() {
        set(new GUIClickableItem(23) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                if (player instanceof SkyblockPlayer sp) {
                    new fun.ascent.skyblock.quest.gui.GUIQuestLog(false).open(player);
                }
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Each island has its own series of").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<aqua>Chapters <gray>for you to complete!").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Complete tasks within a Chapter to").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>earn small <gold>rewards<gray>, or complete").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>entire Chapters to earn big ones!").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Some islands also have <green>Quests <gray>for").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>you to complete! Some items can only").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>be obtained through Quests.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Quests & Chapters"),
                        Material.WRITABLE_BOOK,
                        1,
                        lore
                );
            }
        });
    }

    private void setCalendar() {
        set(new GUIClickableItem(24) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>View the SkyBlock Calendar, upcoming").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>events, and event rewards!").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Date: <green>Early Spring 1st").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>No current events.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<dark_gray>Also accessible via /calendar").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Calendar and Events"),
                        Material.CLOCK,
                        1,
                        lore
                );
            }
        });
    }

    private void setSkills() {
        set(new GUIClickableItem(19) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                SkillOverviewMenu.open((SkyblockPlayer) player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>View your Skill progression and").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>rewards.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Your Skills"),
                        Material.DIAMOND_SWORD,
                        1,
                        lore
                );
            }
        });
    }

    private void setCollections() {
        set(new GUIClickableItem(20) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                CollectionOverviewMenu.open((SkyblockPlayer) player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>View all of the items available in").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>SkyBlock. Collect more of an item to").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>unlock rewards on your way to").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>becoming a master of SkyBlock!").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());

                int unlocked = ((SkyblockPlayer) player).getActiveProfile().unlockedCollections.size();
                int total = 50;
                double percent = (double) unlocked / total * 100;
                int bars = (int) Math.round(percent / 5);
                bars = Math.clamp(bars, 0, 20);
                String progressBar = "<yellow>" + "▬".repeat(bars) + "<gray>" + "▬".repeat(20 - bars);

                lore.add(miniMessage().deserialize("<gray>Collections Unlocked: <yellow>" + unlocked + "/" + total + " <gray>(" + (int) percent + "%)").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize(progressBar).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to view!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Collections"),
                        Material.PAINTING,
                        1,
                        lore
                );
            }
        });
    }

    private void setCraftingTable() {
        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                CraftingMenu.open((SkyblockPlayer) player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Opens the crafting grid.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to open!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Crafting Table"),
                        Material.CRAFTING_TABLE,
                        1,
                        lore
                );
            }
        });
    }

    private void setFastTravel() {
        set(new GUIClickableItem(47) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Teleport to islands you've already").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>visited.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<dark_gray>Right-click to warp home!").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<yellow>Click to pick location!"));

                return ItemStackCreator.getStackHead(
                        miniMessage().deserialize("<aqua>Fast Travel"),
                        "f151cffdaf303673531a7651b36637cad912ba485643158e548d59b2ead5011",
                        1,
                        lore
                );
            }
        });
    }

    private void setProfileManagement() {
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>You can have multiple SkyBlock").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>profiles at the same time.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Each profile has its own island,").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>inventory, quest log...").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Profiles: <yellow>" + ((SkyblockPlayer) player).getPlayerProfiles().size() + "<gold>/<yellow>4").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to manage!"));

                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Profile Management"),
                        Material.NAME_TAG,
                        1,
                        lore
                );
            }
        });
    }

    @Override
    public void refreshItems(Player player) {
    }

    @Override
    public int refreshRate() {
        return 20;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}