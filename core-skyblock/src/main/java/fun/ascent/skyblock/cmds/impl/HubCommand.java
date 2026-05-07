package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.command.builder.Command;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub","lobby");

        setDefaultExecutor((sender,_) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            if(WorldHandler.getLobby() == null){
                System.out.println("[WORLD] Lobby World is NULL");
                return;
            }
            player.setInstance(WorldHandler.getLobby(),WorldHandler.getLobbySpawn());
        });
    }
}
