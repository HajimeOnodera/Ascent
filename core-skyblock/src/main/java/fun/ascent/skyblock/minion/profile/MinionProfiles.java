package fun.ascent.skyblock.minion.profile;

import fun.ascent.skyblock.minion.model.MinionType;

import java.util.EnumMap;
import java.util.Map;

public final class MinionProfiles {
    private static final Map<MinionType, MinionProfile> PROFILES = new EnumMap<>(MinionType.class);

    static {
        for (MinionType type : MinionType.values()) {
            PROFILES.put(type, new BasicMinionProfile(
                    type,
                    type.getIcon(),
                    type.getOutputMaterial(),
                    type.getBaseBlock(),
                    type.getGeneratedBlock(),
                    type.getIdealLayoutBlock(),
                    type.getSecondaryGeneratedBlock(),
                    type.getMobEntityType(),
                    type.getTexture(),
                    type.getArmorColor(),
                    type.getPlacementDescription(),
                    type.getLayoutHint(),
                    type.getIdealLayout(),
                    type.getTierData(1).storageSlots()
            ));
        }
    }

    private MinionProfiles() {
    }

    public static MinionProfile get(MinionType type) {
        return PROFILES.get(type);
    }
}
