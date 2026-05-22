package fun.ascent.skyblock.item.items;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemDefinitions {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDefinitions.class);
    private static final Map<String, ItemDefinition> DEFINITIONS = new HashMap<>();

    public static void init() {
        Reflections reflections = new Reflections(new org.reflections.util.ConfigurationBuilder()
                .setUrls(org.reflections.util.ClasspathHelper.forPackage("fun.ascent.skyblock.item.items", ItemDefinitions.class.getClassLoader()))
                .addClassLoaders(ItemDefinitions.class.getClassLoader()));
        Set<Class<? extends ItemDefinition>> classes = reflections.getSubTypesOf(ItemDefinition.class);

        for (Class<? extends ItemDefinition> clazz : classes) {
            try {
                ItemDefinition def = clazz.getDeclaredConstructor().newInstance();
                DEFINITIONS.put(def.getItemId(), def);
            } catch (Exception e) {
                LOGGER.error("Failed to register item definition: {}", clazz.getSimpleName(), e);
            }
        }

        LOGGER.info("Registered {} item definitions.", DEFINITIONS.size());
    }

    public static ItemDefinition get(String itemId) {
        return DEFINITIONS.get(itemId);
    }

    public static boolean has(String itemId) {
        return DEFINITIONS.containsKey(itemId);
    }
}
