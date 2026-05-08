package fun.ascent.skyblock.player;

import fun.ascent.skyblock.player.actionbar.ActionBar;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.Stats;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public class SkyblockPlayer extends Player {

    private final Map<UUID, SkyblockProfile> playerProfiles = new HashMap<>();
    private SkyblockProfile activeProfile;
    private ProfilePlayer activeProfileData;

    private double currentHealth;
    private double currentMana;

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
        this.activeProfileData.updateStats();
        this.currentHealth = maxStat(Stats.HEALTH);
        this.currentMana = maxStat(Stats.INTELLIGENCE);
    }

    public void addHealth(double amount) {
        currentHealth = Math.min(currentHealth + amount, maxStat(Stats.HEALTH));
    }

    public void removeHealth(double amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }

    public void addMana(double amount) {
        currentMana = Math.min(currentMana + amount, maxStat(Stats.INTELLIGENCE));
    }

    public boolean consumeMana(double amount) {
        if (currentMana < amount) {
            ActionBar.of(getUuid()).addReplacement(
                    ActionBar.Section.MANA, "<red><bold>NOT ENOUGH MANA", 40, 10);
            return false;
        }
        currentMana -= amount;
        return true;
    }

    public double maxStat(Stats stat) {
        if (activeProfileData == null) return stat.getBaseStat();
        Stat entry = activeProfileData.stats.get(stat.name().toLowerCase());
        return entry != null ? stat.applyCap(entry.getCurValue()) : stat.getBaseStat();
    }

    public double playerStat(Stats stat) {
        if (activeProfileData == null) return stat.getBaseStat();
        Stat entry = activeProfileData.stats.get(stat.name().toLowerCase());
        return entry != null ? entry.getCurValue() : stat.getBaseStat();
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

