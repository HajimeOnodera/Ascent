package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.item.gui.ItemCategoryGUI;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class ItemListCommand extends Command {

    public ItemListCommand() {
        super("itemlist");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("This command can only be used by players.");
                return;
            }

            new ItemCategoryGUI().open(player);
        });
    }
}
