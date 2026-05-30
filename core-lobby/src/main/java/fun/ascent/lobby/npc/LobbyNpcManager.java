package fun.ascent.lobby.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.Instance;

public final class LobbyNpcManager {

    private final Instance instance;
    private final NpcManager<AscentNpc> npcs = new NpcManager<>();

    public LobbyNpcManager(Instance instance) {
        this.instance = instance;
    }

    public void spawnDefaults() {
        register(new SkyblockNPC(instance));
        register(new AscentStoreNPC(instance));
    }

    /**
     * Register any NpcDefinition — creates an AscentNpc and spawns it.
     */
    public void register(NpcDefinition definition) {
        npcs.register(new AscentNpc(definition));
    }

    public void registerListeners(GlobalEventHandler handler) {
        npcs.registerListeners(handler);
    }
}
