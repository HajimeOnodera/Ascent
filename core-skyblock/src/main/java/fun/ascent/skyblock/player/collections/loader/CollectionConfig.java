package fun.ascent.skyblock.player.collections.loader;

import lombok.Data;
import java.util.List;

@Data
public class CollectionConfig {
    private String name;
    private String displayIcon;
    private String displayColor;
    private List<CollectionEntry> collections;

    @Data
    public static class CollectionEntry {
        private String itemType;
        private List<CollectionRewardEntry> rewards;
    }

    @Data
    public static class CollectionRewardEntry {
        private int amount;
        private List<RewardAction> rewards;
    }

    @Data
    public static class RewardAction {
        private String type;
        private RewardData data;
    }

    @Data
    public static class RewardData {
        private String unlockedItemType;
        private Integer xp;
        private String customAward;
    }
}
