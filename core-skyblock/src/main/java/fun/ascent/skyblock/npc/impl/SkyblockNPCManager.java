package fun.ascent.skyblock.npc.impl;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.player.PlayerEntityInteractEvent;

import java.util.HashSet;
import java.util.Set;

public class SkyblockNPCManager {
    private static final Set<SkyBlockNPC> SKYBLOCK_NPCS = new HashSet<>();

    public static void init() {
        EventManager.registerEvent(new SEvent<PlayerEntityInteractEvent>() {
            @Override
            public void onEvent(PlayerEntityInteractEvent event) {
                SkyBlockNPC npc = getNPCByEntity(event.getTarget());
                if (npc != null) {
                    npc.getParameters().onInteract(event.getPlayer(), npc);
                }
            }
        });
    }

    public static void registerNPC(SkyBlockNPC skyblockNPC) {
        SKYBLOCK_NPCS.add(skyblockNPC);
    }

    public static Set<SkyBlockNPC> getNPCS() {
        return SKYBLOCK_NPCS;
    }

    public static SkyBlockNPC getNPCbyID(String id) {
        if (id == null) return null;
        return SKYBLOCK_NPCS.stream()
                .filter(npc -> npc.getId() != null && npc.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public static SkyBlockNPC getNPCByEntity(Entity minestomEntity) {
        if (minestomEntity == null) return null;
        return SKYBLOCK_NPCS.stream()
                .filter(npc -> npc.getEntity() != null && npc.getEntity().getUuid().equals(minestomEntity.getUuid()))
                .findFirst()
                .orElse(null);
    }

    public static void removeNPCbyID(String id) {
        if (id == null) return;
        SKYBLOCK_NPCS.removeIf(npc -> {
            if (npc.getId() != null && npc.getId().equals(id)) {
                if (npc.getEntity() != null) {
                    npc.getEntity().remove();
                }
                npc.removeHolograms();
                return true;
            }
            return false;
        });
    }
}
