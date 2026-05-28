package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.menus.gui.profiles.GUIProfileManagement;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class ProfilesCommand extends Command {

    public ProfilesCommand() {
        super("profiles", "profile");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            new GUIProfileManagement().open(player);
        });
    }
}
