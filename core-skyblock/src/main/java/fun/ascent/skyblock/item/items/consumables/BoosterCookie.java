package fun.ascent.skyblock.item.items.consumables;

import fun.ascent.skyblock.item.ItemAbility;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class BoosterCookie implements ItemDefinition {

    private static final ItemAbility ABILITY = new ItemAbility(
            "Booster Cookie Buff", ItemAbility.AbilityType.RIGHT_CLICK,
            List.of(), 0, 0, 0, 0
    );

    @Override
    public String getItemId() {
        return "BOOSTER_COOKIE";
    }

    @Override
    public ItemAbility ability() {
        return ABILITY;
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .displayName("Booster Cookie")
                .description(
                    "Acquire booster cookies from the",
                    "community shop in the hub.",
                    " ",
                    "<light_purple>Cookie Buff:",
                    "<dark_gray>› <gray>Ability to gain <aqua>Bits!",
                    "<dark_gray>› <aqua>+25☴ <gray>on all <aqua>Wisdom <gray>stats",
                    "<dark_gray>› <aqua>+15✯ Magic Find",
                    "<dark_gray>› <gray>Keep <gold>coins <gray>on death",
                    "<dark_gray>› <yellow>Permafly <gray>on private islands and gardens",
                    "<dark_gray>› <gray>Quick access to some menus using their",
                    "  <gray>respective commands:",
                    "  <gold>/ah<gray>, <green>/bazaar<gray>, <green>/bank<gray>, <gold>/accessorybag<gray>,",
                    "  <aqua>/fishingbag<gray>, <light_purple>/timepocket<gray>, <light_purple>/anvil<gray>, <light_purple>/hex<gray>,",
                    "  <aqua>/etable<gray>, <light_purple>/potionbag<gray>, <light_purple>/rngmeter<gray>,",
                    "  <aqua>/attributemenu <gray>and <yellow>/quiver",
                    "<dark_gray>› <gray>Sell items directly to the trades and cookie menu",
                    "<dark_gray>› <gray>AFK <green>immunity <gray>on your island and garden",
                    "<dark_gray>› <gray>Toggle specific <light_purple>potion effects",
                    "<dark_gray>› <gray>Link your items in chat using <yellow>/show",
                    "<dark_gray>› <gray>Insta-sell your Material stash to the <gold>Bazaar",
                    "<dark_gray>› <gray>Increases <gold>Chocolate Factory <gray>production by <gold>+0.25x",
                    "<dark_gray>› <gray>Allows consuming <blue>Mixins <gray>directly from your inventory",
                    " ",
                    "Duration: <green>4d",
                    "<dark_gray>Farm bits from playing with the",
                    "<dark_gray>Cookie Buff active and completing",
                    "<dark_gray>specific tasks."
                )
                .unstackable(true);
    }

    @Override
    public void onRightClick(SkyblockPlayer player) {
        if (player.getActiveProfileData() == null) return;
        openConfirmGUI(player);
    }

    public void openConfirmGUI(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_3_ROW, "Consume Booster Cookie?");

        ItemStack pane = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty())
                .build();
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItemStack(i, pane);
        }
        int bitsToGain = 4800;
        if (player.getActiveProfileData() != null) {
            bitsToGain = (int) (4800.0 * player.getActiveProfileData().bitsMultiplier);
        }
        String formattedBits = String.format("%,d", bitsToGain);

        List<Component> consumeLore = List.of(
                text("§7Gain the Cookie Buff!"),
                Component.empty(),
                text("§7Duration: §a4 days!"),
                Component.empty(),
                text("§7You will be able to gain"),
                text("§b" + formattedBits + " Bits §7from this"),
                text("§7cookie.")
        );
        ItemStack consumeItem = ItemStack.builder(Material.COOKIE)
                .customName(text("§aConsume Cookie"))
                .lore(consumeLore)
                .build();
        inv.setItemStack(11, consumeItem);

        // Slot 15: Cancel
        List<Component> cancelLore = List.of(
                text("§7I'm not hungry...")
        );
        ItemStack cancelItem = ItemStack.builder(Material.RED_STAINED_GLASS)
                .customName(text("§cCancel"))
                .lore(cancelLore)
                .build();
        inv.setItemStack(15, cancelItem);

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();

            if (slot == 11) {
                player.closeInventory();
                
                // Perform consumption check and effect
                if (player.getActiveProfileData() == null) return;
                
                ItemStack mainHand = player.getItemInMainHand();
                if (mainHand.isAir() || !mainHand.material().equals(Material.COOKIE)) {
                    player.sendMessage(text("<red>You must be holding the Booster Cookie in your main hand to consume it!"));
                    return;
                }

                // Consume 1 cookie from player's hand
                player.setItemInHand(PlayerHand.MAIN, mainHand.withAmount(amount -> amount - 1));

                long duration = 4L * 24 * 60 * 60 * 1000; // 4 days in ms
                long current = System.currentTimeMillis();
                
                long newExpiry;
                if (player.getActiveProfileData().boosterCookieExpires <= current) {
                    newExpiry = current + duration;
                } else {
                    newExpiry = player.getActiveProfileData().boosterCookieExpires + duration;
                }
                
                player.getActiveProfileData().boosterCookieExpires = newExpiry;

                double bitsToAdd = 4000.0 * player.getActiveProfileData().bitsMultiplier;
                player.getActiveProfileData().playerBits += bitsToAdd;

                String[] suffixes = {
                    "",
                    "Sweet!",
                    "Delicious!",
                    "Savory!",
                    "Yummy!",
                    "Tasty!",
                    "Delectable!",
                    "Scrumptious!",
                    "Delightful!",
                    "Divine!"
                };
                int rand = new java.util.Random().nextInt(suffixes.length);
                String suffix = suffixes[rand];
                String message = "<yellow>You consumed a <gold>Booster Cookie<yellow>!";
                if (!suffix.isEmpty()) {
                    message += " <light_purple>" + suffix;
                }
                player.sendMessage(text(message + " <green>Your Booster Buff has been extended. <aqua>+" + (int) bitsToAdd + " Bits <green>added!"));
                player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1f, 1f));
                
            } else if (slot == 15) {
                player.closeInventory();
            }
        });

        player.openInventory(inv);
    }
}
