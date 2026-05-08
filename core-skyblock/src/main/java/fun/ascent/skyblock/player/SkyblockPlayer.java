package fun.ascent.skyblock.player;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.PlayerSlots;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.actionbar.ActionBar;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.stats.StatMap;
import fun.ascent.skyblock.player.stats.Stats;
import lombok.Getter;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SkyblockPlayer extends Player {

    @Getter private final Map<UUID, SkyblockProfile> playerProfiles = new HashMap<>();
    @Getter private SkyblockProfile activeProfile;
    @Getter private ProfilePlayer activeProfileData;

    private double currentHealth;
    private double currentMana;

    private final StatMap itemStats = new StatMap();

    public static final Tag<UUID> sbProfileID = Tag.UUID("profile_id");

    public SkyblockPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        this.currentHealth = Stats.HEALTH.getBaseStat();
        this.currentMana = Stats.INTELLIGENCE.getBaseStat();
        loadProfiles();
        if (playerProfiles.isEmpty()) {
            SkyblockProfile profile = ProfileManager.createProfile(this);
            addProfile(profile);
        }
    }

    @Override
    public boolean damage(@NotNull Damage damage) {
        return false;
    }

    public SkyblockProfile loadProfileFromPlayer() {
        return ProfileManager.getProfile(this.getTag(sbProfileID));
    }

    public void loadProfiles() {
        // TODO: Load from database
    }

    public void addProfile(SkyblockProfile profile) {
        System.out.println(profile.profileName + " : " + profile.profileID);
        playerProfiles.put(profile.profileID, profile);
    }

    public void setActiveProfile(UUID profileID) {
        System.out.println(playerProfiles.size());
        SkyblockProfile target = playerProfiles.get(profileID);
        if (target == null) return;
        this.activeProfile = target;
        System.out.println("UUID to Match: " + this.getUuid());
        for (ProfilePlayer pp : target.profilePlayers) {
            if (pp.playerUUID.equals(getUuid())) {
                this.activeProfileData = pp;
                System.out.println("Profile Set Correctly");
                updatePlayer();
                break;
            }
            System.out.println("Not Right UUID: " + pp.playerUUID);
        }
        setTag(sbProfileID, this.activeProfile.profileID);
    }

    public void updatePlayer() {
        if (this.activeProfile == null) return;
        recalculateItemStats();
        this.activeProfileData.updateStats();
        this.currentHealth = stat(Stats.HEALTH);
        this.currentMana = stat(Stats.INTELLIGENCE);
    }

    public ItemStack getSlot(PlayerSlots slot) {
        return equipmentSlots[slot.ordinal()];
    }

    public void setSlot(PlayerSlots slot, ItemStack stack) {
        equipmentSlots[slot.ordinal()] = stack == null ? ItemStack.AIR : stack;
        recalculateItemStats();
    }

    public void recalculateItemStats() {
        itemStats.clearAdditive();
        for (PlayerSlots slot : PlayerSlots.values()) {
            ItemStack stack = resolveSlotItem(slot);
            applyEquipmentStats(stack);
        }
    }

    private ItemStack resolveSlotItem(PlayerSlots slot) {
        return switch (slot) {
            case Helmet -> getEquipment(EquipmentSlot.HELMET);
            case Chestplate -> getEquipment(EquipmentSlot.CHESTPLATE);
            case Leggings -> getEquipment(EquipmentSlot.LEGGINGS);
            case Boots -> getEquipment(EquipmentSlot.BOOTS);
            case HeldItem -> getEquipment(EquipmentSlot.MAIN_HAND);
        };
    }

    private void applyEquipmentStats(ItemStack stack) {
        if (stack.isAir()) return;
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;
        String itemId = customData.nbt().getString("id");
        if (itemId.isEmpty()) return;

        SkyblockItem sbItem = ItemRegistry.getItem(itemId);
        if (sbItem == null) return;

        itemStats.applyItemStats(sbItem.getBaseStats());
    }

    public double stat(Stats stat) {
        if (activeProfileData == null) return stat.getBaseStat();
        double base = activeProfileData.stats.get(stat);
        double item = itemStats.getAdditive(stat);
        return stat.applyCap(base + item);
    }

    public double getCurrentHealth() { return currentHealth; }

    public void addHealth(double amount) {
        currentHealth = Math.min(currentHealth + amount, stat(Stats.HEALTH));
    }

    public void removeHealth(double amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }

    public double getCurrentMana() { return currentMana; }

    public void addMana(double amount) {
        currentMana = Math.min(currentMana + amount, stat(Stats.INTELLIGENCE));
    }

    public boolean consumeMana(double amount) {
        if (currentMana < amount) {
            ActionBar.of(getUuid()).addReplacement(
                    ActionBar.Section.MANA, "§c§lNOT ENOUGH MANA", 40, 10);
            return false;
        }
        currentMana -= amount;
        return true;
    }

    @Deprecated
    public double maxStat(Stats stat) {
        return stat(stat);
    }

    @Deprecated
    public double playerStat(Stats stat) {
        return stat(stat);
    }

    @Nullable
    public PlayerSkillData getSkillData() {
        return activeProfileData != null ? activeProfileData.skillData : null;
    }

    public ProfilePlayer getPlayerProfile(SkyblockProfile profile) {
        for (ProfilePlayer pp : profile.profilePlayers) {
            if (pp.playerUUID.equals(getUuid())) return pp;
        }
        return null;
    }
}
