package fun.ascent.skyblock.menus.gui.profiles;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class GUIProfileCreate extends InventoryGUI {

    public GUIProfileCreate() {
        super("Create New Profile", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getGoBackItem(22, new GUIProfileManagement()));

        Player player = e.player();
        if (!(player instanceof SkyblockPlayer sp)) return;

        // Confirm button
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;

                // 1. Save current active profile to persist all its items and stats
                if (sp.getActiveProfile() != null) {
                    ProfileManager.saveProfile(sp.getActiveProfile().profileID);
                }

                // 2. Create the profile natively (automatically registers, saves, and sets active)
                SkyblockProfile newProfile = ProfileManager.createProfile(sp);

                pl.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1f, 1f));
                pl.sendMessage(text("<green>Created new profile: <gold>" + newProfile.profileName));
                pl.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Create a new classic profile and start"));
                lore.add(text("<gray>your adventure on a new island!"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to confirm creation!"));

                return ItemStackCreator.getStack("<green>Confirm Profile Creation", Material.GREEN_TERRACOTTA, 1, lore);
            }
        });

        // Cancel button
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                new GUIProfileManagement().open(pl);
            }

            @Override
            public ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Go back to Profile Management"));
                lore.add(text("<gray>without creating a new profile."));
                lore.add(Component.empty());
                lore.add(text("<red>Click to cancel!"));

                return ItemStackCreator.getStack("<red>Cancel Creation", Material.RED_TERRACOTTA, 1, lore);
            }
        });
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
