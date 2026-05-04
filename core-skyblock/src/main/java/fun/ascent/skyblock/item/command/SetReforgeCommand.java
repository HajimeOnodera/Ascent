package fun.ascent.skyblock.item.command;

import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

import java.util.Map;

public class SetReforgeCommand extends Command {

    public SetReforgeCommand() {
        super("setreforge");

        setDefaultExecutor((sender, ctx) -> sender.sendMessage("§cUsage: /setreforge <reforge>"));

        ArgumentWord reforgeArg = ArgumentType.Word("reforge");

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player player)) return;

            ItemStack held = player.getEquipment(EquipmentSlot.MAIN_HAND);
            if (held.isAir()) {
                player.sendMessage("§cYou must hold an item!");
                return;
            }

            String itemId = ItemNBT.getItemId(held);
            if (itemId == null) {
                player.sendMessage("§cNot a Skyblock item!");
                return;
            }

            SkyblockItem base = ItemRegistry.getItem(itemId);
            if (base == null) {
                player.sendMessage("§cItem not found in registry!");
                return;
            }

            if (!base.isReforgeable()) {
                player.sendMessage("§cThis item cannot be reforged!");
                return;
            }

            String reforgeName = ctx.get(reforgeArg).toUpperCase();
            Reforge reforge = Reforge.getById(reforgeName, base.getItemType());
            if (reforge == null) {
                player.sendMessage("§cUnknown reforge: §f" + reforgeName);
                return;
            }

            if (!reforge.canApplyTo(base.getItemType())) {
                player.sendMessage("§cCannot apply §f" + reforge.getName() + "§c to this item type!");
                return;
            }

            SkyblockItem.Builder builder = base.toBuilder()
                    .modifier(reforge.getName())
                    .reforgeLore(reforge.getLoreText());
            for (Map.Entry<Stats, RarityStat> entry : reforge.getStats().entrySet()) {
                double bonus = entry.getValue().fromRarity(base.getRarity());
                builder.reforgeStat(entry.getKey(), bonus);
            }

            player.setEquipment(EquipmentSlot.MAIN_HAND, builder.build().buildItemStack(player));
            player.sendMessage("§aApplied §f" + reforge.getName() + "§a reforge.");
        }, reforgeArg);
    }
}
