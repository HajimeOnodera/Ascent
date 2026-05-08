package fun.ascent.skyblock.item.command;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import static fun.ascent.common.StringUtility.text;

public class ItemCommand extends Command {

    public ItemCommand() {
        super("item");

        setDefaultExecutor((sender, ctx) -> sender.sendMessage(text("<red>Usage: /item <id>")));

        ArgumentWord idArg = ArgumentType.Word("id");

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player player)) return;

            String id = ctx.get(idArg).toUpperCase();
            SkyblockItem item = ItemRegistry.getItem(id);

            if (item == null) {
                player.sendMessage(text("<red>Unknown item: <white>" + id));
                return;
            }

            player.getInventory().addItemStack(item.buildItemStack());
            player.sendMessage(text("<green>Gave <white>" + id + "<green>."));
        }, idArg);
    }
}

