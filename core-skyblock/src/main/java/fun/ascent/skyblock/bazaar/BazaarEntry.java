package fun.ascent.skyblock.bazaar;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BazaarEntry {

    public String id,icon,parent,itemName,title;
    public int slot,guiSize = -1;
    public List<BazaarEntry> children;

    public transient BazaarEntry parentEntry;
    public transient SkyblockItem iconItem;
    public transient Component titleComp, nameComp;
    public transient SkyblockItem itemToSell;

    public BazaarEntry(@Nullable BazaarEntry parent,SkyblockItem icon,String id,int slot,
                       Component title,Component iconName,BazaarEntry... children){
        this.parentEntry = parent;
        this.iconItem = icon;
        this.itemToSell = ItemRegistry.getItem(id);
        this.titleComp = title;
        this.nameComp = iconName;

        this.id = id;
        this.icon = iconItem.getItemId();
        this.parent = parentEntry == null ? null : parentEntry.id;
        this.itemName = MiniMessage.miniMessage().serialize(iconName);
        this.title = MiniMessage.miniMessage().serialize(title);
        this.slot = slot;
        this.children = children.length > 0 ? Arrays.asList(children) : null;
    }

    public void initializeTransientFields(@Nullable BazaarEntry parent) {
        this.parentEntry = parent;
        if (this.icon != null) {
            this.iconItem = ItemRegistry.getItem(this.icon.toLowerCase());
        }
        if (this.id != null) {
            this.itemToSell = ItemRegistry.getItem(this.id);
        }
        if (this.itemName != null) {
            this.nameComp = StringUtility.text(this.itemName);
        }
        if (this.title != null) {
            this.titleComp = StringUtility.text(this.title);
        }
        if (this.children != null) {
            for (BazaarEntry child : this.children) {
                child.initializeTransientFields(this);
            }
        }
    }

    public InventoryType getSize(){
       return switch (this.guiSize){
           case 2 -> InventoryType.CHEST_2_ROW;
           case 3 -> InventoryType.CHEST_3_ROW;
           case 4 -> InventoryType.CHEST_4_ROW;
           case 5 -> InventoryType.CHEST_5_ROW;
           case 6 -> InventoryType.CHEST_6_ROW;
           default -> InventoryType.CHEST_1_ROW;
       };
    }

    public ItemStack getStack(boolean isActiveCategory) {
        if(this.parentEntry == null){
            ItemStack base = this.iconItem.buildItemStack();
            return base.withCustomName(this.nameComp)
                    .withLore(
                            StringUtility.text("<dark_gray>Category"),
                            Component.empty(),
                            (isActiveCategory) ?
                                    StringUtility.text("<green>Currently viewing!"):
                                    StringUtility.text("<yellow>Click to view!")
                    );
        }
        if(this.children == null){
            Price price = BZPriceRegistry.priceTable.get(this);
            if(price == null) return this.iconItem.buildItemStack();

            ItemStack base = this.iconItem.buildItemStack();
            return base.withCustomName(this.nameComp).withLore(
                    StringUtility.text(getRarity(this.itemToSell) + " commodity"),
                    Component.empty(),
                    StringUtility.text("<gray>Buy Price: <gold>" + price.buyPrice + " coins"),
                    StringUtility.text("<gray>Sell Price: <gold>" + price.sellPrice + " coins"),
                    Component.empty(),
                    StringUtility.text("<yellow>Click to view details")
            );
        }
        ItemStack base =  this.iconItem.buildItemStack();
        List<Component> lore = new ArrayList<>();
        lore.add(StringUtility.text("<dark_gray>" + this.children.size() + " products"));
        lore.add(Component.empty());
        this.children.forEach(child -> {
            TextColor color = child.nameComp.color();
            if (color == null && !child.nameComp.children().isEmpty()) {
                color = child.nameComp.children().getFirst().color();
            }
            if (color == null) color = net.kyori.adventure.text.format.NamedTextColor.WHITE;
            
            Component component = Component.text("▶").color(color);
            String msg = net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().serialize(component);
            String plainText = StringUtility.getTextFromComponent(child.nameComp);
            msg += " <gray>" + StringUtility.capitalize(plainText.toLowerCase());
            lore.add(StringUtility.text(msg));
        });
        lore.add(Component.empty());
        lore.add(StringUtility.text("<yellow>Click to view products"));
        return base.withCustomName(this.nameComp).withLore(lore);
    }

    public String getRarity(SkyblockItem item){
        if (item == null || item.getRarity() == null) return "<gray>Unknown";
        return "<dark_gray>"+ StringUtility.capitalize(item.getRarity().name());
    }
}
