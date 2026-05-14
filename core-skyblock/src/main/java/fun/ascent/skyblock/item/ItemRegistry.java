package fun.ascent.skyblock.item;

import fun.ascent.database.ItemRepository;
import fun.ascent.skyblock.item.api.HypixelItemFetcher;
import fun.ascent.skyblock.item.items.ItemDefinition;
import fun.ascent.skyblock.item.items.ItemDefinitions;
import fun.ascent.skyblock.item.gemstone.GemstoneSlot;
import fun.ascent.skyblock.item.gemstone.GemstoneSlotType;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.color.Color;

import java.util.*;

import net.minestom.server.item.Material;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRegistry.class);
    private static final Map<String, SkyblockItem> ITEMS = new HashMap<>();

    public static void init() {
        try {
            long count = ItemRepository.getItemCount();
            if (count > 0) {
                LOGGER.info("Loading items from MongoDB cache...");
                List<Document> docs = ItemRepository.getAllItems();
                for (Document doc : docs) {
                    SkyblockItemData data = deserializeData(doc);
                    ITEMS.put(data.id(), convert(data));
                }
                LOGGER.info("Loaded {} items from MongoDB.", ITEMS.size());
            } else {
                LOGGER.info("MongoDB cache empty, fetching items from Hypixel API...");
                var rawItems = HypixelItemFetcher.fetchItems();
                List<Document> docsToSave = new ArrayList<>();
                for (SkyblockItemData data : rawItems) {
                    ITEMS.put(data.id(), convert(data));
                    docsToSave.add(serializeData(data));
                }
                ItemRepository.saveItems(docsToSave);
                LOGGER.info("Loaded and cached {} items from Hypixel API.", ITEMS.size());
            }
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

        Material material = materialStr != null ? Material.fromKey(materialStr) : Material.PAPER;
        if (material == null) material = Material.PAPER;

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
                doc.getBoolean("unstackable"),
                doc.getString("color"),
                doc.getString("description"),
                doc.getString("itemModel")
        );
    }

    private static SkyblockItem convert(SkyblockItemData data) {
        SkyblockItem.Builder builder = SkyblockItem.builder(data.id(), data.material(), data.rarity())
                .displayName(data.name())
                .itemType(data.itemType())
                .glowing(data.glowing());

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

    public static SkyblockItem getItem(String id) {
        return ITEMS.get(id);
    }

    public static boolean hasItem(String id) {
        return ITEMS.containsKey(id);
    }

    public static Collection<SkyblockItem> getAllItems() {
        return Collections.unmodifiableCollection(ITEMS.values());
    }

    public static Map<String, SkyblockItem> getItemMap() {
        return Collections.unmodifiableMap(ITEMS);
    }
}
