package fun.ascent.skyblock.item;

import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.tag.Tag;

import java.util.UUID;

public final class ItemNBT {

    private static final Tag<String> TAG_ID = Tag.String("id");
    private static final Tag<String> TAG_UUID = Tag.String("uuid");
    private static final Tag<Long> TAG_TIMESTAMP = Tag.Long("timestamp");
    private static final Tag<String> TAG_MODIFIER = Tag.String("modifier");
    private static final Tag<Byte> TAG_RARITY_UPGRADES = Tag.Byte("rarity_upgrades");
    private static final Tag<Byte> TAG_HOT_POTATO_COUNT = Tag.Byte("hot_potato_count");
    private static final Tag<Byte> TAG_ART_OF_PEACE = Tag.Byte("art_of_peace");

    private ItemNBT() {}

    public static boolean isSkyblockItem(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.getTag(TAG_ID) != null;
    }

    public static String getItemId(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null ? data.getTag(TAG_ID) : null;
    }

    public static UUID getUuid(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        String raw = data.getTag(TAG_UUID);
        if (raw == null) return null;
        try { return UUID.fromString(raw); } catch (IllegalArgumentException e) { return null; }
    }

    public static Long getTimestamp(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null ? data.getTag(TAG_TIMESTAMP) : null;
    }

    public static String getModifier(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null ? data.getTag(TAG_MODIFIER) : null;
    }

    public static int getHotPotatoCount(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return 0;
        Byte val = data.getTag(TAG_HOT_POTATO_COUNT);
        return val != null ? (int) val : 0;
    }

    public static boolean isRecombobulated(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return false;
        Byte val = data.getTag(TAG_RARITY_UPGRADES);
        return val != null && val == 1;
    }

    public static boolean hasArtOfPeace(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return false;
        Byte val = data.getTag(TAG_ART_OF_PEACE);
        return val != null && val == 1;
    }
}
