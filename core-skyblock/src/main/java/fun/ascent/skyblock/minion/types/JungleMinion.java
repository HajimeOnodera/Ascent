package fun.ascent.skyblock.minion;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class JungleMinion extends TreeMinion {
    public JungleMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.JUNGLE, tier, instance, position);
    }
}
