package fun.ascent.skyblock.menus.shop;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShopRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopRegistry.class);
    public static HashMap<String,ShopData> shops = new HashMap<>();

    public static void initialise(){
        shops.clear();
        Reflections reflections = new Reflections("fun.ascent.skyblock.menus.shop");
        Set<Class<? extends ShopData>> shopsFound = reflections.getSubTypesOf(ShopData.class);
        for (Class<? extends ShopData> shop : shopsFound) {
            if (Modifier.isAbstract(shop.getModifiers()) || shop.isInterface()) {
                continue;
            }
            try {
                ShopData definition  = shop.getDeclaredConstructor().newInstance();
                register(definition);
            } catch (Exception e) {
                LOGGER.error("Failed to register shop definition: {}", shop.getSimpleName(), e);
            }
        }
        LOGGER.info("Initialized {} shops.", shops.size());
    }

    public static void register(ShopData data){
        shops.put(data.shopID,data);
    }
}
