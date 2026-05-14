package fun.ascent.skyblock.shop;

import fun.ascent.skyblock.item.SkyblockItem;
import net.minestom.server.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class ShopData {

    public List<ShopPrice> shopItems = new ArrayList<>();
    public String shopID;
    public String shopTitle;
    public InventoryType invType = InventoryType.CHEST_6_ROW;

    public ShopData(String shopID, String shopTitle) {
        this.shopID = shopID;
        this.shopTitle = shopTitle;
    }

    public void addItem(SkyblockItem skyblockItem, int price, int maxBuyable) {
        this.shopItems.add(new ShopPrice(price, maxBuyable, skyblockItem));
    }

    public List<ShopPrice> getItems() {
        return shopItems;
    }
}
