package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

/**
 * Toggles drop alert messages on/off.
 * Based on HypixelSkyBlock's ToggleDropAlertCommand.
 */
public class ToggleDropAlertCommand extends Command {

    public ToggleDropAlertCommand() {
        super("toggledropalert", "dropalert");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;

            boolean currentlyDisabled = player.hasDropAlertsDisabled();
            player.setDropAlertsDisabled(!currentlyDisabled);

            if (!currentlyDisabled) {
                player.sendMessage("§aDrop alerts toggled §cOFF§a!");
            } else {
                player.sendMessage("§aDrop alerts toggled §aON§a!");
            }
        });
    }
}
