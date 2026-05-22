package fun.ascent.skyblock.events;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.event.Event;

public class PlayerRegionChangeEvent implements Event {
    private final SkyblockPlayer player;
    private final RegionType from;
    private final RegionType to;

    public PlayerRegionChangeEvent(SkyblockPlayer player, RegionType from, RegionType to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public SkyblockPlayer getPlayer() {
        return player;
    }

    public RegionType getFrom() {
        return from;
    }

    public RegionType getTo() {
        return to;
    }
}
