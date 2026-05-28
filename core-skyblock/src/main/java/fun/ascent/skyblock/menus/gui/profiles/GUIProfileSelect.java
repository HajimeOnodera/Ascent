package fun.ascent.skyblock.menus.gui.profiles;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.database.SkyblockRepository;
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

public class GUIProfileSelect extends InventoryGUI {

    private final SkyblockProfile profile;

    public GUIProfileSelect(SkyblockProfile profile) {
        super("Manage Profile: " + profile.profileName, InventoryType.CHEST_4_ROW);
        this.profile = profile;
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getGoBackItem(30, new GUIProfileManagement()));
        set(GUIClickableItem.getCloseItem(31));

        Player player = e.player();
        if (!(player instanceof SkyblockPlayer sp)) return;

        // Switch profile item
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;

                // 1. Save the current active profile to persist all its items and stats
                if (sp.getActiveProfile() != null) {
                    ProfileManager.saveProfile(sp.getActiveProfile().profileID);
                }

                // 2. Switch active profile (natively handles item clearing and loading)
                sp.setActiveProfile(profile.profileID);

                pl.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1f, 1.2f));
                pl.sendMessage(text("<green>Switched to profile: <gold>" + profile.profileName));
                pl.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Switch your current active profile to"));
                lore.add(text("<green>" + profile.profileName + "<gray>. Your current inventory and"));
                lore.add(text("<gray>progression will be securely saved first."));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to switch profile!"));

                return ItemStackCreator.getStack("<green>Switch to Profile", Material.GRASS_BLOCK, 1, lore);
            }
        });

        // Delete profile item
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;

                // Check that they aren't deleting their last remaining profile
                if (sp.getPlayerProfiles().size() <= 1) {
                    pl.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 0.5f, 0.5f));
                    pl.sendMessage(text("<red>You cannot delete your last remaining profile!"));
                    return;
                }

                // Check that they are not trying to delete the active profile (handled by management GUI, but extra safe)
                if (profile.profileID.equals(sp.getActiveProfile().profileID)) {
                    pl.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 0.5f, 0.5f));
                    pl.sendMessage(text("<red>You cannot delete your active profile!"));
                    return;
                }

                // Delete profile completely
                SkyblockRepository.deleteProfile(profile.profileID);
                sp.getPlayerProfiles().remove(profile.profileID);
                ProfileManager.profiles.remove(profile.profileID);

                pl.playSound(Sound.sound(Key.key("entity.generic.explode"), Sound.Source.PLAYER, 0.8f, 1.0f));
                pl.sendMessage(text("<red>Deleted profile: <gold>" + profile.profileName));

                // Reopen profile management
                new GUIProfileManagement().open(pl);
            }

            @Override
            public ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<red>WARNING: THIS ACTION IS PERMANENT!"));
                lore.add(text("<gray>All island structures, items, skills,"));
                lore.add(text("<gray>and coins on <red>" + profile.profileName + " <gray>will be"));
                lore.add(text("<gray>completely deleted forever."));
                lore.add(Component.empty());
                lore.add(text("<red>Click to delete profile!"));

                return ItemStackCreator.getStack("<red>Delete Profile", Material.RED_STAINED_GLASS, 1, lore);
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
