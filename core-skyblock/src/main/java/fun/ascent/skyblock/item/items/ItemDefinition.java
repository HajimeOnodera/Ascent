package fun.ascent.skyblock.item.items;

import fun.ascent.skyblock.item.ItemAbility;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;

public interface ItemDefinition {

    String getItemId();

    SkyblockItem.Builder apply(SkyblockItem.Builder builder);

    default ItemAbility ability() { return null; }

    default void onRightClick(SkyblockPlayer player) {}

    default void onLeftClick(SkyblockPlayer player) {}
}
