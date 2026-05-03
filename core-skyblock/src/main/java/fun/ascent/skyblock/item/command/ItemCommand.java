package fun.ascent.skyblock.item.command;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

public class ItemCommand extends Command {

    public ItemCommand() {
        super("item");

        setDefaultExecutor((sender, ctx) -> sender.sendMessage("§cUsage: /item <id>"));

        ArgumentWord idArg = ArgumentType.Word("id");

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player player)) return;

            String id = ctx.get(idArg).toUpperCase();
            SkyblockItem item = ItemRegistry.getItem(id);

            if (item == null) {
                player.sendMessage("§cUnknown item: §f" + id);
                return;
            }

            player.getInventory().addItemStack(item.buildItemStack());
            player.sendMessage("§aGave §f" + id + "§a.");
        }, idArg);
    }
}
