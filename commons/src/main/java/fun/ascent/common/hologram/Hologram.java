package fun.ascent.common.hologram;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class Hologram {

    private static final double LINE_SPACING = 0.3;
    private final List<Entity> lines = new ArrayList<>();
    private final Instance instance;
    @Getter
    private Pos position;

    public Hologram(Instance instance, Pos position) {
        this.instance = instance;
        this.position = position;
    }

    public void setLines(String... textLines) {
        remove();
        double yOffset = 0;
        for (String line : textLines) {
            if (line == null) {
                yOffset -= LINE_SPACING;
                continue;
            }

            Entity armorStand = new Entity(EntityType.ARMOR_STAND);
            ArmorStandMeta meta = (ArmorStandMeta) armorStand.getEntityMeta();
            meta.setInvisible(true);
            meta.setHasNoBasePlate(true);
            meta.setMarker(true);

            armorStand.setCustomName(text(line));
            armorStand.setCustomNameVisible(true);
            armorStand.setNoGravity(true);
            armorStand.setInstance(instance, position.add(0, yOffset, 0));

            lines.add(armorStand);
            yOffset -= LINE_SPACING;
        }
    }

    public void updateLine(int index, String text) {
        if (index < 0 || index >= lines.size()) return;
        lines.get(index).setCustomName(text(text));
    }

    public void teleport(Pos newPos) {
        this.position = newPos;
        double yOffset = 0;
        for (Entity line : lines) {
            line.teleport(position.add(0, yOffset, 0));
            yOffset -= LINE_SPACING;
        }
    }

    public void remove() {
        for (Entity line : lines) {
            line.remove();
        }
        lines.clear();
    }
}
