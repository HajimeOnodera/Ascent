package fun.ascent.common.npc;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class NpcManager<T extends AscentNpc> {

    private final Set<T> npcs = new HashSet<>();

    public void registerListeners(GlobalEventHandler handler) {
        handler.addListener(PlayerEntityInteractEvent.class, event -> getByEntity(event.getTarget())
                .ifPresent(npc -> npc.interact(event.getPlayer())));
    }

    public void register(T npc) {
        if (npc == null || getById(npc.id()).isPresent()) {
            return;
        }

        npcs.add(npc);
        npc.spawn();
    }

    public Set<T> npcs() {
        return Collections.unmodifiableSet(npcs);
    }

    public Optional<T> getById(String id) {
        if (id == null) {
            return Optional.empty();
        }

        return npcs.stream()
                .filter(npc -> npc.id() != null && npc.id().equalsIgnoreCase(id))
                .findFirst();
    }

    public Optional<T> getByEntity(Entity entity) {
        return npcs.stream()
                .filter(npc -> npc.matches(entity))
                .findFirst();
    }

    public void removeById(String id) {
        getById(id).ifPresent(npc -> {
            npc.remove();
            npcs.remove(npc);
        });
    }
}
