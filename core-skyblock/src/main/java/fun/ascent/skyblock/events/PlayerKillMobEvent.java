package fun.ascent.skyblock.events;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.Event;

public class PlayerKillMobEvent implements Event {
    private final SkyblockPlayer player;
    private final SkyblockMobEntity killedMob;

    public PlayerKillMobEvent(SkyblockPlayer player, SkyblockMobEntity killedMob) {
        this.player = player;
        this.killedMob = killedMob;
    }

    public SkyblockPlayer getPlayer() {
        return player;
    }

    public SkyblockMobEntity getKilledMob() {
        return killedMob;
    }
}
