package fun.ascent.skyblock.bazaar.command;

import fun.ascent.skyblock.bazaar.menu.BazaarMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import static fun.ascent.common.StringUtility.text;

public class BazaarCommand extends Command {

    public BazaarCommand() {
        super("bazaar", "bz");

        setDefaultExecutor((sender, ctx) -> {
            if (sender instanceof Player player) {
                // Ensure the player is an instance of SkyblockPlayer or cast appropriately if required by the framework
                if (player instanceof SkyblockPlayer sp) {
                    BazaarMenu.open(sp);
                } else {
                    // Fallback if the framework casts player differently
                    player.sendMessage(text("<red>You must be a SkyblockPlayer to open the Bazaar."));
                }
            } else {
                sender.sendMessage(text("<red>Only players can use this command."));
            }
        });
    }
}
