package fun.ascent.skyblock.bazaar;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BazaarEntry {

    public String id,icon,parent,itemName,title;
    public int slot;
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
            this.nameComp = MiniMessage.miniMessage().deserialize(this.itemName);
        }
        if (this.title != null) {
            this.titleComp = MiniMessage.miniMessage().deserialize(this.title);
        }
        if (this.children != null) {
            for (BazaarEntry child : this.children) {
                child.initializeTransientFields(this);
            }
        }
    }

    public ItemStack getStack(boolean isActiveCategory) {
        if(this.parentEntry == null){
            ItemStack base = this.iconItem.buildItemStack();
            return base.withCustomName(this.nameComp)
                    .withLore(
                            MiniMessage.miniMessage().deserialize("<dark_gray>Category"),
                            Component.empty(),
                            (isActiveCategory) ?
                                    MiniMessage.miniMessage().deserialize("<green>Currently viewing!"):
                                    MiniMessage.miniMessage().deserialize("<yellow>Click to view!")
                    );
        }
        if(this.children == null){
            Price price = BZPriceRegistry.priceTable.get(this);
            if(price == null) return this.iconItem.buildItemStack();

            ItemStack base = this.iconItem.buildItemStack();
            return base.withCustomName(this.nameComp).withLore(
                    MiniMessage.miniMessage().deserialize(getRarity(this.nameComp) + " commodity"),
                    Component.empty(),
                    MiniMessage.miniMessage().deserialize("<gray>Buy Price: <gold>" + price.buyPrice + " coins"),
                    MiniMessage.miniMessage().deserialize("<gray>Sell Price: <gold>" + price.sellPrice + " coins"),
                    Component.empty(),
                    MiniMessage.miniMessage().deserialize("<yellow>Click to view details")
            );
        }
        ItemStack base =  this.iconItem.buildItemStack();
        List<Component> lore = new ArrayList<>();
        lore.add(MiniMessage.miniMessage().deserialize("<gray>" + this.children.size() + " products"));
        lore.add(Component.empty());
        this.children.forEach(child -> {
            TextColor color = child.nameComp.color();
            if(color == null) return;
            Component component = Component.text("▶").color(color);
            String msg = MiniMessage.miniMessage().serialize(component);
            msg += " <gray>" + StringUtility.capitalize(((TextComponent)child.nameComp).content().toLowerCase());
            lore.add(MiniMessage.miniMessage().deserialize(msg));
        });
        lore.add(Component.empty());
        lore.add(MiniMessage.miniMessage().deserialize("<yellow>Click to view products"));
        return base.withCustomName(this.nameComp).withLore(lore);
    }

    public String getRarity(Component comp){
        if(comp == null || comp.color() == null ) return "<gray> Unknown";
        return switch (comp.color().asHexString()){
            case "0xffffff" -> "<gray> Common";
            case "0x55ff55" -> "<gray> Uncommon";
            case "0x5555ff" -> "<gray> Rare";
            case "0xaa00aa" -> "<gray> Epic";
            case "0xffaa00" -> "<gray> Legendary";
            case "0xff55ff" -> "<gray> Mythic";
            case "0x55ffff" -> "<gray> Divine";
            default -> "<gray> Unknown";
        };
    }
}
