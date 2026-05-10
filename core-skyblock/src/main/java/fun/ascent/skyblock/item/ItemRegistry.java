package fun.ascent.skyblock.item;

import fun.ascent.skyblock.item.api.HypixelItemFetcher;
import fun.ascent.skyblock.item.items.ItemDefinition;
import fun.ascent.skyblock.item.items.ItemDefinitions;
import fun.ascent.skyblock.item.gemstone.GemstoneSlot;
import fun.ascent.skyblock.item.gemstone.GemstoneSlotType;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.color.Color;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private static final Map<String, SkyblockItem> ITEMS = new HashMap<>();

    public static void init() {
        try {
            var rawItems = HypixelItemFetcher.fetchItems();
            for (SkyblockItemData data : rawItems) {
                ITEMS.put(data.id(), convert(data));
            }
            System.out.println("[ItemRegistry] Loaded " + ITEMS.size() + " items.");
        } catch (Exception e) {
            System.err.println("[ItemRegistry] Failed to load items from Hypixel API: " + e.getMessage());
            e.printStackTrace();
        }
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
