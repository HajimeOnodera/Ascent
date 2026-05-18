package fun.ascent.skyblock.player.level.command;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.gui.SkyblockLevelMenu;
import net.minestom.server.command.builder.Command;

public class LevelsCommand extends Command {

    public LevelsCommand() {
        super("sblevels", "sblevel", "levels", "level");

        setDefaultExecutor((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            SkyblockLevelMenu.open(player);
        });
    }
}
