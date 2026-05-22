package fun.ascent.skyblock.events;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.Event;

public class JerryClickedEvent implements Event {
    private final SkyblockPlayer player;

    public JerryClickedEvent(SkyblockPlayer player) {
        this.player = player;
    }

    public SkyblockPlayer getPlayer() {
        return player;
    }
}
