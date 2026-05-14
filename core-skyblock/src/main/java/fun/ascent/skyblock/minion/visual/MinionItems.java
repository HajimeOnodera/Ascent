package fun.ascent.skyblock.minion.visual;

import fun.ascent.skyblock.minion.model.MinionData;
import fun.ascent.skyblock.minion.model.MinionType;
import fun.ascent.skyblock.minion.profile.MinionProfile;
import fun.ascent.skyblock.minion.profile.MinionProfiles;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.ResolvableProfile;
import net.minestom.server.tag.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MinionItems {
    public static final Tag<String> MINION_TYPE_TAG = Tag.String("minion_type");
    public static final Tag<Integer> MINION_TIER_TAG = Tag.Integer("minion_tier");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private MinionItems() {
    }

    public static ItemStack createPlacementItem(MinionType type, int tier) {
        MinionData data = new MinionData(type, tier);
        MinionProfile profile = MinionProfiles.get(type);
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Place this minion and it will"));
        lore.add(text("<gray>start " + profile.placementDescription()));
        lore.add(text("<gray>" + profile.layoutHint()));
        lore.add(text("<gray>Minions also work when you are"));
        lore.add(text("<gray>offline!"));
        lore.add(Component.empty());
        lore.add(text("<gray>Time Between Actions: <green>" + data.getActionDelaySeconds() + "s</green>"));
        lore.add(text("<gray>Max Storage: <yellow>" + data.getMaxStorage() + "</yellow>"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Right-click a block to place this minion.</yellow>"));

        return createHead(profile.texture())
                .withCustomName(text("<blue>" + type.getDisplayName() + " " + roman(tier)))
                .withLore(lore)
                .withTag(MINION_TYPE_TAG, type.getId())
                .withTag(MINION_TIER_TAG, tier)
                .with(DataComponents.CUSTOM_DATA, CustomData.EMPTY
                        .withTag(MINION_TYPE_TAG, type.getId())
                        .withTag(MINION_TIER_TAG, tier));
    }

    public static ItemStack createHead(String base64Texture) {
        ResolvableProfile profile = new ResolvableProfile(
                new ResolvableProfile.Partial(
                        "",
                        UUID.randomUUID(),
                        List.of(new GameProfile.Property("textures", base64Texture, ""))));
        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(DataComponents.PROFILE, profile)
                .build();
    }

    public static boolean isMinionItem(ItemStack itemStack) {
        return itemStack != null && itemStack.getTag(MINION_TYPE_TAG) != null && itemStack.getTag(MINION_TIER_TAG) != null;
    }

    public static MinionType getType(ItemStack itemStack) {
        String id = itemStack.getTag(MINION_TYPE_TAG);
        return id == null ? null : MinionType.fromId(id);
    }

    public static int getTier(ItemStack itemStack) {
        Integer tier = itemStack.getTag(MINION_TIER_TAG);
        return tier == null ? 1 : tier;
    }

    public static String roman(int value) {
        return switch (value) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            case 11 -> "XI";
            case 12 -> "XII";
            default -> String.valueOf(value);
        };
    }

    private static Component text(String value) {
        return MINI_MESSAGE.deserialize(value).decoration(TextDecoration.ITALIC, false);
    }
}
