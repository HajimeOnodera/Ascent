package fun.ascent.skyblock.item.items;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemDefinitions {

    private static final Map<String, ItemDefinition> DEFINITIONS = new HashMap<>();

    public static void init() {
        Reflections reflections = new Reflections("fun.ascent.skyblock.item.items");
        Set<Class<? extends ItemDefinition>> classes = reflections.getSubTypesOf(ItemDefinition.class);

        for (Class<? extends ItemDefinition> clazz : classes) {
            try {
                ItemDefinition def = clazz.getDeclaredConstructor().newInstance();
                DEFINITIONS.put(def.getItemId(), def);
            } catch (Exception e) {
                System.err.println("[ItemDefinitions] Failed to register " + clazz.getSimpleName() + ": " + e.getMessage());
            }
        }

        System.out.println("[ItemDefinitions] Registered " + DEFINITIONS.size() + " items.");
    }

    public static ItemDefinition get(String itemId) {
        return DEFINITIONS.get(itemId);
    }

    public static boolean has(String itemId) {
        return DEFINITIONS.containsKey(itemId);
    }
}
