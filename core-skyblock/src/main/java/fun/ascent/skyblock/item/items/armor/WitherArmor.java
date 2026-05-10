package fun.ascent.skyblock.item.items.armor;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class WitherArmor {

    private static SkyblockItem.Builder applyDesc(SkyblockItem.Builder builder) {
        return builder
                .description("Reduces the damage you take from",
                        "withers by <red>10%<gray>.");
    }

    public static class WitherHelmet implements ItemDefinition {
        @Override public String getItemId() { return "WITHER_HELMET"; }
        @Override public SkyblockItem.Builder apply(SkyblockItem.Builder builder) { return applyDesc(builder); }
    }

    public static class WitherChestplate implements ItemDefinition {
        @Override public String getItemId() { return "WITHER_CHESTPLATE"; }
        @Override public SkyblockItem.Builder apply(SkyblockItem.Builder builder) { return applyDesc(builder); }
    }

    public static class WitherLeggings implements ItemDefinition {
        @Override public String getItemId() { return "WITHER_LEGGINGS"; }
        @Override public SkyblockItem.Builder apply(SkyblockItem.Builder builder) { return applyDesc(builder); }
    }

    public static class WitherBoots implements ItemDefinition {
        @Override public String getItemId() { return "WITHER_BOOTS"; }
        @Override public SkyblockItem.Builder apply(SkyblockItem.Builder builder) { return applyDesc(builder); }
    }
}
