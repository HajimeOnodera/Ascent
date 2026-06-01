package fun.ascent.skyblock.shop;

import fun.ascent.skyblock.item.SkyblockItem;
import net.minestom.server.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class ShopData {

    public List<ShopItem> shopItems = new ArrayList<>();
    public String shopID;
    public String shopTitle;
    public InventoryType invType = InventoryType.CHEST_6_ROW;

    public ShopData(String shopID, String shopTitle) {
        this.shopID = shopID;
        this.shopTitle = shopTitle;
    }

    public void addItem(SkyblockItem skyblockItem, int price, int maxBuyable) {
        addItem(skyblockItem, 1, new CoinShopPrice(price), true, maxBuyable > 0, maxBuyable);
    }

    public void addItem(SkyblockItem skyblockItem, int amount, ShopPrice price, boolean stackable) {
        addItem(skyblockItem, amount, price, stackable, true, PlayerShopData.MAXIMUM_STOCK);
    }

    public void addItem(SkyblockItem skyblockItem, int amount, ShopPrice price, boolean stackable, boolean hasStock) {
        addItem(skyblockItem, amount, price, stackable, hasStock, PlayerShopData.MAXIMUM_STOCK);
    }

    public void addItem(SkyblockItem skyblockItem, int amount, ShopPrice price, boolean stackable, boolean hasStock, int stockLimit) {
        this.shopItems.add(new ShopItem(skyblockItem, amount, price, stackable, hasStock, stockLimit));
    }

    public List<ShopItem> getItems() {
        return shopItems;
    }

    public record ShopItem(SkyblockItem item, int amount, ShopPrice price, boolean stackable, boolean hasStock, int stockLimit) {
        public ShopPrice stackPrice() {
            return price.divide(amount);
        }
    }
}
