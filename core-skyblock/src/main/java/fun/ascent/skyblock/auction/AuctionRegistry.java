package fun.ascent.skyblock.auction;

import fun.ascent.skyblock.auction.vars.AuctionCategory;

import java.util.ArrayList;
import java.util.List;

public class AuctionRegistry {

    public static final List<AuctionItem> allAuctions = new ArrayList<>();

    public static void initialise(){
        allAuctions.clear();
        //TODO: Load From DB
    }

    public static void addAuction(AuctionItem item) {
        allAuctions.add(item);
    }

    public static void removeAuction(AuctionItem item) {
        allAuctions.remove(item);
    }

    public static List<AuctionItem> getAuctionsByCategory(AuctionCategory category) {
        // Simple mapping based on item material or id. For now we will return all since we haven't built a category mapping for items.
        // In a real system, you'd map item IDs to AuctionCategory.
        return new ArrayList<>(allAuctions);
    }

    public static List<AuctionItem> getAuctionsByPlayer(java.util.UUID playerUUID) {
        List<AuctionItem> result = new ArrayList<>();
        for (AuctionItem item : allAuctions) {
            if (item.sellerUUID.equals(playerUUID)) {
                result.add(item);
            }
        }
        return result;
    }

}
