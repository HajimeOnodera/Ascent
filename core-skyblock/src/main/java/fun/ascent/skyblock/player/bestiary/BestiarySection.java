package fun.ascent.skyblock.player.bestiary;

import net.minestom.server.item.Material;

import java.util.Arrays;
import java.util.List;

public enum BestiarySection {
    YOUR_ISLAND("Your Island", "<green>", Material.PLAYER_HEAD, "c9c8881e42915a9d29bb61a16fb26d059913204d265df5b439b3d792acd56",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed on <green>Your Island<gray>."
            )),
    HUB("Hub", "<green>", Material.PLAYER_HEAD, "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in the <green>Hub<gray>."
            )),
    THE_FARMING_ISLANDS("The Farming Islands", "<green>", Material.PLAYER_HEAD, "4d3a6bd98ac1833c664c4909ff8d2dc62ce887bdcf3cc5b3848651ae5af6b",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in <green>The Farming",
                    "<green>Islands<gray>."
            )),
    GARDEN("Garden", "<aqua>", Material.PLAYER_HEAD, "f4880d2c1e7b86e87522e20882656f45bafd42f94932b2c5e0d6ecaa490cb4c",
            List.of(
                    "<gray>View all of the <gold>Pests <gray>that you've",
                    "<gray>killed on the <aqua>Garden<gray>."
            )),
    SPIDERS_DEN("Spider's Den", "<red>", Material.PLAYER_HEAD, "c754318a3376f470e481dfcd6c83a59aa690ad4b4dd7577fdad1c2ef08d8aee6",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in the <red>Spider's Den<gray>."
            )),
    THE_END("The End", "<light_purple>", Material.PLAYER_HEAD, "7840b87d52271d2a755dedc82877e0ed3df67dcc42ea479ec146176b02779a5",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in <light_purple>The End<gray>."
            )),
    CRIMSON_ISLE("Crimson Isle", "<red>", Material.PLAYER_HEAD, "c3687e25c632bce8aa61e0d64c24e694c3eea629ea944f4cf30dcfb4fbce071",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in the <red>Crimson Isle<gray>."
            )),
    DEEP_CAVERNS("Deep Caverns", "<aqua>", Material.PLAYER_HEAD, "569a1f114151b4521373f34bc14c2963a5011cdc25a6554c48c708cd96ebfc",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in the <aqua>Deep Caverns<gray>."
            )),
    DWARVEN_MINES("Dwarven Mines", "<dark_green>", Material.PLAYER_HEAD, "6b20b23c1aa2be0270f016b4c90d6ee6b8330a17cfef87869d6ad60b2ffbf3b5",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in the <dark_green>Dwarven Mines<gray>."
            )),
    CRYSTAL_HALLOWS("Crystal Hallows", "<dark_purple>", Material.PLAYER_HEAD, "21dbe30b027acbceb612563bd877cd7ebb719ea6ed1399027dcee58bb9049d4a",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in the <dark_purple>Crystal",
                    "<dark_purple>Hallows<gray>."
            )),
    THE_PARK("The Park", "<dark_aqua>", Material.PLAYER_HEAD, "a221f813dacee0fef8c59f76894dbb26415478d9ddfc44c2e708a6d3b7549b",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in <dark_aqua>The Park<gray>."
            )),
    GALATEA("Galatea", "<dark_green>", Material.PLAYER_HEAD, "9336d7cc95cbf6689f5e8c954294ec8d1efc494a4031325bb427bc81d56a484d",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in <dark_green>Galatea<gray>."
            )),
    SPOOKY_FESTIVAL("Spooky Festival", "<gold>", Material.JACK_O_LANTERN, null,
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed during the <gold>Spooky",
                    "<gold>Festival<gray>."
            )),
    CATACOMBS("Catacombs", "<red>", Material.PLAYER_HEAD, "964e1c3e315c8d8fffc37985b6681c5bd16a6f97ffd07199e8a05efbef103793",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed in <red>The Catacombs<gray>."
            )),
    FISHING("Fishing", "<dark_aqua>", Material.FISHING_ROD, null,
            List.of(
                    "<gray>View all of the <dark_aqua>Sea Creatures <gray>that",
                    "<gray>you've killed while fishing."
            )),
    MYTHOLOGICAL_CREATURES("Mythological Creatures", "<aqua>", Material.PLAYER_HEAD, "83cc1cf672a4b2540be346ead79ac2d9ed19d95b6075bf95be0b6d0da61377be",
            List.of(
                    "<gray>View all of the <aqua>Mythological",
                    "<aqua>Creatures <gray>that you've killed."
            )),
    JERRY("Jerry", "<gold>", Material.PLAYER_HEAD, "45f729736996a38e186fe9fe7f5a04b387ed03f3871ecc82fa78d8a2bdd31109",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed while fighting <gold>Jerry<gray>."
            )),
    KUUDRA("Kuudra", "<red>", Material.PLAYER_HEAD, "5051c83d9ebf69013f1ec8c9efc979ec2d925a921cc877ff64abe09aadd2f6cc",
            List.of(
                    "<gray>View all of the mobs that you've",
                    "<gray>found and killed while fighting <red>Kuudra<gray>."
            ));

    private final String displayName;
    private final String color;
    private final Material iconMaterial;
    private final String iconTexture;
    private final List<String> description;

    BestiarySection(String displayName, String color, Material iconMaterial, String iconTexture, List<String> description) {
        this.displayName = displayName;
        this.color = color;
        this.iconMaterial = iconMaterial;
        this.iconTexture = iconTexture;
        this.description = description;
    }

    public String displayName() {
        return displayName;
    }

    public String coloredName() {
        return color + displayName;
    }

    public Material iconMaterial() {
        return iconMaterial;
    }

    public String iconTexture() {
        return iconTexture;
    }

    public List<String> description() {
        return description;
    }

    public List<BestiaryFamily> families() {
        return Arrays.stream(BestiaryFamily.values())
                .filter(family -> family.section() == this)
                .toList();
    }

    public boolean openable() {
        return !families().isEmpty();
    }
}
