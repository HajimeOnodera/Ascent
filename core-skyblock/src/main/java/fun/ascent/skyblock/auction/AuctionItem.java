package fun.ascent.skyblock.auction;

import fun.ascent.skyblock.item.SkyblockItemData;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class AuctionItem {

    public SkyblockItemData item;
    public UUID sellerUUID;
    public double sellPrice;
    public UUID buyerUUID;

}
