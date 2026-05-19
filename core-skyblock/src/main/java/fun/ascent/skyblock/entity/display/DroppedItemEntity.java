package fun.ascent.skyblock.entity.display;

import lombok.Getter;
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

@Getter
public class DroppedItemEntity extends Entity {

    @Getter
    private static final Map<Player, List<DroppedItemEntity>> droppedItems = new ConcurrentHashMap<>();

    private final Player owner;
    private final long endPickupDelay;

    public DroppedItemEntity(ItemStack item, Player owner) {
        super(EntityType.ITEM);

        this.owner = owner;
        this.endPickupDelay = System.currentTimeMillis() + 500; // 500ms pickup delay

        // Set the displayed item
        editEntityMeta(ItemEntityMeta.class, meta -> meta.setItem(item));

        // Only the owner can see this entity
        setAutoViewable(false);

        // Auto-despawn after 60 seconds
        scheduleRemove(Duration.ofSeconds(60));
    }

    @Override
    public void spawn() {
        super.spawn();
        addViewer(owner);

        // Track per-player dropped items with a 50-item cap to protect server performance
        List<DroppedItemEntity> list = droppedItems.computeIfAbsent(owner, k -> new ArrayList<>());
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
        for (List<DroppedItemEntity> list : droppedItems.values()) {
            list.remove(this);
        }
    }

    public ItemStack getDroppedItemStack() {
        return ((ItemEntityMeta) this.entityMeta).getItem();
    }

    public boolean canPickup() {
        return System.currentTimeMillis() > endPickupDelay;
    }

    public static void clearDroppedItems(Player player) {
        List<DroppedItemEntity> items = droppedItems.remove(player);
        if (items != null) {
            items.forEach(Entity::remove);
        }
    }
}
