package fun.ascent.skyblock.hotm;

public enum Powder {
    MITHRIL("mithril", "§2Mithril Powder", "§2"),
    GEMSTONE("gemstone", "§dGemstone Powder", "§d"),
    GLACIAL("glacial", "§bGlacial Powder", "§b");

    private final String id;
    private final String displayName;
    private final String color;

    Powder(String id, String displayName, String color) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public String color() { return color; }
}
