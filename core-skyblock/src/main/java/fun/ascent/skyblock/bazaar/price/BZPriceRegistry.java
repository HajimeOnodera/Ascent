package fun.ascent.skyblock.bazaar.price;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fun.ascent.skyblock.bazaar.BazaarData;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.Price;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class BZPriceRegistry {

    public static HashMap<BazaarEntry, Price> priceTable = new HashMap<>();

    public static void initialise(BazaarData data){
        if (data != null && data.bazaarData != null) {
            priceTable.clear();
            for (BazaarEntry entry : data.bazaarData) {
                registerEntriesRecursively(entry);
            }
            updatePrices();
        }
    }

    private static void registerEntriesRecursively(BazaarEntry entry) {
        if (entry == null) return;
        
        priceTable.put(entry, new Price());

        if (entry.children != null) {
            for (BazaarEntry child : entry.children) {
                registerEntriesRecursively(child);
            }
        }
    }

    public static void updatePrices(){
        try {
            URL url = new URI("https://api.hypixel.net/v2/skyblock/bazaar").toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == 200) {
                JsonObject response = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                if (response.has("products")) {
                    JsonObject products = response.getAsJsonObject("products");
                    
                    priceTable.forEach((entry, price) -> {
                        if (products.has(entry.id)) {
                            JsonObject product = products.getAsJsonObject(entry.id);
                            if (product.has("quick_status")) {
                                JsonObject quickStatus = product.getAsJsonObject("quick_status");
                                price.buyPrice = quickStatus.has("buyPrice") ? quickStatus.get("buyPrice").getAsDouble() : 0;
                                price.sellPrice = quickStatus.has("sellPrice") ? quickStatus.get("sellPrice").getAsDouble() : 0;
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch bazaar prices: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
