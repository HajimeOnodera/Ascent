package fun.ascent.skyblock.menus.gui.profiles;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.gui.RefreshingGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.UserManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
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
import java.util.Comparator;
import java.util.List;

import static fun.ascent.common.StringUtility.text;
import static fun.ascent.common.StringUtility.commaify;

public class GUIProfileManagement extends InventoryGUI implements RefreshingGUI {

    private static final int[] SLOTS = {11, 12, 13, 14, 15};

    public GUIProfileManagement() {
        super("Profile Management", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getCloseItem(31));

        Player player = e.player();
        refreshItems(player);
    }

    @Override
    public void refreshItems(Player player) {
        if (!(player instanceof SkyblockPlayer sp)) return;

        Rank playerRank = UserManager.getUser(player.getUuid()).getRank();
        int maxAllowed = 2;
        if (playerRank.isEqualOrHigherThan(Rank.ADMIN)) {
            maxAllowed = 5;
        } else if (playerRank.isEqualOrHigherThan(Rank.MVP_PLUS)) {
            maxAllowed = 4;
        } else if (playerRank.isEqualOrHigherThan(Rank.VIP)) {
            maxAllowed = 3;
        }

        List<SkyblockProfile> profilesList = new ArrayList<>(sp.getPlayerProfiles().values());
        profilesList.sort(Comparator.comparing(p -> p.profileName));

        for (int i = 0; i < SLOTS.length; i++) {
            int slot = SLOTS[i];
            
            // Check rank limit locks
            boolean isLocked = false;
            String requiredRankName = "";
            String requiredRankColor = "";

            if (i == 2) {
                isLocked = !playerRank.isEqualOrHigherThan(Rank.VIP);
                requiredRankName = "[VIP]";
                requiredRankColor = "<green>";
            } else if (i == 3) {
                isLocked = !playerRank.isEqualOrHigherThan(Rank.MVP_PLUS);
                requiredRankName = "[MVP<red>+<aqua>]";
                requiredRankColor = "<aqua>";
            } else if (i == 4) {
                isLocked = !playerRank.isEqualOrHigherThan(Rank.ADMIN);
                requiredRankName = "[ADMIN]";
                requiredRankColor = "<red>";
            }

            if (isLocked) {
                final String finalRankColor = requiredRankColor;
                final String finalRankName = requiredRankName;
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, Player pl) {
                        pl.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 0.5f, 0.5f));
                        pl.sendMessage(text("<red>This profile slot is locked! Upgrade your rank to unlock it."));
                    }

                    @Override
                    public ItemStack.Builder getItem(Player pl) {
                        List<Component> lore = new ArrayList<>();
                        lore.add(text("<gray>Unavailable"));
                        lore.add(Component.empty());
                        lore.add(text("<gray>Requires: " + finalRankColor + finalRankName));

                        return ItemStackCreator.getStack("<red>Locked profile slot", Material.BEDROCK, 1, lore);
                    }
                });
            } else if (i < profilesList.size()) {
                SkyblockProfile profile = profilesList.get(i);
                boolean isActive = profile.profileID.equals(sp.getActiveProfile().profileID);
                ProfilePlayer pp = profile.getPlayer(sp);

                if (isActive) {
                    set(new GUIClickableItem(slot) {
                        @Override
                        public void run(InventoryPreClickEvent e, Player pl) {
                            pl.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 0.5f, 0.5f));
                            pl.sendMessage(text("<red>You are already playing on this profile!"));
                        }

                        @Override
                        public ItemStack.Builder getItem(Player pl) {
                            List<Component> lore = new ArrayList<>();
                            lore.add(text("<gray>SkyBlock Level: <gold>" + (pp != null ? pp.level.curLevel : 0)));
                            lore.add(text("<gray>Purse Coins: <gold>" + (pp != null ? commaify(pp.playerCoins) : "0") + " coins"));
                            lore.add(text("<gray>Bank Coins: <gold>" + commaify(profile.bankCoins) + " coins"));
                            lore.add(Component.empty());
                            lore.add(text("<green>Currently playing on this profile!"));

                            return ItemStackCreator.getStack("<green>" + profile.profileName + " <dark_gray>(Active)", Material.EMERALD_BLOCK, 1, lore);
                        }
                    });
                } else {
                    set(new GUIClickableItem(slot) {
                        @Override
                        public void run(InventoryPreClickEvent e, Player pl) {
                            new GUIProfileSelect(profile).open(pl);
                        }

                        @Override
                        public ItemStack.Builder getItem(Player pl) {
                            List<Component> lore = new ArrayList<>();
                            lore.add(text("<gray>SkyBlock Level: <gold>" + (pp != null ? pp.level.curLevel : 0)));
                            lore.add(text("<gray>Purse Coins: <gold>" + (pp != null ? commaify(pp.playerCoins) : "0") + " coins"));
                            lore.add(text("<gray>Bank Coins: <gold>" + commaify(profile.bankCoins) + " coins"));
                            lore.add(Component.empty());
                            lore.add(text("<yellow>Click to switch or manage!"));

                            return ItemStackCreator.getStack("<green>" + profile.profileName, Material.GRASS_BLOCK, 1, lore);
                        }
                    });
                }
            } else {
                // Empty profile slot
                final int finalMax = maxAllowed;
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, Player pl) {
                        if (profilesList.size() >= finalMax) {
                            pl.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 0.5f, 0.5f));
                            pl.sendMessage(text("<red>You have reached the maximum limit of " + finalMax + " profiles!"));
                            return;
                        }
                        new GUIProfileCreate().open(pl);
                    }

                    @Override
                    public ItemStack.Builder getItem(Player pl) {
                        List<Component> lore = new ArrayList<>();
                        lore.add(text("<gray>Create a new classic profile and"));
                        lore.add(text("<gray>start your adventure on a new island!"));
                        lore.add(Component.empty());
                        lore.add(text("<yellow>Click to create a new profile!"));

                        return ItemStackCreator.getStack("<red>Empty Profile Slot", Material.OAK_BUTTON, 1, lore);
                    }
                });
            }
        }
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
