package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.menus.gui.banker.GUIBanker;
import net.minestom.server.command.builder.Command;

public class BankCommand extends Command {

    public BankCommand() {
        super("bank");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) {
                sender.sendMessage("This command can only be run by players.");
                return;
            }
            new GUIBanker().open(player);
        });
    }
}
