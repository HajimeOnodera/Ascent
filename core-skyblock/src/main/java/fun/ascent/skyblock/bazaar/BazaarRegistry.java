package fun.ascent.skyblock.bazaar;

import com.google.gson.Gson;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;

import java.io.InputStream;
import java.io.InputStreamReader;

public class BazaarRegistry {

    public static BazaarData bazaarData;

    public static void initialise(){
        bazaarData = loadDataFromJSON();
        if (bazaarData != null && bazaarData.bazaarData != null) {
            for (BazaarEntry entry : bazaarData.bazaarData) {
                entry.initializeTransientFields(null);
            }
            BZPriceRegistry.initialise(bazaarData);
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