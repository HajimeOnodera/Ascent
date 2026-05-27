package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.item.ItemStack;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear");
        setDefaultExecutor((sender, _) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            player.getInventory().clear();
        });
        var item = ArgumentType.ItemStack("item");
        addSyntax((sender, commandContext) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            ItemStack stack = commandContext.get(item);
            for(int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack stack1 = player.getInventory().getItemStack(i);
                if(stack1.isSimilar(stack)) {
                    player.getInventory().setItemStack(i,ItemStack.AIR);
                }
            }
        },item);
    }
}
