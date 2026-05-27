package fun.ascent.skyblock.bazaar;

import com.google.gson.Gson;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class BazaarRegistry {

    public static BazaarData bazaarItemList;
    public static double sellTax = 1.2;
    public static final HashMap<String, BazaarEntry> itemToEntryMap = new HashMap<>();

    public static void initialise(){
        bazaarItemList = null;
        bazaarItemList = loadDataFromJSON();
        if (bazaarItemList != null && bazaarItemList.bazaarData != null) {
            for (BazaarEntry entry : bazaarItemList.bazaarData) {
                entry.initializeTransientFields(null);
                populateItemMap(entry);
            }
            BZPriceRegistry.initialise(bazaarItemList);
        }
    }

    private static void populateItemMap(BazaarEntry entry) {
        if (entry.children == null) {
            itemToEntryMap.put(entry.id, entry);
        } else {
            for (BazaarEntry child : entry.children) {
                populateItemMap(child);
            }
        }
    }

    private static BazaarData loadDataFromJSON() {
        File file = new File("configuration/skyblock/bazaar/bazaar_families.json");
        if(!file.exists())return null;
        try (InputStream in = new FileInputStream(file)) {
            return new Gson().fromJson(new InputStreamReader(in), BazaarData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}