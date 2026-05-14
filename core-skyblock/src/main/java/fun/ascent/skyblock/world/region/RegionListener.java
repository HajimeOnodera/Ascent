package fun.ascent.skyblock.world.region;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.tag.Tag;

import static fun.ascent.common.StringUtility.text;

public class RegionListener {
    private static final Tag<String> LAST_REGION = Tag.String("last_region");

    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerMoveEvent.class, event -> {
            if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;

            Region current = RegionManager.getRegion(player.getInstance(), event.getNewPosition());
            String lastRegionId = player.getTag(LAST_REGION);
            String currentId = current != null ? current.getId() : "none";

            if (!currentId.equals(lastRegionId)) {
                if (current != null) {
                    player.sendMessage(text("<gray>Entering <white>" + current.getType().toString()));
                    player.sendActionBar(text("<gray>Entering <white>" + current.getType().toString()));
                }
                player.setTag(LAST_REGION, currentId);
            }
        });
    }
}
