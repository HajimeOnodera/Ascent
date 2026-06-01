package fun.ascent.skyblock.shop;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class CombinedShopPrice implements ShopPrice {

    private final List<ShopPrice> priceElements;

    public CombinedShopPrice(List<ShopPrice> priceElements) {
        this.priceElements = List.copyOf(priceElements);
    }

    @Override
    public List<Component> getGUIDisplay() {
        List<Component> result = new ArrayList<>();
        priceElements.forEach(price -> result.addAll(price.getGUIDisplay()));
        return result;
    }

    @Override
    public String getNamePlural() {
        return String.join(" or ", priceElements.stream().map(ShopPrice::getNamePlural).toList());
    }

    @Override
    public boolean canAfford(SkyblockPlayer player) {
        return priceElements.stream().allMatch(price -> price.canAfford(player));
    }

    @Override
    public void processPurchase(SkyblockPlayer player) {
        priceElements.forEach(price -> price.processPurchase(player));
    }

    @Override
    public ShopPrice multiply(int amount) {
        return new CombinedShopPrice(priceElements.stream().map(price -> price.multiply(amount)).toList());
    }

    @Override
    public ShopPrice divide(double amount) {
        return new CombinedShopPrice(priceElements.stream().map(price -> price.divide(amount)).toList());
    }
}
