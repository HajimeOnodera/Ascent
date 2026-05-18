package fun.ascent.skyblock.player.level;

import lombok.Getter;
import net.minestom.server.item.Material;
import fun.ascent.skyblock.player.level.unlocks.SkyBlockLevelStatisticUnlock;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class SkyBlockLevelRequirement {
    private static final Map<Integer, SkyBlockLevelRequirement> LEVELS = new LinkedHashMap<>();
    private static boolean isLoaded = false;

    private final int level;
    private final int experience;
    private final boolean isMilestone;
    private final List<SkyBlockLevelUnlock> unlocks;
    private final String prefix;
    private final String prefixDisplay;
    private final Material prefixItem;

    public SkyBlockLevelRequirement(int level, int experience, boolean isMilestone,
                                    List<SkyBlockLevelUnlock> unlocks, String prefix,
                                    String prefixDisplay, Material prefixItem) {
        this.level = level;
        this.experience = experience;
        this.isMilestone = isMilestone;
        this.unlocks = unlocks != null ? new ArrayList<>(unlocks) : new ArrayList<>();
        this.prefix = prefix;
        this.prefixDisplay = prefixDisplay;
        this.prefixItem = prefixItem;
    }

    public static void loadFromYaml() {
        if (isLoaded) {
            return;
        }

        try {
            SkyBlockLevelRequirement[] levels = SkyBlockLevelLoader.loadFromFile();
            LEVELS.clear();

            for (SkyBlockLevelRequirement lvl : levels) {
                LEVELS.put(lvl.getLevel(), lvl);
            }

            SkyBlockLevelLoader.initializeCustomLevelAwardCache(levels);
            isLoaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SkyBlockLevelRequirement getLevel(int level) {
        ensureLoaded();
        return LEVELS.get(level);
    }

    public static SkyBlockLevelRequirement[] values() {
        ensureLoaded();
        return LEVELS.values().toArray(new SkyBlockLevelRequirement[0]);
    }

    public static Collection<SkyBlockLevelRequirement> getAllLevels() {
        ensureLoaded();
        return LEVELS.values();
    }

    public static int getMaxLevel() {
        ensureLoaded();
        return LEVELS.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    private static void ensureLoaded() {
        if (!isLoaded) {
            loadFromYaml();
        }
    }

    public int getCumulativeExperience() {
        int cumulative = 0;
        for (SkyBlockLevelRequirement requirement : values()) {
            cumulative += requirement.experience;
            if (requirement.level == this.level) {
                return cumulative;
            }
        }
        return 0;
    }

    public SkyBlockLevelRequirement getNextMilestoneLevel() {
        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.isMilestone && requirement.level > this.level) {
                return requirement;
            }
        }
        return null;
    }

    public List<SkyBlockLevelStatisticUnlock> getStatisticUnlocks() {
        return unlocks.stream()
                .filter(unlock -> unlock instanceof SkyBlockLevelStatisticUnlock)
                .map(unlock -> (SkyBlockLevelStatisticUnlock) unlock)
                .collect(Collectors.toList());
    }

    public int asInt() {
        return level;
    }

    public String getColor() {
        return prefix != null ? prefix : "§7";
    }

    public @Nullable SkyBlockLevelRequirement getNextLevel() {
        return getLevel(level + 1);
    }

    @Override
    public String toString() {
        return String.valueOf(level);
    }

    public Map<SkyBlockLevelRequirement, String> getPreviousPrefixChanges() {
        Map<SkyBlockLevelRequirement, String> toReturn = new HashMap<>();

        SkyBlockLevelRequirement last = getLevel(0);
        if (last == null) return toReturn;

        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.level < this.level && !requirement.prefix.equals(last.prefix)) {
                toReturn.put(requirement, requirement.prefix);
            }
            last = requirement;
        }

        return toReturn;
    }

    public Map.Entry<SkyBlockLevelRequirement, String> getNextPrefixChange() {
        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.level > this.level && !requirement.prefix.equals(this.prefix)) {
                return Map.entry(requirement, requirement.prefix);
            }
        }
        return null;
    }

    public int getExperienceOfAllPreviousLevels() {
        return Arrays.stream(values())
                .filter(requirement -> requirement.level < this.level)
                .mapToInt(SkyBlockLevelRequirement::getExperience)
                .sum();
    }

    public static Map<SkyBlockLevelRequirement, String> getAllPrefixChanges() {
        ensureLoaded();
        Map<SkyBlockLevelRequirement, String> toReturn = new HashMap<>();

        SkyBlockLevelRequirement last = getLevel(0);
        if (last == null) return toReturn;

        for (SkyBlockLevelRequirement requirement : values()) {
            if (!requirement.prefix.equals(last.prefix)) {
                toReturn.put(requirement, requirement.prefix);
            }
            last = requirement;
        }

        return toReturn;
    }

    public static SkyBlockLevelRequirement getFromTotalXP(double xp) {
        ensureLoaded();
        SkyBlockLevelRequirement toReturn = getLevel(0);
        if (toReturn == null) return null;

        for (SkyBlockLevelRequirement requirement : values()) {
            if (xp < requirement.experience) {
                return toReturn;
            } else {
                toReturn = requirement;
            }
        }
        return toReturn;
    }

    public static void reload() {
        isLoaded = false;
        LEVELS.clear();
        loadFromYaml();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SkyBlockLevelRequirement that = (SkyBlockLevelRequirement) obj;
        return level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level);
    }

    public static boolean levelExists(int level) {
        ensureLoaded();
        return LEVELS.containsKey(level);
    }

    public static SkyBlockLevelRequirement getLevelSafe(int level) {
        SkyBlockLevelRequirement result = getLevel(level);
        if (result == null) {
            return getLevel(getMaxLevel());
        }
        return result;
    }
}
