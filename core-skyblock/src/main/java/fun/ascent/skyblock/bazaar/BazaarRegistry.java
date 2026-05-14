package fun.ascent.skyblock.bazaar;

import fun.ascent.skyblock.bazaar.price.BazaarPriceRegistry;
import fun.ascent.skyblock.bazaar.vars.BazaarCategory;

import java.util.EnumMap;

public class BazaarRegistry {

    public static EnumMap<BazaarCategory,BazaarItem> itemList =  new EnumMap<>(BazaarCategory.class);

    public static void initialise(){
        itemList.clear();
        //TODO: Add Items
        BazaarPriceRegistry.initialise();
    }


}
