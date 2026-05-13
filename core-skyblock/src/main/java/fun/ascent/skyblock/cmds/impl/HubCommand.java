package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.command.builder.Command;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub");

        setDefaultExecutor((sender,_) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            if(WorldHandler.getLobby() == null){
                System.out.println("[WORLD] Lobby World is NULL");
                player.sendMessage("§cHub is currently unavailable!");
                return;
            }
            player.sendMessage("§aTeleporting to Hub...");
            player.setInstance(WorldHandler.getLobby(), WorldHandler.getLobbySpawn());
        });
    }
}
