package fun.ascent.skyblock.entity.display;

import lombok.Getter;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.item.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DroppedItemEntity extends Entity {

    @Getter
    private static final Map<Player, List<DroppedItemEntity>> droppedItems = new HashMap<>();

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

        // Track per-player dropped items with a 50-item cap
        droppedItems.computeIfPresent(owner, (key, value) -> {
            if (value.size() > 50) {
                value.getFirst().remove();
                value.removeFirst();
            }
            value.add(this);
            return value;
        });
        droppedItems.putIfAbsent(owner, new ArrayList<>(List.of(this)));
    }

    @Override
    public void spawn() {
        super.spawn();
        addViewer(owner);
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
