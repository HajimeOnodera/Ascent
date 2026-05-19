package fun.ascent.skyblock.player;

import fun.ascent.database.PlayerRepository;
import fun.ascent.skyblock.entity.display.DroppedItemEntity;
import fun.ascent.skyblock.hotm.HotmData;
import fun.ascent.skyblock.player.actionbar.ActionBar;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.Stats;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
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

    private final Map<String, Map<Stats, Double>> statEffects = new HashMap<>();
    private final Map<String, Task> effectTasks = new HashMap<>();
    private final Map<String, Long> abilityCooldowns = new HashMap<>();

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
        ProfileManager.loadProfilesForPlayer(this);
        String lastProfileId = PlayerRepository.getField(getUuid(), "skyblock.last_profile_id", null);
        if (lastProfileId != null) {
            setActiveProfile(UUID.fromString(lastProfileId));
        } else if (!playerProfiles.isEmpty()) {
            setActiveProfile(playerProfiles.keySet().iterator().next());
        }
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
                this.activeProfileData.skyblockPlayer = this; // Ensure transient ref is set
                
                // Sync data from handler to player (restores inventory, coins, etc.)
                pp.getDataHandler().syncToPlayer(this);

                System.out.println("Profile Set Correctly");
                updatePlayer();
                this.currentHealth = maxStat(Stats.HEALTH);
                this.currentMana = maxStat(Stats.INTELLIGENCE);
                break;
            }
            System.out.println("Not Right UUID: " + pp.playerUUID);
        }
        setTag(sbProfileID, this.activeProfile.profileID);
        PlayerRepository.setField(getUuid(), "skyblock.last_profile_id", profileID.toString());
    }

    public void updatePlayer() {
        if (this.activeProfile == null) return;
        this.activeProfileData.updateStats();
        this.currentHealth = Math.min(currentHealth, maxStat(Stats.HEALTH));
        this.currentMana = Math.min(currentMana, maxStat(Stats.INTELLIGENCE));
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
        double base = entry != null ? entry.getCurValue() : stat.getBaseStat();
        double bonus = statEffects.values().stream()
                .mapToDouble(bonuses -> bonuses.getOrDefault(stat, 0.0))
                .sum();
        return stat.applyCap(base + bonus);
    }

    public void applyEffect(String effectId, Map<Stats, Double> bonuses, int durationSeconds) {
        Task existing = effectTasks.remove(effectId);
        if (existing != null) existing.cancel();
        statEffects.put(effectId, bonuses);
        updatePlayer();
        Task task = MinecraftServer.getSchedulerManager()
                .buildTask(() -> {
                    statEffects.remove(effectId);
                    effectTasks.remove(effectId);
                    updatePlayer();
                })
                .delay(TaskSchedule.seconds(durationSeconds))
                .schedule();
        effectTasks.put(effectId, task);
    }

    public boolean startCooldown(String key, int seconds) {
        long now = System.currentTimeMillis();
        Long expires = abilityCooldowns.get(key);
        if (expires != null && now < expires) return false;
        abilityCooldowns.put(key, now + seconds * 1000L);
        return true;
    }

    public int getCooldownRemaining(String key) {
        Long expires = abilityCooldowns.get(key);
        if (expires == null) return 0;
        return Math.max(0, (int) Math.ceil((expires - System.currentTimeMillis()) / 1000.0));
    }

    public double playerStat(Stats stat) {
        if (activeProfileData == null) return stat.getBaseStat();
        Stat entry = activeProfileData.stats.get(stat.name().toLowerCase());
        double base = entry != null ? entry.getCurValue() : stat.getBaseStat();
        double bonus = statEffects.values().stream()
                .mapToDouble(bonuses -> bonuses.getOrDefault(stat, 0.0))
                .sum();
        return base + bonus;
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

    public void setCoins(double coins) { if (activeProfileData != null) activeProfileData.playerCoins = coins; }
    public double getCoins() { return activeProfileData != null ? activeProfileData.playerCoins : 0; }
    public void setBits(double bits) { if (activeProfileData != null) activeProfileData.playerBits = bits; }
    public double getBits() { return activeProfileData != null ? activeProfileData.playerBits : 0; }
    public void setGold(double gold) { if (activeProfileData != null) activeProfileData.playerGold = gold; }
    public double getGold() { return activeProfileData != null ? activeProfileData.playerGold : 0; }
    public void setSkyblockLevel(SkyblockLevel level) { if (activeProfileData != null) activeProfileData.level = level; }
    public SkyblockLevel getSkyblockLevel() { return activeProfileData != null ? activeProfileData.level : null; }
    public void setSkyblockHotmData(HotmData data) { if (activeProfileData != null) activeProfileData.hotmData = data; }
    public HotmData getSkyblockHotmData() { return activeProfileData != null ? activeProfileData.hotmData : null; }

    // ─── Drop Alert Toggle ───────────────────────────────────────────
    private static final Tag<Boolean> DROP_ALERTS_DISABLED = Tag.Boolean("drop_alerts_disabled").defaultValue(false);

    public boolean hasDropAlertsDisabled() {
        Boolean val = getTag(DROP_ALERTS_DISABLED);
        return val != null && val;
    }

    public void setDropAlertsDisabled(boolean disabled) {
        setTag(DROP_ALERTS_DISABLED, disabled);
    }

    // ─── Coins Helper ────────────────────────────────────────────────
    public void addCoins(double amount) {
        if (activeProfileData != null) {
            activeProfileData.playerCoins += amount;
        }
    }

    // ─── Cleanup on disconnect ───────────────────────────────────────
    public void cleanupDroppedItems() {
        DroppedItemEntity.clearDroppedItems(this);
    }
}
