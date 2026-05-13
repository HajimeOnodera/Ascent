package fun.ascent.skyblock.player.collections;

import lombok.Getter;
import net.minestom.server.item.Material;

import java.util.List;

@Getter
public abstract class CollectionCategory {
    private final String name;
    private final Material icon;
    private final CollectionType type;

    public CollectionCategory(String name, Material icon, CollectionType type) {
        this.name = name;
        this.icon = icon;
        this.type = type;
    }

    public abstract List<ItemCollection> getCollections();

    public record ItemCollection(String itemId, String name, Material icon, List<CollectionReward> rewards) {
        public CollectionReward getRewardAtTier(int tier) {
            if (tier <= 0 || tier > rewards.size()) return null;
            return rewards.get(tier - 1);
        }

        public int getTotalTiers() {
            return rewards.size();
        }

        public int getTierFromProgress(int progress) {
            int tier = 0;
            for (CollectionReward reward : rewards) {
                if (progress >= reward.requirement()) {
                    tier++;
                } else {
                    break;
                }
            }
            return tier;
        }
    }

    public record CollectionReward(int requirement, List<CollectionUnlock> unlocks) {
    }

    @Getter
    public enum CollectionType {
        FARMING("Farming"),
        MINING("Mining"),
        COMBAT("Combat"),
        FORAGING("Foraging"),
        FISHING("Fishing"),
        BOSS("Boss"),
        OTHER("Other");

        private final String displayName;

        CollectionType(String displayName) {
            this.displayName = displayName;
        }

    }
}
