package fun.ascent.skyblock.dungeon;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;

public class DungeonHubServiceRegistry {
    @Getter
    private static InstanceContainer instance;
    @Getter
    private static Pos spawnPos;

    public static void register(InstanceContainer inst, Pos spawn) {
        instance = inst;
        spawnPos = spawn;
    }
}
