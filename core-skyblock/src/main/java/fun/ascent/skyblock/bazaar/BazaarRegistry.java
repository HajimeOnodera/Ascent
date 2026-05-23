package fun.ascent.skyblock.bazaar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.ascent.skyblock.bazaar.price.BazaarPriceRegistry;
import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import net.minestom.server.item.Material;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BazaarRegistry {

    public static EnumMap<BazaarCategory, List<BazaarItemFamily>> familyList = new EnumMap<>(BazaarCategory.class);

    private static class FamilyModel {
        String name;
        String icon;
        List<String> productIds;
    }

    public static void initialise() {
        for (BazaarCategory category : BazaarCategory.values()) {
            familyList.put(category, new ArrayList<>());
        }

        try (Reader reader = new InputStreamReader(BazaarRegistry.class.getResourceAsStream("/bazaar_families.json"))) {
            Gson gson = new Gson();
            Map<String, List<FamilyModel>> data = gson.fromJson(reader, new TypeToken<Map<String, List<FamilyModel>>>() {}.getType());

            for (BazaarCategory category : BazaarCategory.values()) {
                if (data.containsKey(category.name)) {
                    for (FamilyModel model : data.get(category.name)) {
                        Material material = Material.fromNamespaceId("minecraft:" + model.icon.toLowerCase());
                        if (material == null) material = Material.STONE;
                        familyList.get(category).add(new BazaarItemFamily(model.name, material, model.productIds));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load bazaar_families.json");
        }

        BazaarPriceRegistry.initialise();
    }
}
