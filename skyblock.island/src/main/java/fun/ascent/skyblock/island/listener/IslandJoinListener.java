package fun.ascent.skyblock.island.listener;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.skyblock.island.npc.JerryNPC;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;

public class IslandJoinListener {

    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerSpawnEvent.class, event -> {
            if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;
            
            String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
            if (!serverType.equalsIgnoreCase("ISLAND")) return;

            Instance instance = event.getInstance();
            spawnJerry(player, instance);
        });
    }

    private static void spawnJerry(SkyblockPlayer player, Instance instance) {
        if (player.getActiveProfile() == null) return;

        java.util.UUID profileID = player.getActiveProfile().profileID;
        Pos jerryPos = new Pos(9.5, 100, 34, 180, 0);
        String jerryId = "jerry_" + profileID;

        if (SkyblockNPCManager.getNPCbyID(jerryId) == null) {
            System.out.println("[IslandJoin] Spawning Jerry for profile " + profileID);
            AscentNpc jerry = new AscentNpc(new JerryNPC(instance, jerryPos, jerryId));
            SkyblockNPCManager.registerNPC(jerry);
            jerry.spawn();
        }
    }
}
