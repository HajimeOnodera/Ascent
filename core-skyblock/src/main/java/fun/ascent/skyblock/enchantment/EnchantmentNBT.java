package fun.ascent.skyblock.enchantment;

import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EnchantmentNBT {

    private static final String ENCHANTMENTS_TAG = "enchantments";

    private EnchantmentNBT() {}

    public static Map<String, Integer> getEnchantments(ItemStack stack) {
        Map<String, Integer> result = new LinkedHashMap<>();
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return result;
        CompoundBinaryTag root = data.nbt();
        if (!root.keySet().contains(ENCHANTMENTS_TAG)) return result;
        CompoundBinaryTag enchTag = root.getCompound(ENCHANTMENTS_TAG);
        for (String key : enchTag.keySet()) {
            if (enchTag.get(key) instanceof IntBinaryTag intTag) {
                result.put(key, intTag.intValue());
            }
        }
        return result;
    }

    public static boolean hasEnchantment(ItemStack stack, EnchantmentRegistry registry) {
        return getEnchantments(stack).containsKey(registry.getKey());
    }

    public static int getEnchantmentLevel(ItemStack stack, EnchantmentRegistry registry) {
        return getEnchantments(stack).getOrDefault(registry.getKey(), 0);
    }

    public static ItemStack setEnchantments(ItemStack stack, SkyblockPlayer player, Map<String, Integer> enchantments) {
        SkyblockItem template = EnchantingItemResolver.resolve(stack);
        if (template == null) return stack;

        boolean recombobulated = ItemNBT.isRecombobulated(stack);
        int hotPotatoCount = ItemNBT.getHotPotatoCount(stack);
        boolean artOfPeace = ItemNBT.hasArtOfPeace(stack);
        String modifier = ItemNBT.getModifier(stack);

        SkyblockItem.Builder builder = template.toBuilder()
                .recombobulated(recombobulated)
                .hotPotatoCount(hotPotatoCount)
                .artOfPeace(artOfPeace);

        builder.clearEnchantments();
        enchantments.forEach((name, level) -> {
            EnchantmentRegistry registry = EnchantmentRegistry.fromKey(name);
            builder.enchantment(registry != null ? registry.getDisplayName() : name, level);
        });

        if (modifier != null) {
            Reforge reforge = Reforge.getById(modifier.toUpperCase(), template.getItemType());
            if (reforge != null) {
                Rarity effectiveRarity = (recombobulated && template.getRarity().getNextRarity() != null)
                        ? template.getRarity().getNextRarity()
                        : template.getRarity();
                builder.modifier(reforge.getName()).reforgeLore(reforge.getLoreText());
                for (Map.Entry<Stats, RarityStat> entry : reforge.getStats().entrySet()) {
                    builder.reforgeStat(entry.getKey(), entry.getValue().fromRarity(effectiveRarity));
                }
            }
        }

        return builder.build().buildItemStack(player, false);
    }

    public static ItemStack applyEnchantment(ItemStack stack, SkyblockPlayer player, EnchantmentRegistry registry, int level) {
        Map<String, Integer> enchantments = new LinkedHashMap<>(getEnchantments(stack));
        enchantments.put(registry.getKey(), level);
        return setEnchantments(stack, player, enchantments);
    }

    public static ItemStack removeEnchantment(ItemStack stack, SkyblockPlayer player, EnchantmentRegistry registry) {
        Map<String, Integer> enchantments = new LinkedHashMap<>(getEnchantments(stack));
        enchantments.remove(registry.getKey());
        return setEnchantments(stack, player, enchantments);
    }
}
