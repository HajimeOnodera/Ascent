package fun.ascent.skyblock.auction;

import fun.ascent.skyblock.auction.vars.AuctionCategory;

import java.util.EnumMap;
import java.util.List;

public class AuctionRegistry {

    public static EnumMap<AuctionCategory, List<AuctionItem>> items = new EnumMap<>(AuctionCategory.class);

    public static void initialise(){
        items.clear();
        //TODO: Load From DB
    }

}
