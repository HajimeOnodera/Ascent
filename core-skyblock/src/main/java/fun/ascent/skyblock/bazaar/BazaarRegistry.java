package fun.ascent.skyblock.bazaar;

import com.google.gson.Gson;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;

import java.io.InputStream;
import java.io.InputStreamReader;

public class BazaarRegistry {

    public static BazaarData bazaarItemList;

    public static void initialise(){
        bazaarItemList = loadDataFromJSON();
        if (bazaarItemList != null && bazaarItemList.bazaarData != null) {
            for (BazaarEntry entry : bazaarItemList.bazaarData) {
                entry.initializeTransientFields(null);
            }
            BZPriceRegistry.initialise(bazaarItemList);
        }
    }

    private static BazaarData loadDataFromJSON() {
        try (InputStream in = BazaarRegistry.class.getClassLoader().getResourceAsStream("bazaar_families.json")) {
            if (in == null) {
                System.err.println("bazaar_families.json not found in resources!");
                return null;
            }
            return new Gson().fromJson(new InputStreamReader(in), BazaarData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}