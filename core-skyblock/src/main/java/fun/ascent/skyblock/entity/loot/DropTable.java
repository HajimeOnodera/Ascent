package fun.ascent.skyblock.entity.loot;

import fun.ascent.skyblock.player.SkyblockPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class DropTable {

    public abstract List<MobDrop> drops();

    public final List<MobDrop> roll() {
        List<MobDrop> result = new ArrayList<>();
        for (MobDrop drop : drops()) {
            if (drop.rolls()) {
                result.add(drop);
            }
        }
        return result;
    }

    public final List<MobDrop> roll(SkyblockPlayer killer) {
        List<MobDrop> result = new ArrayList<>();
        for (MobDrop drop : drops()) {
            if (drop.rolls(killer)) {
                result.add(drop);
            }
        }
        return result;
    }
}
