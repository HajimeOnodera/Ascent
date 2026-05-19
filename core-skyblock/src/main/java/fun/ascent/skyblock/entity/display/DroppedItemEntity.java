package fun.ascent.skyblock.entity.display;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.item.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DroppedItemEntity extends Entity {

    private static final Map<Player, List<DroppedItemEntity>> playerDroppedItems = new ConcurrentHashMap<>();

    public DroppedItemEntity(ItemStack item, Player owner) {
        super(EntityType.ITEM);

        editEntityMeta(ItemEntityMeta.class, meta -> meta.setItem(item));
        setAutoViewable(false);
        scheduleRemove(Duration.ofSeconds(60));

        spawn();
        addViewer(owner);

        List<DroppedItemEntity> list = playerDroppedItems.computeIfAbsent(owner, k -> new ArrayList<>());
        if (list.size() > 50) {
            DroppedItemEntity oldest = list.remove(0);
            if (oldest != null && !oldest.isRemoved()) {
                oldest.remove();
            }
        }
        list.add(this);
    }

    @Override
    public void remove() {
        super.remove();
        for (List<DroppedItemEntity> list : playerDroppedItems.values()) {
            list.remove(this);
        }
    }
}
