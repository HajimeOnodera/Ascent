package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import net.minestom.server.command.builder.Command;

public class IslandCommand extends Command {

    public IslandCommand() {
        super("island","is");
        setDefaultExecutor((sender,_) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            SkyblockProfile profile = player.getActiveProfile();
            if(profile == null){
                System.out.println("[PROFILE] Profile is NULL");
                return;
            }
            if(profile.island == null || profile.island.getInstance() == null){
                System.out.println("[PROFILE] Instance is NULL");
                return;
            }
            player.setInstance(profile.island.getInstance(),profile.getSpawnPos());
        });
    }
}
