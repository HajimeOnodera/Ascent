package fun.ascent.skyblock.menus.shop;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

public class ShopRegistry {

    public static HashMap<String,ShopData> shops = new HashMap<>();

    public static void initialise(){
        shops.clear();
        Reflections reflections = new Reflections("fun.ascent.skyblock.menus.shop");
        Set<Class<? extends ShopData>> shops = reflections.getSubTypesOf(ShopData.class);
        for (Class<? extends ShopData> shop : shops) {
            if (Modifier.isAbstract(shop.getModifiers()) || shop.isInterface()) {
                continue;
            }
            try {
                ShopData definition  = shop.getDeclaredConstructor().newInstance();
                register(definition);
            } catch (Exception e) {
                System.err.println("[Skyblock] Failed to register CMD: " + shop.getSimpleName());
                e.printStackTrace();
            }

        }
    }

    public static void register(ShopData data){
        shops.put(data.shopID,data);
        System.out.println("[SHOPS] Registered: " + data.shopID);
    }

}
