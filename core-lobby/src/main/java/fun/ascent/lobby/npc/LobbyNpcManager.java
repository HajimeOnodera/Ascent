package fun.ascent.lobby.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;

import java.util.HashSet;
import java.util.Set;

public final class LobbyNpcManager {

    private final Instance instance;
    private final Set<LobbyNpc> npcs = new HashSet<>();

    public LobbyNpcManager(Instance instance) {
        this.instance = instance;
    }

    public void spawnDefaults() {
        register(new LobbyNpc("skyblock", "SkyBlock", "skyblock", new Pos(0.5, 41, 6.5, 180, 0), instance));
    }

    public void registerListeners(GlobalEventHandler handler) {
        handler.addListener(PlayerEntityInteractEvent.class, event -> npcs.stream()
                .filter(npc -> npc.is(event.getTarget()))
                .findFirst()
                .ifPresent(npc -> npc.interact(event.getPlayer())));
    }

    private void register(LobbyNpc npc) {
        if (npcs.stream().anyMatch(existing -> existing.id().equalsIgnoreCase(npc.id()))) {
            return;
        }

        npcs.add(npc);
        npc.spawn();
    }
}
