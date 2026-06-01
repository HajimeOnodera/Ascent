package fun.ascent.skyblock.shop;

import fun.ascent.skyblock.item.SkyblockItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerShopData {
    public static final int MAXIMUM_STOCK = 640;
    private static final long STOCK_RESET_MILLIS = 1_200_000L;

    private final Map<String, PurchaseData> itemData = new HashMap<>();
    private final List<BuybackData> buybackData = new ArrayList<>();

    public boolean canPurchase(SkyblockItem item, int amount) {
        return canPurchase(item, amount, MAXIMUM_STOCK);
    }

    public boolean canPurchase(SkyblockItem item, int amount, int stockLimit) {
        updateStock(item);
        return getStock(item, stockLimit) >= amount;
    }

    public int getStock(SkyblockItem item) {
        return getStock(item, MAXIMUM_STOCK);
    }

    public int getStock(SkyblockItem item, int stockLimit) {
        if (item == null) return 0;
        updateStock(item);
        PurchaseData data = itemData.get(item.getItemId());
        return data == null ? stockLimit : Math.max(0, stockLimit - data.amountBought());
    }

    public void documentPurchase(SkyblockItem item, int amount) {
        if (item == null) return;
        PurchaseData data = itemData.get(item.getItemId());
        int bought = data == null ? amount : data.amountBought() + amount;
        itemData.put(item.getItemId(), new PurchaseData(bought, System.currentTimeMillis()));
    }

    public void resetStocks() {
        long now = System.currentTimeMillis();
        itemData.replaceAll((ignored, data) -> new PurchaseData(0, now));
    }

    public void pushBuyback(SkyblockItem item, int amount) {
        buybackData.add(new BuybackData(item, amount));
    }

    public BuybackData popBuyback() {
        if (buybackData.isEmpty()) {
            throw new IndexOutOfBoundsException("No buyback entries available");
        }
        return buybackData.remove(buybackData.size() - 1);
    }

    public BuybackData lastBuyback() {
        if (buybackData.isEmpty()) {
            throw new IndexOutOfBoundsException("No buyback entries available");
        }
        return buybackData.get(buybackData.size() - 1);
    }

    public boolean hasAnythingToBuyback() {
        return !buybackData.isEmpty();
    }

    private void updateStock(SkyblockItem item) {
        if (item == null) return;
        PurchaseData data = itemData.get(item.getItemId());
        if (data == null) return;
        if (System.currentTimeMillis() - data.lastPurchaseMillis() > STOCK_RESET_MILLIS) {
            itemData.put(item.getItemId(), new PurchaseData(0, System.currentTimeMillis()));
        }
    }

    private record PurchaseData(int amountBought, long lastPurchaseMillis) {}

    public record BuybackData(SkyblockItem item, int amount) {}
}
