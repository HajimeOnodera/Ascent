package fun.ascent.skyblock.shop;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CoinShopPrice implements ShopPrice {

    private final double amount;

    public CoinShopPrice(double amount) {
        this.amount = amount;
    }

    @Override
    public List<Component> getGUIDisplay() {
        return List.of(StringUtility.text("<gold>" + StringUtility.decimalify(amount, 1) + " Coin" + (amount != 1 ? "s" : "")));
    }

    @Override
    public String getNamePlural() {
        return "coins";
    }

    @Override
    public boolean canAfford(SkyblockPlayer player) {
        return player.getCoins() >= amount;
    }

    @Override
    public void processPurchase(SkyblockPlayer player) {
        player.removeCoins(amount);
    }

    @Override
    public ShopPrice multiply(int amount) {
        return new CoinShopPrice(this.amount * amount);
    }

    @Override
    public ShopPrice divide(double amount) {
        return new CoinShopPrice(this.amount / amount);
    }
}
