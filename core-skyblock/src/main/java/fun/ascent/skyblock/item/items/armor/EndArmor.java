package fun.ascent.skyblock.item.items.armor;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class EndArmor {

    private static SkyblockItem.Builder applyDesc(SkyblockItem.Builder builder) {
        return builder
                .description("All stats on this armor piece are",
                        "multiplied by <dark_purple>2x<gray> while on the End",
                        "Island!");
    }

    public static class EndHelmet implements ItemDefinition {
        @Override public String getItemId() { return "END_HELMET"; }
        @Override public SkyblockItem.Builder apply(SkyblockItem.Builder builder) { return applyDesc(builder); }
    }

    public static class EndChestplate implements ItemDefinition {
        @Override public String getItemId() { return "END_CHESTPLATE"; }
        @Override public SkyblockItem.Builder apply(SkyblockItem.Builder builder) { return applyDesc(builder); }
    }

    public static class EndLeggings implements ItemDefinition {
        @Override public String getItemId() { return "END_LEGGINGS"; }
        @Override public SkyblockItem.Builder apply(SkyblockItem.Builder builder) { return applyDesc(builder); }
    }

    public static class EndBoots implements ItemDefinition {
        @Override public String getItemId() { return "END_BOOTS"; }
        @Override public SkyblockItem.Builder apply(SkyblockItem.Builder builder) { return applyDesc(builder); }
    }
}
