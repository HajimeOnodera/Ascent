package fun.ascent.common.world;

import net.minestom.server.instance.Instance;
import net.minestom.server.tag.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class WorldRegistry {
    public static final Tag<String> WORLD_ID_TAG = Tag.String("world");

    private final Map<String, UUID> worlds = new HashMap<>();

    public void register(String worldName, Instance instance) {
        worlds.put(worldName, instance.getUuid());
    }

    public boolean contains(String worldName) {
        return worlds.containsKey(worldName);
    }

    public Optional<UUID> uuid(String worldName) {
        return Optional.ofNullable(worlds.get(worldName));
    }

    public Optional<String> name(Instance instance) {
        if (instance == null) {
            return Optional.empty();
        }

        return worlds.entrySet().stream()
                .filter(entry -> entry.getValue().equals(instance.getUuid()))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public Map<String, UUID> worlds() {
        return Map.copyOf(worlds);
    }
}
