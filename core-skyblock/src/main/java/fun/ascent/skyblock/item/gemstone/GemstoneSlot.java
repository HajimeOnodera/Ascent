package fun.ascent.skyblock.item.gemstone;

/**
 * Represents a gemstone slot on an item. When empty, gemstone and quality are null.
 */
public class GemstoneSlot {

    private final GemstoneSlotType type;
    private final GemstoneType gemstone;   // null when empty
    private final GemstoneQuality quality; // null when empty
    private final boolean unlocked;

    public GemstoneSlot(GemstoneSlotType type, GemstoneType gemstone, GemstoneQuality quality, boolean unlocked) {
        this.type = type;
        this.gemstone = gemstone;
        this.quality = quality;
        this.unlocked = unlocked;
    }

    /**
     * Creates an empty slot.
     */
    public GemstoneSlot(GemstoneSlotType type, boolean unlocked) {
        this(type, null, null, unlocked);
    }

    public GemstoneSlotType getType() { return type; }
    public GemstoneType getGemstone() { return gemstone; }
    public GemstoneQuality getQuality() { return quality; }
    public boolean isUnlocked() { return unlocked; }
    public boolean isEmpty() { return gemstone == null; }

    /**
     * Returns a new GemstoneSlot with a gemstone applied.
     */
    public GemstoneSlot withGemstone(GemstoneType gemstone, GemstoneQuality quality) {
        return new GemstoneSlot(type, gemstone, quality, unlocked);
    }

    /**
     * Returns a new empty GemstoneSlot (gemstone removed).
     */
    public GemstoneSlot cleared() {
        return new GemstoneSlot(type, null, null, unlocked);
    }

    /**
     * Returns a new GemstoneSlot with unlocked set to true.
     */
    public GemstoneSlot unlocked() {
        return new GemstoneSlot(type, gemstone, quality, true);
    }
}
