package fun.ascent.skyblock.item.items;

import fun.ascent.skyblock.item.SkyblockItem;

public interface ItemDefinition {

    String getItemId();

    SkyblockItem.Builder apply(SkyblockItem.Builder builder);
}
