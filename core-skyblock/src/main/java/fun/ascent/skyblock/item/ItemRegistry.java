package fun.ascent.skyblock.item;

import fun.ascent.database.ItemRepository;
import fun.ascent.skyblock.item.api.HypixelItemFetcher;
import fun.ascent.skyblock.item.items.ItemDefinition;
import fun.ascent.skyblock.item.items.ItemDefinitions;
import fun.ascent.skyblock.item.gemstone.GemstoneSlot;
import fun.ascent.skyblock.item.gemstone.GemstoneSlotType;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.color.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import net.minestom.server.item.Material;
import net.minestom.server.item.ItemStack;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class ItemRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRegistry.class);
    private static final Map<String, SkyblockItem> ITEMS = new HashMap<>();

    private static Material safeGetMaterial(String key) {
        if (key == null) return null;
        try {
            return Material.fromKey(key);
        } catch (Exception e) {
            return null;
        }
    }
    private static final Map<String, Boolean> YAML_STACKABLE_OVERRIDES = new HashMap<>();

    public static void loadYamlOverrides() {
        File folder = new File("configuration/skyblock/items");
        if (folder.exists() && folder.isDirectory()) {
            loadYamlOverridesRecursively(folder);
        }
        LOGGER.info("Loaded {} item stackability overrides from YAML configs.", YAML_STACKABLE_OVERRIDES.size());
    }

    private static void loadYamlOverridesRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                loadYamlOverridesRecursively(file);
            } else if (file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    Yaml yaml = new Yaml();
                    Map<String, Object> data = yaml.load(inputStream);
                    if (data != null && data.containsKey("items")) {
                        Object itemsObj = data.get("items");
                        if (itemsObj instanceof List<?> itemsList) {
                            for (Object itemObj : itemsList) {
                                if (itemObj instanceof Map<?, ?> itemMap) {
                                    String id = (String) itemMap.get("id");
                                    if (id != null) {
                                        Object componentsObj = itemMap.get("components");
                                        if (componentsObj instanceof List<?> componentsList) {
                                            for (Object compObj : componentsList) {
                                                if (compObj instanceof Map<?, ?> compMap) {
                                                    Object compId = compMap.get("id");
                                                    if ("STACKABLE".equals(compId)) {
                                                        YAML_STACKABLE_OVERRIDES.put(id.toUpperCase(), false);
                                                    } else if ("UNSTACKABLE".equals(compId)) {
                                                        YAML_STACKABLE_OVERRIDES.put(id.toUpperCase(), true);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to load YAML item config: {}", file.getName(), e);
                }
            }
        }
    }

    public static String formatName(String id) {
        if (id == null) return "";
        String[] words = id.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.isEmpty()) continue;
            sb.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                sb.append(word.substring(1).toLowerCase(Locale.ROOT));
            }
            if (i < words.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static void loadYamlItems() {
        File folder = new File("configuration/skyblock/items");
        if (folder.exists() && folder.isDirectory()) {
            loadYamlItemsRecursively(folder);
        }
        LOGGER.info("Registered custom items from YAML configs. Total items in registry: {}", ITEMS.size());
    }

    private static void loadYamlItemsRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                loadYamlItemsRecursively(file);
            } else if (file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    Yaml yaml = new Yaml();
                    Map<String, Object> data = yaml.load(inputStream);
                    if (data != null && data.containsKey("items")) {
                        Object itemsObj = data.get("items");
                        if (itemsObj instanceof List<?> itemsList) {
                            for (Object itemObj : itemsList) {
                                if (itemObj instanceof Map<?, ?> itemMap) {
                                    String id = (String) itemMap.get("id");
                                    String matStr = (String) itemMap.get("material");
                                    String rarityStr = (String) itemMap.get("rarity");
                                    
                                    if (id != null && matStr != null) {
                                        if (matStr.startsWith("minecraft:")) {
                                            matStr = matStr.substring(10);
                                        }
                                        Material material = safeGetMaterial("minecraft:" + matStr.toLowerCase(Locale.ROOT));
                                        if (material == null) {
                                            material = safeGetMaterial(matStr.toLowerCase(Locale.ROOT));
                                        }
                                        if (material == null) {
                                            LOGGER.warn("Skipping item {} because material {} is invalid.", id, matStr);
                                            continue;
                                        }
                                        
                                        Rarity rarity = Rarity.COMMON;
                                        if (rarityStr != null) {
                                            try {
                                                rarity = Rarity.valueOf(rarityStr.toUpperCase(Locale.ROOT));
                                            } catch (IllegalArgumentException ignored) {}
                                        }
                                        
                                        SkyblockItem.Builder builder = SkyblockItem.builder(id, material, rarity);
                                        
                                        // 1. Determine Display Name
                                        String displayName = formatName(id);
                                        
                                        // 2. Parse components
                                        ItemType itemType = ItemType.NONE;
                                        Boolean unstackable = null;
                                        
                                        Object componentsObj = itemMap.get("components");
                                        if (componentsObj instanceof List<?> componentsList) {
                                            for (Object compObj : componentsList) {
                                                if (compObj instanceof Map<?, ?> compMap) {
                                                    Object compId = compMap.get("id");
                                                    if (compId instanceof String compIdStr) {
                                                        switch (compIdStr) {
                                                            case "CUSTOM_DISPLAY_NAME" -> {
                                                                Object customName = compMap.get("display_name");
                                                                if (customName instanceof String) {
                                                                    displayName = (String) customName;
                                                                }
                                                            }
                                                            case "STACKABLE" -> unstackable = false;
                                                            case "UNSTACKABLE" -> unstackable = true;
                                                            default -> {
                                                                try {
                                                                    itemType = ItemType.valueOf(compIdStr.toUpperCase(Locale.ROOT));
                                                                } catch (IllegalArgumentException ignored) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        
                                        builder.displayName(displayName);
                                        builder.itemType(itemType);
                                        if (unstackable != null) {
                                            builder.unstackable(unstackable);
                                        }
                                        
                                        // 3. Parse stats
                                        Object statsObj = itemMap.get("default_statistics");
                                        if (statsObj instanceof Map<?, ?> statsMap) {
                                            for (Map.Entry<?, ?> entry : statsMap.entrySet()) {
                                                if (entry.getKey() instanceof String statKey && entry.getValue() instanceof Number statVal) {
                                                    try {
                                                        Stats stat = Stats.valueOf(statKey.toUpperCase(Locale.ROOT));
                                                        builder.stat(stat, statVal.doubleValue());
                                                    } catch (IllegalArgumentException ignored) {}
                                                }
                                            }
                                        }
                                        
                                        // 4. Overrides/definitions applying
                                        ItemDefinition def = ItemDefinitions.get(id);
                                        if (def != null) {
                                            builder = def.apply(builder);
                                        }
                                        
                                        SkyblockItem item = builder.build();
                                        ITEMS.put(id, item);
                                        ITEMS.put(id.toUpperCase(Locale.ROOT), item);
                                        ITEMS.put(id.toLowerCase(Locale.ROOT), item);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to register custom item from YAML config: {}", file.getName(), e);
                }
            }
        }
    }

    public static void init() {
        try {
            loadYamlOverrides();
            long count = ItemRepository.getItemCount();
            if (count > 0) {
                LOGGER.info("Loading items from MongoDB cache...");
                List<Document> docs = ItemRepository.getAllItems();
                List<Document> docsToUpdate = new ArrayList<>();
                for (Document doc : docs) {
                    SkyblockItemData data = deserializeData(doc);
                    
                    // Apply YAML override check to see if database needs sync
                    Boolean overrideUnstackable = YAML_STACKABLE_OVERRIDES.get(data.id().toUpperCase());
                    if (overrideUnstackable != null && overrideUnstackable != data.unstackable()) {
                        // DB needs to be updated with the YAML override
                        data = new SkyblockItemData(
                                data.id(), data.name(), data.material(), data.rarity(), data.itemType(),
                                data.skinValue(), data.npcSellPrice(), data.stats(), data.gemstoneSlotTypes(),
                                data.soulbound(), data.dungeon(), data.glowing(), overrideUnstackable,
                                data.color(), data.description(), data.itemModel()
                        );
                        docsToUpdate.add(serializeData(data));
                    }
                    
                    ITEMS.put(data.id(), convert(data));
                }
                if (!docsToUpdate.isEmpty()) {
                    LOGGER.info("Updating {} items in MongoDB with YAML overrides...", docsToUpdate.size());
                    ItemRepository.saveItems(docsToUpdate);
                }
                LOGGER.info("Loaded {} items from MongoDB.", ITEMS.size());
            } else {
                LOGGER.info("MongoDB cache empty, fetching items from Hypixel API...");
                var rawItems = HypixelItemFetcher.fetchItems();
                List<Document> docsToSave = new ArrayList<>();
                for (SkyblockItemData data : rawItems) {
                    // Apply YAML override to the newly fetched items
                    Boolean overrideUnstackable = YAML_STACKABLE_OVERRIDES.get(data.id().toUpperCase());
                    if (overrideUnstackable != null) {
                        data = new SkyblockItemData(
                                data.id(), data.name(), data.material(), data.rarity(), data.itemType(),
                                data.skinValue(), data.npcSellPrice(), data.stats(), data.gemstoneSlotTypes(),
                                data.soulbound(), data.dungeon(), data.glowing(), overrideUnstackable,
                                data.color(), data.description(), data.itemModel()
                        );
                    }
                    ITEMS.put(data.id(), convert(data));
                    docsToSave.add(serializeData(data));
                }
                ItemRepository.saveItems(docsToSave);
                LOGGER.info("Loaded and cached {} items from Hypixel API.", ITEMS.size());
            }
            // Register YAML custom items now!
            loadYamlItems();
        } catch (Exception e) {
            LOGGER.error("Failed to load items: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private static Document serializeData(SkyblockItemData data) {
        Document doc = new Document();
        doc.put("_id", data.id());
        doc.put("name", data.name());
        doc.put("material", data.material().name().toLowerCase());
        doc.put("rarity", data.rarity().name());
        doc.put("itemType", data.itemType().name());
        doc.put("skinValue", data.skinValue());
        doc.put("npcSellPrice", data.npcSellPrice());
        doc.put("stats", new Document(data.stats()));
        doc.put("gemstoneSlotTypes", data.gemstoneSlotTypes());
        doc.put("soulbound", data.soulbound());
        doc.put("dungeon", data.dungeon());
        doc.put("glowing", data.glowing());
        doc.put("unstackable", data.unstackable());
        doc.put("color", data.color());
        doc.put("description", data.description());
        doc.put("itemModel", data.itemModel());
        return doc;
    }

    private static SkyblockItemData deserializeData(org.bson.Document doc) {
        String materialStr = doc.getString("material");
        if (materialStr != null && materialStr.startsWith("minecraft:")) {
            materialStr = materialStr.substring(10);
        }

        Material material = materialStr != null ? safeGetMaterial(materialStr) : Material.PAPER;
        if (material == null) material = Material.PAPER;

        Boolean unstackableVal = doc.getBoolean("unstackable");
        boolean unstackable = unstackableVal != null ? unstackableVal : false;

        return new SkyblockItemData(
                doc.getString("_id"),
                doc.getString("name"),
                material,
                Rarity.valueOf(doc.getString("rarity")),
                ItemType.valueOf(doc.getString("itemType")),
                doc.getString("skinValue"),
                doc.getDouble("npcSellPrice"),
                (Map) doc.get("stats", org.bson.Document.class),
                doc.getList("gemstoneSlotTypes", String.class),
                doc.getString("soulbound"),
                doc.getBoolean("dungeon"),
                doc.getBoolean("glowing"),
                unstackable,
                doc.getString("color"),
                doc.getString("description"),
                doc.getString("itemModel")
        );
    }

    private static SkyblockItem convert(SkyblockItemData data) {
        Boolean overrideUnstackable = YAML_STACKABLE_OVERRIDES.get(data.id().toUpperCase());
        boolean isUnstackable = overrideUnstackable != null ? overrideUnstackable : data.unstackable();

        SkyblockItem.Builder builder = SkyblockItem.builder(data.id(), data.material(), data.rarity())
                .displayName(data.name())
                .itemType(data.itemType())
                .npcSellPrice(data.npcSellPrice())
                .glowing(data.glowing())
                .unstackable(isUnstackable);

        if (data.hasSkin()) {
            builder.skinValue(data.skinValue());
        }

        if (data.description() != null) {
            builder.description(data.description());
        }

        builder.dungeon(data.dungeon());

        if ("SOLO".equals(data.soulbound())) {
            builder.soulbound(true);
        } else if ("COOP".equals(data.soulbound())) {
            builder.coopSoulbound(true);
        }

        for (Map.Entry<String, Double> entry : data.stats().entrySet()) {
            try {
                builder.stat(Stats.valueOf(entry.getKey()), entry.getValue());
            } catch (IllegalArgumentException ignored) {}
        }

        if (data.color() != null) {
            try {
                String[] parts = data.color().split(",");
                builder.armorColor(new Color(
                        Integer.parseInt(parts[0].trim()),
                        Integer.parseInt(parts[1].trim()),
                        Integer.parseInt(parts[2].trim())));
            } catch (Exception ignored) {}
        }

        for (String slotType : data.gemstoneSlotTypes()) {
            try {
                GemstoneSlotType type = GemstoneSlotType.valueOf(slotType);
                builder.gemstoneSlot(new GemstoneSlot(type, false));
            } catch (IllegalArgumentException ignored) {}
        }

        ItemDefinition def = ItemDefinitions.get(data.id());
        if (def != null) {
            builder = def.apply(builder);
        }

        return builder.build();
    }

    public static String canonicalizeId(String id) {
        if (id == null) return null;
        String upper = id.toUpperCase();
        return switch (upper) {
            case "OAK_LOG", "OAK_WOOD" -> "LOG";
            case "SPRUCE_LOG", "SPRUCE_WOOD" -> "LOG:1";
            case "BIRCH_LOG", "BIRCH_WOOD" -> "LOG:2";
            case "JUNGLE_LOG", "JUNGLE_WOOD" -> "LOG:3";
            case "ACACIA_LOG", "ACACIA_WOOD" -> "LOG_2";
            case "DARK_OAK_LOG", "DARK_OAK_WOOD" -> "LOG_2:1";
            default -> id;
        };
    }

    public static SkyblockItem getItem(String id) {
        if (id == null) return null;
        String resolvedId = canonicalizeId(id);
        SkyblockItem item = ITEMS.get(resolvedId);
        if (item != null) return item;
        item = ITEMS.get(resolvedId.toUpperCase());
        if (item != null) return item;
        item = ITEMS.get(resolvedId.toLowerCase());
        if (item != null) return item;

        String cleanId = resolvedId.toUpperCase(Locale.ROOT).replace("MINECRAFT:", "");
        Material material = safeGetMaterial("minecraft:" + cleanId.toLowerCase(Locale.ROOT));
        if (material == null) {
            material = safeGetMaterial(cleanId.toLowerCase(Locale.ROOT));
        }
        if (material != null && material != Material.AIR) {
            SkyblockItem fallbackItem = SkyblockItem.builder(cleanId, material, Rarity.COMMON)
                    .displayName(formatName(cleanId))
                    .build();
            ITEMS.put(cleanId, fallbackItem);
            ITEMS.put(cleanId.toUpperCase(Locale.ROOT), fallbackItem);
            ITEMS.put(cleanId.toLowerCase(Locale.ROOT), fallbackItem);
            return fallbackItem;
        }

        return null;
    }

    public static SkyblockItem getItemByMaterial(Material material) {
        if (material == null || material == Material.AIR) return null;
        String matName = material.name().toUpperCase().replace("MINECRAFT:", "");
        
        // 1. Explicit Skyblock special mappings
        String targetId = switch (matName) {
            case "WHEAT_SEEDS" -> "SEEDS";
            case "CARROT", "CARROTS" -> "CARROT";
            case "POTATO", "POTATOES" -> "POTATO";
            case "MELON" -> "MELON_BLOCK";
            case "COD" -> "RAW_FISH";
            case "SALMON" -> "RAW_SALMON";
            case "STONE" -> "COBBLESTONE";
            case "GLOWSTONE" -> "GLOWSTONE_DUST";
            case "OAK_LOG", "OAK_WOOD" -> "LOG";
            case "SPRUCE_LOG", "SPRUCE_WOOD" -> "LOG:1";
            case "BIRCH_LOG", "BIRCH_WOOD" -> "LOG:2";
            case "JUNGLE_LOG", "JUNGLE_WOOD" -> "LOG:3";
            case "ACACIA_LOG", "ACACIA_WOOD" -> "LOG_2";
            case "DARK_OAK_LOG", "DARK_OAK_WOOD" -> "LOG_2:1";
            default -> null;
        };
        
        if (targetId != null) {
            SkyblockItem item = getItem(targetId);
            if (item != null) return item;
        }
        
        // 2. Try the material name directly (ensure it's not an enchanted item)
        SkyblockItem itemNameMatch = ITEMS.get(matName);
        if (itemNameMatch == null) itemNameMatch = ITEMS.get(matName.toUpperCase());
        if (itemNameMatch == null) itemNameMatch = ITEMS.get(matName.toLowerCase());
        
        if (itemNameMatch != null) {
            if (!itemNameMatch.getItemId().toUpperCase().startsWith("ENCHANTED_")) {
                return itemNameMatch;
            }
        }
        
        // 3. Fallback: Search all registered Skyblock items by matching material
        // Prioritize exact match of item ID to material name first (ignoring enchanted)
        for (SkyblockItem item : ITEMS.values()) {
            if (item.getItemId().toUpperCase().startsWith("ENCHANTED_")) {
                continue;
            }
            if (item.getItemId().equalsIgnoreCase(matName)) {
                return item;
            }
        }
        
        // Then search by material match (ignoring enchanted)
        for (SkyblockItem item : ITEMS.values()) {
            if (item.getItemId().toUpperCase().startsWith("ENCHANTED_")) {
                continue;
            }
            if (item.getMaterial() == material) {
                return item;
            }
        }

        // 4. Dynamic Fallback: construct and cache a Skyblock item for this material!
        SkyblockItem fallbackItem = SkyblockItem.builder(matName, material, Rarity.COMMON)
                .displayName(formatName(matName))
                .build();
        ITEMS.put(matName, fallbackItem);
        ITEMS.put(matName.toUpperCase(Locale.ROOT), fallbackItem);
        ITEMS.put(matName.toLowerCase(Locale.ROOT), fallbackItem);
        return fallbackItem;
    }

    public static ItemStack createSkyblockOrVanillaStack(Material material, int amount) {
        SkyblockItem item = getItemByMaterial(material);
        if (item != null) {
            return item.buildItemStack().withAmount(amount);
        }
        return ItemStack.of(material, amount);
    }

    public static Collection<SkyblockItem> getAllItems() {
        return Collections.unmodifiableCollection(ITEMS.values());
    }
}
