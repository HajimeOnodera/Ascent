package fun.ascent.skyblock.entity.display;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.item.ItemStack;

import java.time.Duration;

public class DroppedItemEntity extends Entity {

    public DroppedItemEntity(ItemStack item, Player owner) {
        super(EntityType.ITEM);

        editEntityMeta(ItemEntityMeta.class, meta -> meta.setItem(item));
        setAutoViewable(false);
        scheduleRemove(Duration.ofSeconds(60));

        spawn();
        addViewer(owner);
    }
}
