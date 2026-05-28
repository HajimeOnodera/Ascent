package fun.ascent.skyblock.dungeon;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;

public class DungeonServiceImpl implements DungeonService {

    @Override
    public void startDungeon(SkyblockPlayer player, String floorInput) {
        try {
            DungeonFloor floor = DungeonFloor.fromString(floorInput);
            if (floor == null) {
                try {
                    floor = DungeonFloor.valueOf(floorInput.toUpperCase());
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§cUnknown floor: " + floorInput);
                    return;
                }
            }
            DungeonInstance dungeon = DungeonManager.get().createDungeon(floor);
            DungeonManager.get().addPlayer(player, dungeon);
            player.sendMessage("§aTest dungeon " + floor.displayName() + ". Teleporting...");
        } catch (Exception e) {
            player.sendMessage("§cError creating dungeon: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void handleDroomCommand(SkyblockPlayer player, String action, String name) {
        DroomCommandExecutor.execute(player, action, name);
    }

    @Override
    public void initialize() {
        DungeonManager.get().initialize();
    }

    @Override
    public void handlePlayerJoinPre(SkyblockPlayer player, AsyncPlayerConfigurationEvent event) {
        try {
            DungeonInstance dungeon = DungeonManager.get().createDungeon(DungeonFloor.FLOOR_7);
            DungeonManager.get().trackPlayer(player.getUuid(), dungeon);
            event.setSpawningInstance(dungeon.instance());
            player.setRespawnPoint(dungeon.spawnPosition());
        } catch (Exception e) {
            System.err.println("[Dungeon] Error handling player spawn on Dungeon server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getDungeonName(java.util.UUID playerUuid, Instance playerInstance) {
        try {
            DungeonInstance dungeon = DungeonManager.get().getDungeon(playerUuid);
            if (dungeon != null && dungeon.instance() != null && dungeon.instance().equals(playerInstance)) {
                return "<gray> ⏣</gray> <red>The Catacombs <gray>(" + dungeon.floor().shortName() + ")";
            }
        } catch (Exception ignored) {}
        return null;
    }
}
