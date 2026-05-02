package fun.ascent.skyblock.entity.display;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;

import java.util.function.Consumer;

public class FloatingTextEntity extends Entity {

    public FloatingTextEntity(Component text, Consumer<TextDisplayMeta> setup) {
        super(EntityType.TEXT_DISPLAY);

        editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setText(text);
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
            meta.setHasNoGravity(true);
            meta.setSeeThrough(true);
            setup.accept(meta);
        });
    }

    public void animateRise(float blocks, int durationTicks) {
        editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setTransformationInterpolationDuration(durationTicks);
            meta.setTransformationInterpolationStartDelta(0);
            meta.setTranslation(new Vec(0, blocks, 0));
        });
    }
}