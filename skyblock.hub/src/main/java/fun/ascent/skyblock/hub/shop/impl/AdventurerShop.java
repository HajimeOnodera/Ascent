package fun.ascent.skyblock.hub.shop.impl;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.hub.shop.ShopData;

public class AdventurerShop extends ShopData {

    public AdventurerShop() {
        super("adv_shop","<gray>Adventurer");
        addItem(ItemRegistry.getItem("ROTTEN_FLESH"),8,640);
        addItem(ItemRegistry.getItem("BONE"),8,640);
        addItem(ItemRegistry.getItem("STRING"),10,640);
        addItem(ItemRegistry.getItem("SLIME_BALL"),14,640);
        addItem(ItemRegistry.getItem("SULPHUR"),10,640);
        addItem(ItemRegistry.getItem("ZOMBIE_TALISMAN"),500,640);
        addItem(ItemRegistry.getItem("SKELETON_TALISMAN"),500,640);
        addItem(ItemRegistry.getItem("VILLAGE_TALISMAN"),2500,640);
        addItem(ItemRegistry.getItem("MINE_TALISMAN"),2500,640);
        addItem(ItemRegistry.getItem("INTIMIDATION_TALISMAN"),10000,640);
        addItem(ItemRegistry.getItem("SCAVENGER_TALISMAN"),10000,640);
    }
}
