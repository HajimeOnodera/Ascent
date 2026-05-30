package fun.ascent.skyblock.hub.gui;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class GUIBiblio extends InventoryGUI {
    public GUIBiblio() {
        super("SkyBlock Wiki", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getCloseItem(31));

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Visit the Wiki using <yellow>/wiki <gray>and browse"));
                lore.add(text("<gray>the many pages and utilities."));
                lore.add(Component.empty());
                lore.add(text("<gray>You can also specify and extra"));
                lore.add(text("<gray>argument when using <gold>/wiki <id> <gray>to"));
                lore.add(text("<gray>search via an item ID."));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to visit the Wiki!"));

                return ItemStackCreator.getStack("<light_purple>Wiki Command", Material.PAINTING, 1, lore);
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>The newly finished <green>Official SkyBlock"));
                lore.add(text("<green>Wiki <gray>has launched and contains lots"));
                lore.add(text("<gray>of useful information on items, mobs,"));
                lore.add(text("<gray>drop rates, areas, trivia, and more."));
                lore.add(Component.empty());
                lore.add(text("<gray>This is a <gold>Hypixel-led<gray>, <light_purple>community"));
                lore.add(text("<light_purple>maintained <gray>Wiki which aims to provide"));
                lore.add(text("<gray>the most accurate information in the"));
                lore.add(text("<gray>best way possible."));
                lore.add(Component.empty());
                lore.add(text("<yellow>See more @ wiki.hypixel.net"));

                return ItemStackCreator.getStack("<gold>The SkyBlock Wiki", Material.WRITABLE_BOOK, 1, lore);
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Want to view more information about"));
                lore.add(text("<gray>the item you are currently <light_purple>holding<gray>?"));
                lore.add(text("<gray>Then this is the command for <yellow>you<gray>!"));
                lore.add(Component.empty());
                lore.add(text("<gray>Running <gold>/wikithis <gray>whilst <green>holding an"));
                lore.add(text("<green>item <gray>will attempt to find a Wiki page"));
                lore.add(text("<gray>for the item and then link you to it"));
                lore.add(text("<gray>in-game."));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to search your held item!"));

                return ItemStackCreator.getStack("<green>Wikithis Command", Material.OAK_SIGN, 1, lore);
            }
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
