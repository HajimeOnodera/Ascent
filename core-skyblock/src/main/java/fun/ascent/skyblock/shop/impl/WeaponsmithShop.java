package fun.ascent.skyblock.shop.impl;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.shop.ShopData;

public class WeaponsmithShop extends ShopData {

    public WeaponsmithShop() {
        super("weaponsmith_shop","<gray>Weaponsmith");
        addItem(ItemRegistry.getItem("UNDEAD_SWORD"),100,640);
        addItem(ItemRegistry.getItem("END_SWORD"),150,640);
        addItem(ItemRegistry.getItem("SPIDER_SWORD"),100,640);
        addItem(ItemRegistry.getItem("DIAMOND_SWORD"),60,640);
        addItem(ItemRegistry.getItem("ROGUE_SWORD"),100,640);
        addItem(ItemRegistry.getItem("BOW"),25,640);
        addItem(ItemRegistry.getItem("ARROW"),3,640);
        addItem(ItemRegistry.getItem("WITHER_BOW"),250,640);
        addItem(ItemRegistry.getItem("ARTISANAL_SHORTBOW"),600,640);
    }
}
