package fun.ascent.skyblock.bazaar.ui.buySell;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.bazaar.BazaarEntry;
import fun.ascent.skyblock.bazaar.price.BZPriceRegistry;
import fun.ascent.skyblock.bazaar.ui.BazaarItemBuyMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.key.Key;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerEditSignEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fun.ascent.skyblock.bazaar.ui.BazaarChildCategoryMenu.fill;


public class BazaarInstantBuyMenu {

    public static final HashMap<UUID,Integer> amount = new HashMap<>();
    public static final HashMap<UUID,BazaarEntry> items = new HashMap<>();

    public static void open(SkyblockPlayer player, BazaarEntry item){
        items.put(player.getUuid(),item);
        String titleStart = item.title.split("➜")[1];

        Inventory inventory = new Inventory(InventoryType.CHEST_4_ROW,
                StringUtility.text("<dark_gray>" + titleStart + " ➜ Instant Buy"));

        fill(inventory);
        addButtons(inventory,item,player);
        player.openInventory(inventory);

        inventory.eventNode().addListener(InventoryPreClickEvent.class,event -> {
            event.setCancelled(true);
            SkyblockPlayer player1 =(SkyblockPlayer) event.getPlayer();

            if(event.getSlot() == 16){
                if(event.getClick() instanceof Click.Right || !amount.containsKey(player1.getUuid())){
                    openSignAmountThingy(player1, item);
                } else {
                    openConfirmInstantBuy(player1, item, amount.get(player1.getUuid()));
                }
                return;
            }
            if(event.getSlot() == 31){
                BazaarEntry bzItem = items.getOrDefault(player1.getUuid(),null);
                if(bzItem == null) return;
                BazaarItemBuyMenu.open(player1,bzItem);
                return;
            }
            if(event.getSlot() % 2 != 0 && event.getSlot() < 10 && event.getSlot() > 14) return;

            int amt = -1;
            if(event.getSlot() == 10){
                amt = 1;
            }
            if(event.getSlot() == 12){
                amt = item.itemToSell.buildItemStack().maxStackSize();
            }
            if(event.getSlot() == 14){
                int space = 0;
                for(ItemStack itemStack : player1.getInventory().getItemStacks()){
                    if(itemStack.isAir()){
                        space += item.itemToSell.buildItemStack().maxStackSize();
                    }
                }
                amt = space;
            }
            if(amt > 0) {
                if (canBuy(player1, item, amt)) {
                    double totalCost = getCost(item, amt);
                    player1.getActiveProfileData().playerCoins -= totalCost;
                    giveItems(player1, item, amt);
                    player1.sendMessage(StringUtility.text("<green>Bought " + amt + "x " + item.itemToSell.getDisplayName() + " for <gold>" + totalCost + " coins!"));
                    Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 1f);
                    player1.playSound(sound);
                } else {
                    player1.sendMessage(
                            StringUtility.text("<red>You cannot afford this item.")
                    );
                }
            }
        });
    }

    private static double getCost(BazaarEntry item, int amount) {
        return BZPriceRegistry.getBuy(item) * amount;
    }

    public static boolean canBuy(SkyblockPlayer player,BazaarEntry item,int amount){
        double price = BZPriceRegistry.getBuy(item) * amount;
        return player.getActiveProfileData().playerCoins > price;
    }

    public static boolean canBuy(SkyblockPlayer player,BazaarEntry item){
        return canBuy(player,item,1);
    }

    public static void giveItems(SkyblockPlayer player, BazaarEntry item, int amt) {
        ItemStack stack = item.itemToSell.buildItemStack();
        int remaining = amt;
        while (remaining > 0) {
            int toAdd = Math.min(stack.maxStackSize(), remaining);
            player.getInventory().addItemStack(stack.withAmount(toAdd));
            remaining -= toAdd;
        }
    }

    public static void openSignAmountThingy(SkyblockPlayer player, BazaarEntry item) {
        player.closeInventory();
        Point pos = player.getPosition();
        ListBinaryTag messages = ListBinaryTag.builder()
                .add(StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"))
                .add(StringBinaryTag.stringBinaryTag("{\"text\":\"^^^^^^^^^^^^^^^\"}"))
                .add(StringBinaryTag.stringBinaryTag("{\"text\":\"Enter amount\"}"))
                .add(StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"))
                .build();
        CompoundBinaryTag frontText = CompoundBinaryTag.builder()
                .put("messages", messages)
                .build();
        Block signBlock = Block.OAK_SIGN.withNbt(CompoundBinaryTag.builder()
                .put("front_text", frontText)
                .build());

        player.sendPacket(new BlockChangePacket(pos, signBlock));
        player.sendPacket(new OpenSignEditorPacket(pos, true));

        EventListener<PlayerEditSignEvent> listener = EventListener.builder(PlayerEditSignEvent.class)
                .handler(event -> {
                    String line = event.getLines().getFirst();
                    Block original = player.getInstance().getBlock(pos);
                    player.sendPacket(new BlockChangePacket(pos, original));
                    
                    if(line.isBlank()){
                        open(player, item);
                        return;
                    }
                    try {
                        int amt = Integer.parseInt(line.trim());
                        if (amt <= 0) {
                            player.sendMessage(StringUtility.text("<red>Please enter a valid positive integer!"));
                            open(player, item);
                            return;
                        }
                        amount.put(player.getUuid(), amt);
                        openConfirmInstantBuy(player, item, amt);
                    } catch (NumberFormatException e) {
                        player.sendMessage(StringUtility.text("<red>Please enter a valid number!"));
                        open(player, item);
                    }
                })
                .expireCount(1)
                .build();
        player.eventNode().addListener(listener);
    }

    public static void openConfirmInstantBuy(SkyblockPlayer player, BazaarEntry item, int amt) {
        Inventory inventory = new Inventory(InventoryType.CHEST_4_ROW, StringUtility.text("<dark_gray>Confirm Instant Buy"));
        fill(inventory);

        double basePrice = BZPriceRegistry.getBuy(item);
        double totalCost = basePrice * amt;

        List<Component> itemLore = List.of(
            StringUtility.text("<dark_gray>" + item.itemToSell.getDisplayName()),
            Component.empty(),
            StringUtility.text("<gray>Amount: <green>" + amt + "x"),
            Component.empty(),
            StringUtility.text("<gray>Per unit: <gold>" + basePrice + " coins"),
            StringUtility.text("<gray>Price: <gold>" + totalCost + " coins"),
            Component.empty(),
            StringUtility.text("<yellow>Click to buy now!")
        );
        ItemStack targetItem = item.itemToSell.buildItemStack()
                .withCustomName(StringUtility.text("<green>Custom Amount"))
                .withLore(itemLore);
        inventory.setItemStack(13, targetItem);

        ItemStack arrow = ItemStack.of(Material.ARROW)
                .withCustomName(StringUtility.text("<green>Go Back"))
                .withLore(StringUtility.text("<gray>To " + item.itemToSell.getDisplayName() + " ➜ Instant Buy"));
        inventory.setItemStack(31, arrow);

        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            SkyblockPlayer p = (SkyblockPlayer) event.getPlayer();
            int slot = event.getSlot();

            if (slot == 13) {
                if (canBuy(p, item, amt)) {
                    p.getActiveProfileData().playerCoins -= totalCost;
                    giveItems(p, item, amt);
                    p.sendMessage(StringUtility.text("<green>Bought " + amt + "x " + item.itemToSell.getDisplayName() + " for <gold>" + totalCost + " coins!"));
                    Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 1f);
                    p.playSound(sound);
                    p.closeInventory();
                } else {
                    p.sendMessage(StringUtility.text("<red>You cannot afford this item."));
                }
            } else if (slot == 31) {
                open(p, item);
            }
        });

        player.openInventory(inventory);
    }

    public static void addButtons(Inventory inventory, BazaarEntry item,SkyblockPlayer player){
        double basePrice = BZPriceRegistry.getBuy(item);
        ItemStack single = item.itemToSell.buildItemStack().
                withCustomName(StringUtility.text("<green>Buy only<yellow> one<green>!"))
                .withLore(
                        StringUtility.text("<dark_gray> " + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Amount: <green>1<gray>x"),
                        Component.empty(),
                        StringUtility.text("<gray>Price: <gold>" + basePrice + " coins"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to buy now!")
                );
        inventory.setItemStack(10,single);

        int maxStack = item.itemToSell.buildItemStack().maxStackSize();
        ItemStack stack = item.itemToSell.buildItemStack().
                withCustomName(StringUtility.text("<green>Buy a stack!"))
                .withAmount(maxStack)
                .withLore(
                        StringUtility.text("<dark_gray> " + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Amount: <green>"+ maxStack + "<gray>x"),
                        Component.empty(),
                        StringUtility.text("<gray>Per unit:<gold> " + basePrice + " coins"),
                        StringUtility.text("<gray>Price: <gold>" + (maxStack*basePrice) + " coins"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to buy now!")
                );
        inventory.setItemStack(12,stack);

        int space = 0;
        for(ItemStack itemStack : player.getInventory().getItemStacks()){
            if(itemStack.isAir()){
                space += item.itemToSell.buildItemStack().maxStackSize();
            }
        }
        ItemStack inv = ItemStack.of(Material.CHEST)
                .withCustomName(StringUtility.text("<green>Fill my inventory!"))
                .withLore(
                        StringUtility.text("<dark_gray> " + item.itemToSell.getDisplayName()),
                        Component.empty(),
                        StringUtility.text("<gray>Amount: <green>" + space + "<gray>x"),
                        Component.empty(),
                        StringUtility.text("<gray>Per unit:<gold> " + basePrice + " coins"),
                        StringUtility.text("<gray>Price: <gold>" + (space*basePrice) + " coins"),
                        Component.empty(),
                        StringUtility.text("<yellow>Click to buy now!")
                );
        inventory.setItemStack(14,inv);
        List<Component> lore = new ArrayList<>(List.of(
                StringUtility.text("<dark_gray>Buy Order Quantity"),
                Component.empty(),
                StringUtility.text("<gray>Buy up to <green>71,680<gray>x."),
                Component.empty(),
                StringUtility.text("<yellow>Click to specify!")
        ));
        if(amount.containsKey(player.getUuid())){
            int customAmt = amount.get(player.getUuid());
            lore = new ArrayList<>(List.of(
                StringUtility.text("<dark_gray>Buy Order Quantity"),
                Component.empty(),
                StringUtility.text("<gray>Your amount: <green>" + customAmt + "x"),
                Component.empty(),
                StringUtility.text("<aqua>Right-Click to edit!"),
                StringUtility.text("<yellow>Click to proceed!")
            ));
        }
        ItemStack custom = ItemStack.of(Material.OAK_SIGN)
                .withCustomName(StringUtility.text("<green>Custom Amount"))
                .withLore(
                        lore
                );
        inventory.setItemStack(16,custom);

        ItemStack back = ItemStack.of(Material.ARROW)
                .withCustomName(StringUtility.text("<green>Go Back"))
                .withLore(
                        StringUtility.text("<gray>To " + item.itemToSell.getDisplayName())
                );
        inventory.setItemStack(31,back);
    }

}
