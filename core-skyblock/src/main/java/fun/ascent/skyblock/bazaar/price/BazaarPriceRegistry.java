package fun.ascent.skyblock.bazaar.price;

import fun.ascent.skyblock.bazaar.BazaarItem;

import java.util.HashMap;

public class BazaarPriceRegistry {

    public static HashMap<BazaarItem,Price> itemPrices = new HashMap<>();

    public static void initialise(){
        itemPrices.clear();
        //TODO: Load From DB & Set Price for infinite Items
    }
}
