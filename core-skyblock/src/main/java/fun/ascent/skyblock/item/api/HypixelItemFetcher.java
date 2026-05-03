package fun.ascent.skyblock.item.api;

import com.google.gson.*;
import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.SkyblockItemData;
import net.minestom.server.item.Material;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class HypixelItemFetcher {

    private static final String ITEMS_ENDPOINT = "https://api.hypixel.net/v2/resources/skyblock/items";

    private static final Map<String, String> LEGACY_MATERIALS = Map.ofEntries(
            Map.entry("SKULL_ITEM", "player_head"),
            Map.entry("INK_SACK", "ink_sac"),
            Map.entry("NETHER_STALK", "nether_wart"),
            Map.entry("GRASS", "grass_block"),
            Map.entry("WOOD", "oak_planks"),
            Map.entry("LOG", "oak_log"),
            Map.entry("LOG_2", "dark_oak_log"),
            Map.entry("LEAVES", "oak_leaves"),
            Map.entry("LEAVES_2", "dark_oak_leaves"),
            Map.entry("STEP", "stone_slab"),
            Map.entry("DOUBLE_STEP", "stone_slab"),
            Map.entry("STONE_SLAB2", "smooth_red_sandstone_slab"),
            Map.entry("GOLD_SWORD", "golden_sword"),
            Map.entry("GOLD_SPADE", "golden_shovel"),
            Map.entry("GOLD_PICKAXE", "golden_pickaxe"),
            Map.entry("GOLD_AXE", "golden_axe"),
            Map.entry("GOLD_HOE", "golden_hoe"),
            Map.entry("GOLD_HELMET", "golden_helmet"),
            Map.entry("GOLD_CHESTPLATE", "golden_chestplate"),
            Map.entry("GOLD_LEGGINGS", "golden_leggings"),
            Map.entry("GOLD_BOOTS", "golden_boots"),
            Map.entry("GOLD_NUGGET", "gold_nugget"),
            Map.entry("GOLD_INGOT", "gold_ingot"),
            Map.entry("GOLD_BLOCK", "gold_block"),
            Map.entry("GOLD_ORE", "gold_ore"),
            Map.entry("WATCH", "clock"),
            Map.entry("MAP", "filled_map"),
            Map.entry("SULPHUR", "gunpowder"),
            Map.entry("WATER_LILY", "lily_pad"),
            Map.entry("HUGE_MUSHROOM_1", "brown_mushroom_block"),
            Map.entry("HUGE_MUSHROOM_2", "red_mushroom_block"),
            Map.entry("PORK", "porkchop"),
            Map.entry("GRILLED_PORK", "cooked_porkchop"),
            Map.entry("RAW_BEEF", "beef"),
            Map.entry("RAW_CHICKEN", "chicken"),
            Map.entry("RAW_FISH", "cod"),
            Map.entry("COOKED_FISH", "cooked_cod"),
            Map.entry("THIN_GLASS", "glass_pane"),
            Map.entry("SMOOTH_BRICK", "stone_bricks"),
            Map.entry("SMOOTH_STAIRS", "stone_brick_stairs"),
            Map.entry("SNOW_BALL", "snowball"),
            Map.entry("WORKBENCH", "crafting_table"),
            Map.entry("SIGN", "oak_sign"),
            Map.entry("WOOD_STAIRS", "oak_stairs"),
            Map.entry("WOOD_DOOR", "oak_door"),
            Map.entry("TRAP_DOOR", "oak_trapdoor"),
            Map.entry("FENCE", "oak_fence"),
            Map.entry("FENCE_GATE", "oak_fence_gate"),
            Map.entry("WOOD_PLATE", "oak_pressure_plate"),
            Map.entry("STONE_PLATE", "stone_pressure_plate"),
            Map.entry("DIODE", "repeater"),
            Map.entry("REDSTONE_COMPARATOR", "comparator"),
            Map.entry("SPECKLED_MELON", "glistering_melon_slice"),
            Map.entry("MELON", "melon"),
            Map.entry("ENDER_STONE", "end_stone"),
            Map.entry("STORAGE_MINECART", "chest_minecart"),
            Map.entry("POWERED_MINECART", "furnace_minecart"),
            Map.entry("EXPLOSIVE_MINECART", "tnt_minecart"),
            Map.entry("HOPPER_MINECART", "hopper_minecart"),
            Map.entry("FIREWORK", "firework_rocket"),
            Map.entry("FIREWORK_CHARGE", "firework_star"),
            Map.entry("BOOK_AND_QUILL", "writable_book"),
            Map.entry("QUARTZ_ORE", "nether_quartz_ore"),
            Map.entry("STAINED_CLAY", "white_terracotta"),
            Map.entry("HARD_CLAY", "terracotta"),
            Map.entry("DOUBLE_PLANT", "sunflower"),
            Map.entry("CARPET", "white_carpet"),
            Map.entry("LONG_GRASS", "tall_grass"),
            Map.entry("STAINED_GLASS", "white_stained_glass"),
            Map.entry("STAINED_GLASS_PANE", "white_stained_glass_pane")
    );

    public static List<SkyblockItemData> fetchItems() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ITEMS_ENDPOINT))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
        if (!root.has("success") || !root.get("success").getAsBoolean()) {
            throw new RuntimeException("Hypixel API returned a failure response");
        }

        JsonArray itemsArray = root.getAsJsonArray("items");
        List<SkyblockItemData> result = new ArrayList<>(itemsArray.size());
        for (JsonElement element : itemsArray) {
            result.add(parseItem(element.getAsJsonObject()));
        }
        return result;
    }

    private static SkyblockItemData parseItem(JsonObject obj) {
        String id = obj.get("id").getAsString();
        String name = obj.get("name").getAsString();

        Material material = parseMaterial(obj.get("material").getAsString());
        Rarity rarity = parseRarity(obj);
        ItemType itemType = parseItemType(obj);

        String skinValue = null;
        if (obj.has("skin")) {
            JsonObject skin = obj.getAsJsonObject("skin");
            if (skin.has("value")) skinValue = skin.get("value").getAsString();
        }

        double npcSellPrice = obj.has("npc_sell_price") ? obj.get("npc_sell_price").getAsDouble() : 0.0;

        Map<String, Double> stats = new HashMap<>();
        if (obj.has("stats")) {
            for (Map.Entry<String, JsonElement> entry : obj.getAsJsonObject("stats").entrySet()) {
                try {
                    stats.put(entry.getKey().toUpperCase(), entry.getValue().getAsDouble());
                } catch (Exception ignored) {}
            }
        }

        List<String> gemstoneSlotTypes = new ArrayList<>();
        if (obj.has("gemstone_slots")) {
            for (JsonElement slotEl : obj.getAsJsonArray("gemstone_slots")) {
                JsonObject slot = slotEl.getAsJsonObject();
                if (slot.has("slot_type")) {
                    gemstoneSlotTypes.add(slot.get("slot_type").getAsString());
                }
            }
        }

        String soulbound = obj.has("soulbound") ? obj.get("soulbound").getAsString() : null;
        boolean glowing = obj.has("glowing") && obj.get("glowing").getAsBoolean();
        boolean unstackable = obj.has("unstackable") && obj.get("unstackable").getAsBoolean();
        String color = obj.has("color") ? obj.get("color").getAsString() : null;
        String description = obj.has("description") ? obj.get("description").getAsString() : null;
        String itemModel = obj.has("item_model") ? obj.get("item_model").getAsString() : null;

        return new SkyblockItemData(id, name, material, rarity, itemType, skinValue, npcSellPrice,
                Collections.unmodifiableMap(stats), List.copyOf(gemstoneSlotTypes),
                soulbound, glowing, unstackable, color, description, itemModel);
    }

    private static Material parseMaterial(String apiName) {
        String mapped = LEGACY_MATERIALS.getOrDefault(apiName, apiName.toLowerCase());
        Material material = Material.fromKey("minecraft:" + mapped);
        return material != null ? material : Material.PAPER;
    }

    private static Rarity parseRarity(JsonObject obj) {
        if (!obj.has("tier")) return Rarity.COMMON;
        return switch (obj.get("tier").getAsString()) {
            case "UNCOMMON" -> Rarity.UNCOMMON;
            case "RARE" -> Rarity.RARE;
            case "EPIC" -> Rarity.EPIC;
            case "LEGENDARY" -> Rarity.LEGENDARY;
            case "MYTHIC" -> Rarity.MYTHIC;
            case "DIVINE" -> Rarity.DIVINE;
            case "SPECIAL" -> Rarity.SPECIAL;
            case "VERY_SPECIAL" -> Rarity.VERY_SPECIAL;
            case "ULTIMATE" -> Rarity.ULTIMATE;
            case "ADMIN" -> Rarity.ADMIN;
            default -> Rarity.COMMON;
        };
    }

    private static ItemType parseItemType(JsonObject obj) {
        if (!obj.has("category")) return ItemType.NONE;
        return switch (obj.get("category").getAsString()) {
            case "SWORD" -> ItemType.SWORD;
            case "BOW" -> ItemType.BOW;
            case "LONGSWORD" -> ItemType.LONGSWORD;
            case "WAND" -> ItemType.WAND;
            case "AXE" -> ItemType.AXE;
            case "PICKAXE" -> ItemType.PICKAXE;
            case "DRILL" -> ItemType.DRILL;
            case "SHOVEL" -> ItemType.SHOVEL;
            case "HOE" -> ItemType.HOE;
            case "FISHING_ROD" -> ItemType.FISHING_ROD;
            case "FISHING_WEAPON" -> ItemType.FISHING_WEAPON;
            case "GAUNTLET" -> ItemType.GAUNTLET;
            case "SHEARS" -> ItemType.SHEARS;
            case "HELMET" -> ItemType.HELMET;
            case "CHESTPLATE" -> ItemType.CHESTPLATE;
            case "LEGGINGS" -> ItemType.LEGGINGS;
            case "BOOTS" -> ItemType.BOOTS;
            case "BELT" -> ItemType.BELT;
            case "NECKLACE" -> ItemType.NECKLACE;
            case "BRACELET" -> ItemType.BRACELET;
            case "GLOVES" -> ItemType.GLOVES;
            case "CLOAK" -> ItemType.CLOAK;
            case "ACCESSORY" -> ItemType.ACCESSORY;
            case "HATCESSORY" -> ItemType.HATCESSORY;
            case "DEPLOYABLE" -> ItemType.DEPLOYABLE;
            case "PET_ITEM" -> ItemType.PET_ITEM;
            case "REFORGE_STONE" -> ItemType.REFORGE_STONE;
            case "COSMETIC" -> ItemType.COSMETIC;
            case "TRAVEL_SCROLL" -> ItemType.TRAVEL_SCROLL;
            case "ARROW" -> ItemType.ARROW;
            case "ARROW_POISON" -> ItemType.ARROW_POISON;
            case "BAIT" -> ItemType.BAIT;
            case "DUNGEON_PASS" -> ItemType.DUNGEON_PASS;
            default -> ItemType.NONE;
        };
    }
}
