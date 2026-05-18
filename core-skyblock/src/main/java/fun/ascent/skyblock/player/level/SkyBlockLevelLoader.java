package fun.ascent.skyblock.player.level;

import net.minestom.server.item.Material;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.level.unlocks.CustomLevelUnlock;
import fun.ascent.skyblock.player.level.unlocks.SkyBlockLevelStatisticUnlock;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class SkyBlockLevelLoader {

    public static SkyBlockLevelRequirement[] loadFromFile() {
        List<SkyBlockLevelRequirement> requirements = new ArrayList<>();
        File file = new File("./configuration/skyblock/levels/skyblock_level_rewards.yml");
        if (!file.exists()) {
            System.err.println("Could not find skyblock_level_rewards.yml at: " + file.getAbsolutePath());
            return new SkyBlockLevelRequirement[0];
        }

        try (InputStream input = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            if (data == null || !data.containsKey("levels")) {
                return new SkyBlockLevelRequirement[0];
            }

            List<Map<String, Object>> levelsList = (List<Map<String, Object>>) data.get("levels");
            for (Map<String, Object> entry : levelsList) {
                int level = ((Number) entry.getOrDefault("level", 0)).intValue();
                int experience = ((Number) entry.getOrDefault("experience", 100)).intValue();
                boolean isMilestone = (Boolean) entry.getOrDefault("isMilestone", false);
                String prefix = (String) entry.get("prefix");
                String prefixDisplay = (String) entry.get("prefixDisplay");
                String prefixItemStr = (String) entry.get("prefixItem");

                Material prefixItem = Material.AIR;
                if (prefixItemStr != null && !prefixItemStr.equalsIgnoreCase("null")) {
                    String cleanName = prefixItemStr.replace("Material.", "").toLowerCase().trim();
                    Material found = Material.fromKey(cleanName);
                    if (found == null) {
                        found = Material.fromKey("minecraft:" + cleanName);
                    }
                    prefixItem = Objects.requireNonNullElse(found, Material.BONE_MEAL);
                }

                List<SkyBlockLevelUnlock> unlocks = new ArrayList<>();
                List<Map<String, Object>> unlocksList = (List<Map<String, Object>>) entry.get("unlocks");
                if (unlocksList != null) {
                    for (Map<String, Object> unlockEntry : unlocksList) {
                        String type = (String) unlockEntry.get("type");
                        Map<String, Object> unlockData = (Map<String, Object>) unlockEntry.get("data");

                        if ("statistic".equalsIgnoreCase(type) && unlockData != null) {
                            Map<String, Object> statisticsMap = (Map<String, Object>) unlockData.get("statistics");
                            if (statisticsMap != null) {
                                Map<Stats, Double> parsedStats = new HashMap<>();
                                statisticsMap.forEach((statKey, statValObj) -> {
                                    try {
                                        Stats statEnum = Stats.valueOf(statKey.toUpperCase(Locale.ROOT));
                                        if (statValObj instanceof Map) {
                                            Map<String, Object> valMap = (Map<String, Object>) statValObj;
                                            double baseVal = ((Number) valMap.getOrDefault("base", 0.0)).doubleValue();
                                            parsedStats.put(statEnum, baseVal);
                                        }
                                    } catch (IllegalArgumentException ignored) {
                                        // Ignore unknown statistics from older configs
                                    }
                                });
                                if (!parsedStats.isEmpty()) {
                                    unlocks.add(new SkyBlockLevelStatisticUnlock(parsedStats));
                                }
                            }
                        } else if ("custom".equalsIgnoreCase(type) && unlockData != null) {
                            String awardStr = (String) unlockData.get("customAward");
                            if (awardStr != null) {
                                try {
                                    CustomLevelAward award = CustomLevelAward.valueOf(awardStr.toUpperCase(Locale.ROOT));
                                    unlocks.add(new CustomLevelUnlock(award));
                                } catch (IllegalArgumentException ignored) {
                                }
                            }
                        }
                    }
                }

                requirements.add(new SkyBlockLevelRequirement(
                        level, experience, isMilestone, unlocks, prefix, prefixDisplay, prefixItem
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return requirements.toArray(new SkyBlockLevelRequirement[0]);
    }

    public static void initializeCustomLevelAwardCache(SkyBlockLevelRequirement[] requirements) {
        for (SkyBlockLevelRequirement req : requirements) {
            for (SkyBlockLevelUnlock unlock : req.getUnlocks()) {
                if (unlock instanceof CustomLevelUnlock customUnlock) {
                    CustomLevelAward.addToCache(req.getLevel(), customUnlock.getAward());
                }
            }
        }
    }
}
