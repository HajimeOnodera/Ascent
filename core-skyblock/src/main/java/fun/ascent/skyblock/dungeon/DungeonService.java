package fun.ascent.skyblock.dungeon;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;

public interface DungeonService {
    void startDungeon(SkyblockPlayer player, String floorInput);
    void handleDroomCommand(SkyblockPlayer player, String action, String name);
    void initialize();
    void handlePlayerJoinPre(SkyblockPlayer player, AsyncPlayerConfigurationEvent event);
    String getDungeonName(java.util.UUID playerUuid, Instance playerInstance);
}
