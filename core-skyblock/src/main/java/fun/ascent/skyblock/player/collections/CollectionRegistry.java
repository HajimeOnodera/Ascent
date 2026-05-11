package fun.ascent.skyblock.player.collections;

import java.util.HashMap;
import java.util.Map;

public class CollectionRegistry {

    private static final Map<String, Collection> REGISTRY = new HashMap<>();

    public static void init() {
        REGISTRY.clear();
        // Register all your collections here
    }

    public static void register(Collection collection) {
        REGISTRY.put(collection.ITEM_ID, collection);
    }

    public static Collection get(String itemId) {
        return REGISTRY.get(itemId);
    }
}