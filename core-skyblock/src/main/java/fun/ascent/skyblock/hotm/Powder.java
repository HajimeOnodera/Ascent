package fun.ascent.skyblock.hotm;

public enum Powder {
    MITHRIL("mithril", "<dark_green>Mithril Powder", "<dark_green>"),
    GEMSTONE("gemstone", "<light_purple>Gemstone Powder", "<light_purple>"),
    GLACIAL("glacial", "<aqua>Glacial Powder", "<aqua>");

    private final String id;
    private final String displayName;
    private final String colorTag;

    Powder(String id, String displayName, String colorTag) {
        this.id = id;
        this.displayName = displayName;
        this.colorTag = colorTag;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public String colorTag() { return colorTag; }
}

