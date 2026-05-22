package fun.ascent.skyblock.crafting;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import lombok.Getter;

@Getter
public class RecipeIngredient {
    private final String itemId;
    private final int amount;

    public RecipeIngredient(String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public boolean matches(SkyblockItem item) {
        if (item == null) return false;
        String thisId = ItemRegistry.canonicalizeId(itemId);
        String thatId = ItemRegistry.canonicalizeId(item.getItemId());
        return thisId.equalsIgnoreCase(thatId);
    }
}
