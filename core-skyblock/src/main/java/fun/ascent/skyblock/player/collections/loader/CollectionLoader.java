package fun.ascent.skyblock.player.collections.loader;

import fun.ascent.skyblock.config.ConfigPaths;
import fun.ascent.skyblock.player.collections.*;
import net.minestom.server.item.Material;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CollectionLoader {

    private static final File COLLECTIONS_DIR = ConfigPaths.skyblockPath("collections");

    public static List<CollectionCategory> loadAll() {
        List<CollectionCategory> categories = new ArrayList<>();
        if (!COLLECTIONS_DIR.exists()) {
            COLLECTIONS_DIR.mkdirs();
        }

        File[] files = COLLECTIONS_DIR.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null || files.length == 0) {
            System.err.println("[CollectionLoader] No collection files found in " + COLLECTIONS_DIR.getAbsolutePath());
            return categories;
        }

        Yaml yaml = new Yaml(new Constructor(CollectionConfig.class, new LoaderOptions()));

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                CollectionConfig config = yaml.load(reader);
                if (config != null) {
                    categories.add(parseCategory(config));
                }
            } catch (Exception e) {
                System.err.println("[CollectionLoader] Failed to load " + file.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        return categories;
    }

    private static CollectionCategory parseCategory(CollectionConfig config) {
        Material icon = parseMaterial(config.getDisplayIcon());
        CollectionCategory.CollectionType type = parseType(config.getName());

        List<CollectionCategory.ItemCollection> collections = new ArrayList<>();
        if (config.getCollections() != null) {
            for (CollectionConfig.CollectionEntry entry : config.getCollections()) {
                collections.add(parseCollection(entry));
            }
        }

        return new CollectionCategory(config.getName(), icon, type, collections);
    }

    private static CollectionCategory.ItemCollection parseCollection(CollectionConfig.CollectionEntry entry) {
        String itemId = entry.getItemType();
        Material icon = parseMaterial(itemId);
        String name = formatName(itemId);

        List<CollectionCategory.CollectionReward> rewards = new ArrayList<>();
        if (entry.getRewards() != null) {
            for (CollectionConfig.CollectionRewardEntry rewardEntry : entry.getRewards()) {
                List<CollectionUnlock> unlocks = new ArrayList<>();
                if (rewardEntry.getRewards() != null) {
                    for (CollectionConfig.RewardAction action : rewardEntry.getRewards()) {
                        CollectionUnlock unlock = parseUnlock(action);
                        if (unlock != null) unlocks.add(unlock);
                    }
                }
                rewards.add(new CollectionCategory.CollectionReward(rewardEntry.getAmount(), unlocks));
            }
        }

        return new CollectionCategory.ItemCollection(itemId, name, icon, rewards);
    }

    private static CollectionUnlock parseUnlock(CollectionConfig.RewardAction action) {
        if (action.getType() == null) return null;
        String type = action.getType().toUpperCase();
        CollectionConfig.RewardData data = action.getData();

        return switch (type) {
            case "XP" -> new XpUnlock(data.getXp() != null ? data.getXp() : 0);
            case "RECIPE_UNLOCK" -> new RecipeUnlock(data.getUnlockedItemType());
            case "CUSTOM_AWARD" -> new CustomUnlock(data.getCustomAward());
            default -> null;
        };
    }

    private static Material parseMaterial(String name) {
        if (name == null) return Material.AIR;
        String key = name.toLowerCase(Locale.ROOT).replace(" ", "_");
        
        // Try direct key lookup (minecraft:name)
        Material mat = Material.fromKey(key);
        if (mat != null && mat != Material.AIR) return mat;
        
        // Try lookup without prefix
        mat = Material.fromKey("minecraft:" + key);
        if (mat != null && mat != Material.AIR) return mat;

        // Specific fallbacks for Hypixel legacy names
        return switch (key) {
            case "carrot" -> Material.CARROT;
            case "potato" -> Material.POTATO;
            case "wheat" -> Material.WHEAT;
            case "raw_beef" -> Material.BEEF;
            case "raw_chicken" -> Material.CHICKEN;
            case "raw_fish" -> Material.COD;
            case "melon_slice" -> Material.MELON_SLICE;
            case "ink_sack" -> Material.INK_SAC;
            default -> Material.BARRIER;
        };
    }

    private static CollectionCategory.CollectionType parseType(String name) {
        try {
            return CollectionCategory.CollectionType.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return CollectionCategory.CollectionType.OTHER;
        }
    }

    private static String formatName(String id) {
        String[] split = id.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            if (s.isEmpty()) continue;
            sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
