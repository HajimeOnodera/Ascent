package fun.ascent.skyblock.item.command;

import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;

import java.util.Map;

import static fun.ascent.common.StringUtility.text;

public class GetItemDataCommand extends Command {

    public GetItemDataCommand() {
        super("getitemdata");

        var mode = ArgumentType.Word("mode").from("stat", "nbt");

        setDefaultExecutor((sender, ctx) ->
                sender.sendMessage(text("&cUsage: /getitemdata <stat/nbt>")));

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;

            ItemStack held = player.getEquipment(EquipmentSlot.MAIN_HAND);
            if (held.isAir()) {
                player.sendMessage(text("&cYou are not holding an item."));
                return;
            }

            String act = ctx.get(mode);
            if (act.equals("stat")) {
                showStats(player, held);
            } else {
                showNbt(player, held);
            }
        }, mode);
    }

    private static String legacy(String s) {
        return s.replace('§', '&');
    }

    private void showStats(SkyblockPlayer player, ItemStack held) {
        String itemId = ItemNBT.getItemId(held);
        if (itemId == null) {
            player.sendMessage(text("&cThis item has no skyblock ID."));
            return;
        }

        SkyblockItem base = ItemRegistry.getItem(itemId);
        if (base == null) {
            player.sendMessage(text("&cItem not found in registry: " + itemId));
            return;
        }

        SkyblockItem.Builder builder = base.toBuilder();

        boolean recomb = ItemNBT.isRecombobulated(held);
        if (recomb) builder.recombobulated(true);

        Rarity effectiveRarity = base.getRarity();
        if (recomb && effectiveRarity.getNextRarity() != null) {
            effectiveRarity = effectiveRarity.getNextRarity();
        }

        String modifier = ItemNBT.getModifier(held);
        if (modifier != null) {
            Reforge reforge = Reforge.getById(modifier.toUpperCase(), base.getItemType());
            if (reforge != null) {
                builder.modifier(reforge.getName());
                for (Map.Entry<Stats, RarityStat> entry : reforge.getStats().entrySet()) {
                    double bonus = entry.getValue().fromRarity(effectiveRarity);
                    builder.reforgeStat(entry.getKey(), bonus);
                }
            }
        }

        int hpb = ItemNBT.getHotPotatoCount(held);
        if (hpb > 0) builder.hotPotatoCount(hpb);

        if (ItemNBT.hasArtOfPeace(held)) builder.artOfPeace(true);

        SkyblockItem rebuilt = builder.build();
        Map<Stats, Double> stats = rebuilt.getTotalStats();

        player.sendMessage(text("&6&l     Item Stats &7(" + itemId + ")"));
        player.sendMessage("");
        if (stats.isEmpty()) {
            player.sendMessage(text(" &7No stats."));
            return;
        }
        for (Map.Entry<Stats, Double> entry : stats.entrySet()) {
            Stats stat = entry.getKey();
            double value = entry.getValue();
            String suffix = stat.getStatIntType() ? "%" : "";
            String sign = value > 0 ? "+" : "";
            player.sendMessage(text(" " + legacy(stat.getStatSymbol()) + " &7"
                    + stat.getStatFormattedDisplay() + ": " + legacy(stat.getStatColor())
                    + sign + (int) value + suffix));
        }
    }

    private void showNbt(SkyblockPlayer player, ItemStack held) {
        CustomData customData = held.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            player.sendMessage(text("&cThis item has no custom data."));
            return;
        }

        CompoundBinaryTag nbt = customData.nbt();
        player.sendMessage(text("&6&l     Item NBT"));
        player.sendMessage("");
        for (String key : nbt.keySet()) {
            player.sendMessage(text(" &e" + key + "&7: &f" + nbt.get(key)));
        }
    }
}
