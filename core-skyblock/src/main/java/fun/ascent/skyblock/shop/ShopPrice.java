package fun.ascent.skyblock.shop;

import fun.ascent.skyblock.item.SkyblockItem;

public class ShopPrice {
    public int price;
    public int maxBuyable;
    public SkyblockItem item;

    public ShopPrice(int price, int maxBuyable, SkyblockItem item) {
        this.price = price;
        this.maxBuyable = maxBuyable;
        this.item = item;
    }
}
