package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.player.collections.loader.CollectionLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionRegistry {

    private static final Map<String, CollectionCategory.ItemCollection> COLLECTIONS_BY_ID = new HashMap<>();
    private static final Map<CollectionCategory.CollectionType, CollectionCategory> CATEGORIES = new HashMap<>();

    public static void init() {
        COLLECTIONS_BY_ID.clear();
        CATEGORIES.clear();

        List<CollectionCategory> loaded = CollectionLoader.loadAll();
        for (CollectionCategory category : loaded) {
            registerCategory(category);
        }
    }

    public static void registerCategory(CollectionCategory category) {
        CATEGORIES.put(category.getType(), category);
        for (CollectionCategory.ItemCollection collection : category.getCollections()) {
            COLLECTIONS_BY_ID.put(collection.itemId(), collection);
        }
    }

    public static CollectionCategory.ItemCollection get(String itemId) {
        return COLLECTIONS_BY_ID.get(itemId);
    }

    public static CollectionCategory getCategory(CollectionCategory.CollectionType type) {
        return CATEGORIES.get(type);
    }

    public static CollectionCategory getCategoryFor(CollectionCategory.ItemCollection collection) {
        for (CollectionCategory category : CATEGORIES.values()) {
            if (category.getCollections().contains(collection)) {
                return category;
            }
        }
        return null;
    }
    
    public static Map<CollectionCategory.CollectionType, CollectionCategory> getCategories() {
        return CATEGORIES;
    }

    public static int getTotalCollectionsCount() {
        return COLLECTIONS_BY_ID.size();
    }
}
