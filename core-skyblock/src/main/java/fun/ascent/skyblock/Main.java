package fun.ascent.skyblock;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldManager;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

import fun.ascent.skyblock.npc.impl.SkyblockNPCManager;

public class Main {

    public static void main(String[] args){
        MinecraftServer server = MinecraftServer.init(new Auth.Offline());
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);
        WorldManager.initialise();
        EventManager.initialise();
        SkyblockNPCManager.init();

        System.out.println("[Skyblock] Starting the Server ");
        server.start("0.0.0.0",25565);
        System.out.println("[Skyblock] Started the Server ");
    }

}
