package fun.ascent.skyblock.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcManager;
import net.minestom.server.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class SkyblockNPCManager {
    private static final NpcManager<AscentNpc> MANAGER = new NpcManager<>();
    private static final Set<AscentNpc> SKYBLOCK_NPCS = new HashSet<>();

    public static void init() {
        MANAGER.registerListeners(net.minestom.server.MinecraftServer.getGlobalEventHandler());
    }

    public static void registerNPC(AscentNpc npc) {
        SKYBLOCK_NPCS.add(npc);
        MANAGER.register(npc);
    }

    public static Set<AscentNpc> getNPCS() {
        return SKYBLOCK_NPCS;
    }

    public static AscentNpc getNPCbyID(String id) {
        if (id == null) return null;
        return MANAGER.getById(id).orElse(null);
    }

    public static AscentNpc getNPCByEntity(Entity minestomEntity) {
        if (minestomEntity == null) return null;
        return MANAGER.getByEntity(minestomEntity).orElse(null);
    }

    public static void removeNPCbyID(String id) {
        if (id == null) return;
        SKYBLOCK_NPCS.removeIf(npc -> {
            if (npc.id() != null && npc.id().equals(id)) {
                MANAGER.removeById(id);
                return true;
            }
            return false;
        });
    }
}
