package fun.ascent.skyblock.island;

import fun.ascent.skyblock.events.definitions.IslandLoadEvent;
import fun.ascent.skyblock.events.definitions.IslandSaveEvent;
import fun.ascent.skyblock.island.npc.IslandJerryNPC;
import fun.ascent.skyblock.minion.base.SkyblockMinion;
import fun.ascent.skyblock.minion.service.MinionManager;
import fun.ascent.skyblock.minion.service.MinionPersistence;
import fun.ascent.skyblock.minion.service.MinionFactory;
import fun.ascent.skyblock.minion.model.MinionType;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.common.npc.AscentNpc;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IslandSystemListener {
    public static void register(GlobalEventHandler handler) {
        handler.addListener(IslandLoadEvent.class, event -> {
            Island island = event.island();
            System.out.println("[IslandSync] Handling Load for island " + island.getIslandId());
            
            // 1. Restore Minions
            if (!island.getMinionData().isEmpty()) {
                for (Document minionDoc : island.getMinionData()) {
                    try {
                        SkyblockMinion minion = MinionPersistence.deserialize(minionDoc, event.instance());
                        if (island.getSpawnedMinionUuids().contains(minion.getId())) continue;

                        minion.spawn();
                        MinionManager.registerMinion(minion);
                        island.getSpawnedMinionUuids().add(minion.getId());
                    } catch (Exception e) {
                        System.err.println("[IslandSync] Failed to restore minion for island " + island.getIslandId());
                        e.printStackTrace();
                    }
                }
            } else {
                // Spawn Default Minion if it's a new island
                System.out.println("[IslandSync] Spawning default Cobblestone minion for " + island.getIslandId());
                Pos minionPos = new Pos(3.5, 100, 36.5, -90, 0);
                SkyblockMinion minion = MinionFactory.create(island.getIslandId(), MinionType.COBBLESTONE, 1, event.instance(), minionPos);
                minion.spawn();
                MinionManager.registerMinion(minion);
                island.getSpawnedMinionUuids().add(minion.getId());
            }

            // 2. Spawn Jerry
            String jerryId = "jerry_" + island.getIslandId();
            if (SkyblockNPCManager.getNPCbyID(jerryId) == null) {
                System.out.println("[IslandSync] Spawning Jerry for island " + island.getIslandId());
                Pos jerryPos = new Pos(9.5, 100, 34, 180, 0);
                AscentNpc jerry = new AscentNpc(new IslandJerryNPC(event.instance(), jerryPos, jerryId));
                SkyblockNPCManager.registerNPC(jerry);
            }
        });

        handler.addListener(IslandSaveEvent.class, event -> {
            Island island = event.island();
            System.out.println("[IslandSync] Handling Save for island " + island.getIslandId());
            
            // Save Minions
            List<Document> minionDocs = new ArrayList<>();
            Collection<SkyblockMinion> minions = MinionManager.getOwnedMinions(island.getIslandId());
            for (SkyblockMinion minion : minions) {
                if (minion.getInstance() == island.getInstance()) {
                    minionDocs.add(MinionPersistence.serialize(minion));
                }
            }
            island.setMinionData(minionDocs);
            
            // Clean up internal tracking so they can respawn on next load
            island.getSpawnedMinionUuids().clear();
            island.getSpawnedNpcIds().clear();
        });
    }
}
