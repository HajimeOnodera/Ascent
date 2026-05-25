package fun.ascent.skyblock.bazaar.cmd;

import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.bazaar.ui.BazaarCategoryMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class BZCommand extends Command {

    public BZCommand() {
        super("bz","bazaar");

        setDefaultExecutor(((sender, context) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            BazaarCategoryMenu.openMenu(player, BazaarRegistry.bazaarData.getFarming());
        }));
    }
}
