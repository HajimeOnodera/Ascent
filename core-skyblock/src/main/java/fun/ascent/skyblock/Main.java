package fun.ascent.skyblock;

import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

public class Main {

    static void main(String[] args){

        MinecraftServer server = MinecraftServer.init(new Auth.Offline());
        server.start("0.0.0.0", 25565);
    }

}
