package fun.ascent.skyblock.bazaar.price;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class BazaarPriceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(BazaarPriceRegistry.class);
    public static HashMap<String, Price> itemPrices = new HashMap<>();

    public static void initialise(){
        itemPrices.clear();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.hypixel.net/v2/skyblock/bazaar"))
                .header("Accept", "application/json")
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    try {
                        Gson gson = new Gson();
                        JsonObject json = gson.fromJson(body, JsonObject.class);
                        if (json != null && json.has("success") && json.get("success").getAsBoolean()) {
                            JsonObject products = json.getAsJsonObject("products");
                            int count = 0;
                            for (String productId : products.keySet()) {
                                JsonObject product = products.getAsJsonObject(productId);
                                JsonObject quickStatus = product.getAsJsonObject("quick_status");
                                
                                double buyPrice = quickStatus.get("buyPrice").getAsDouble();
                                double sellPrice = quickStatus.get("sellPrice").getAsDouble();
                                long buyVolume = quickStatus.get("buyVolume").getAsLong();
                                long sellVolume = quickStatus.get("sellVolume").getAsLong();
                                int buyOrders = quickStatus.get("buyOrders").getAsInt();
                                int sellOrders = quickStatus.get("sellOrders").getAsInt();
                                
                                Price price = new Price(buyPrice, sellPrice, buyVolume, sellVolume, buyOrders, sellOrders);
                                itemPrices.put(productId, price);
                                
                                BazaarCategory category = guessCategory(productId);
//                                BazaarRegistry.registerItem(productId, category);
                                //TODO: Fix
                                count++;
                            }
                            LOGGER.info("Loaded " + count + " items from Hypixel Bazaar API.");
                        }
                    } catch (Exception e) {
                        LOGGER.error("Failed to parse Bazaar API response", e);
                    }
                })
                .exceptionally(e -> {
                    LOGGER.error("Failed to fetch Bazaar API", e);
                    return null;
                });
    }

    private static BazaarCategory guessCategory(String id) {
        String upper = id.toUpperCase();
        if (upper.contains("WHEAT") || upper.contains("CARROT") || upper.contains("POTATO") || 
            upper.contains("PUMPKIN") || upper.contains("MELON") || upper.contains("MUSHROOM") || 
            upper.contains("SUGAR_CANE") || upper.contains("CACTUS") || upper.contains("PORK") || 
            upper.contains("MUTTON") || upper.contains("RABBIT") || upper.contains("FEATHER") || 
            upper.contains("LEATHER") || upper.contains("BEEF") || upper.contains("CHICKEN") ||
            upper.contains("SEEDS") || upper.contains("HOE") || upper.contains("FARMING")) {
            return BazaarCategory.FARMING;
        }
        if (upper.contains("COAL") || upper.contains("IRON") || upper.contains("GOLD") || 
            upper.contains("DIAMOND") || upper.contains("LAPIS") || upper.contains("EMERALD") || 
            upper.contains("REDSTONE") || upper.contains("QUARTZ") || upper.contains("OBSIDIAN") || 
            upper.contains("GLOWSTONE") || upper.contains("GRAVEL") || upper.contains("FLINT") || 
            upper.contains("ICE") || upper.contains("NETHERRACK") || upper.contains("SAND") || 
            upper.contains("END_STONE") || upper.contains("MITHRIL") || upper.contains("TITANIUM") || upper.contains("MINING") || upper.contains("SNOW")) {
            return BazaarCategory.MINING;
        }
        if (upper.contains("ROTTEN_FLESH") || upper.contains("BONE") || upper.contains("STRING") || 
            upper.contains("SPIDER_EYE") || upper.contains("GUNPOWDER") || upper.contains("ENDER_PEARL") || 
            upper.contains("GHAST_TEAR") || upper.contains("SLIME") || upper.contains("MAGMA_CREAM") || 
            upper.contains("BLAZE") || upper.contains("FLESH") || upper.contains("TEAR") || upper.contains("COMBAT") || upper.contains("STARFALL")) {
            return BazaarCategory.COMBAT;
        }
        if (upper.contains("FISH") || upper.contains("SALMON") || upper.contains("PUFFER") || 
            upper.contains("CLOWNFISH") || upper.contains("SPONGE") || upper.contains("PRISMARINE") || 
            upper.contains("LILY_PAD") || upper.contains("INK_SACK") || upper.contains("LOG") || 
            upper.contains("WOOD") || upper.contains("CLAY") || upper.contains("FISHING") || upper.contains("SHARK")) {
            return BazaarCategory.FISHING_WOOD;
        }
        return BazaarCategory.ODDITIES;
    }
}
