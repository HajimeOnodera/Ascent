package fun.ascent.skyblock.menus;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class BoosterCookieMenu extends InventoryGUI {

    public BoosterCookieMenu() {
        super("Booster Cookie", InventoryType.CHEST_6_ROW);
    }

    public static void open(SkyblockPlayer player) {
        new BoosterCookieMenu().open((Player) player);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);

        ProfilePlayer plProfile = ((SkyblockPlayer) e.player()).getActiveProfileData();
        if (plProfile == null) return;

        setBits(plProfile);
        setCookie(plProfile);
        setGems();
        setUtilities();
        setNavigation();
    }

    private void setBits(ProfilePlayer plProfile) {
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Bits are earned from booster").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>cookies and spent in the community").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>shop for unique items.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Bits Purse: <aqua>" + String.format("%,d", (int) plProfile.playerBits)).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Bits Available: <aqua>" + String.format("%,d", (int) plProfile.cookieBits)).decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>Eligible to farm while you have").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>the cookie buff.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<gray>Bits Multiplier: <aqua>" + String.format("%.3fx", plProfile.bitsMultiplier)).decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>Per <gold>Cookie<gray>: <aqua>+" + String.format("%,d bits", (int) (4800.0 * plProfile.bitsMultiplier)) + " <gray>available").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<dark_gray>Your Fame Rank's bit multiplier").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>applies on the bits from every").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>cookie you've ever eaten!").decoration(TextDecoration.ITALIC, false));

                ItemStack.Builder builder = ItemStackCreator.getStack(
                        miniMessage().deserialize("<aqua>Bits"),
                        Material.DIAMOND,
                        1,
                        lore
                );

                if (plProfile.cookieBits > 0) {
                    ItemStackCreator.enchant(builder);
                }

                return builder;
            }
        });
    }

    private void setCookie(ProfilePlayer plProfile) {
        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Cookie Buff for <aqua>4 days").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Gain <aqua>" + String.format("%,d Bits", (int) (4800.0 * plProfile.bitsMultiplier))).decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>Acquire Booster Cookies from the").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<aqua>Community Center <gray>in the Hub.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<light_purple>Cookie Buff:").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Ability to gain <aqua>Bits!").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <aqua>+25☴ <gray>on all <aqua>Wisdom <gray>stats").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <aqua>+15✯ <gray>Magic Find").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Keep <gold>coins <gray>on death").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <yellow>Permafly <gray>on private islands and gardens").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Quick access to some menus using their").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("  <gray>respective commands:").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("  <gold>/ah<gray>, <green>/bazaar<gray>, <green>/bank<gray>, <gold>/accessorybag<gray>,").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("  <aqua>/fishingbag<gray>, <light_purple>/timepocket<gray>, <light_purple>/anvil<gray>, <light_purple>/hex<gray>,").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("  <aqua>/etable<gray>, <light_purple>/potionbag<gray>, <light_purple>/rngmeter<gray>, <light_purple>/pity<gray>,").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("  <gray>and <yellow>/quiver").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Sell items directly to the trades and cookie menu").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>AFK <green>immunity <gray>on your island and garden").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Toggle specific <light_purple>potion effects").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Link your items in chat using <yellow>/show").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Insta-sell your Material stash to the <gold>Bazaar").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Increases <gold>Chocolate Factory <gray>production by <gold>+0.25x").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>▪ <gray>Allows consuming <blue>Mixins <gray>directly from your inventory").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());

                boolean active = plProfile.boosterCookieExpires > System.currentTimeMillis();
                if (active) {
                    long remainingMs = plProfile.boosterCookieExpires - System.currentTimeMillis();
                    long remainingSeconds = remainingMs / 1000;
                    long days = remainingSeconds / (24 * 3600);
                    long hours = (remainingSeconds % (24 * 3600)) / 3600;
                    long minutes = (remainingSeconds % 3600) / 60;
                    long seconds = remainingSeconds % 60;
                    
                    String timeStr;
                    if (days > 0) {
                        timeStr = days + "d " + hours + "h " + minutes + "m " + seconds + "s";
                    } else if (hours > 0) {
                        timeStr = hours + "h " + minutes + "m " + seconds + "s";
                    } else if (minutes > 0) {
                        timeStr = minutes + "m " + seconds + "s";
                    } else {
                        timeStr = seconds + "s";
                    }
                    lore.add(miniMessage().deserialize("<gray>Duration: <green>" + timeStr).decoration(TextDecoration.ITALIC, false));
                } else {
                    lore.add(miniMessage().deserialize("<gray>Duration: <red>Not Active").decoration(TextDecoration.ITALIC, false));
                }
                lore.add(miniMessage().deserialize("<dark_gray>Farm bits from playing with the").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>Cookie Buff active and completing").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>specific tasks.").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to get store info!"));
                lore.add(miniMessage().deserialize("<gold>Bazaar Buy Price: <aqua>12,799,993.7 Coins").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gold>Bazaar Sell Price: <aqua>12,642,875.4 Coins").decoration(TextDecoration.ITALIC, false));

                ItemStack.Builder builder = ItemStackCreator.getStack(
                        miniMessage().deserialize("<gold>Booster Cookie"),
                        Material.COOKIE,
                        1,
                        lore
                );

                if (active) {
                    ItemStackCreator.enchant(builder);
                }

                return builder;
            }
        });
    }

    private void setGems() {
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.sendMessage(text("<green>Gems are purchasable from our webstore at store.ascent.eu!"));
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Use SkyBlock Gems to purchase:").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>- <gold>Booster Cookies").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>- <red>Fire Sales").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>- <light_purple>Taylor's Cosmetics").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>- <green>SkyMart Barn & Greenhouse Skins").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>- <light_purple>Account & Profile Upgrades").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("You have: <green>0 Gems").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>Gems can be purchased from our").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<dark_gray>webstore at <aqua>store.ascent.eu").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage().deserialize("<yellow>Click to get link!"));

                return ItemStackCreator.getStackHead(
                        miniMessage().deserialize("<green>SkyBlock Gems"),
                        "34f19b26ad0b190f845d4ff025d57bcfd5d0458df21b0235f30327f3d9db2cd", // Golden hat head
                        1,
                        lore
                );
            }
        });
    }

    private void setUtilities() {
        set(new GUIClickableItem(28) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStackHead(
                        miniMessage().deserialize("<green>Potion Effects"),
                        "ce89d20c5717ef1b2394d6e9f2913e6396e838708709abed66c303f269a23c3b", // Purple potion chest head
                        1,
                        List.of(miniMessage().deserialize("<gray>Toggle active potion effects.").decoration(TextDecoration.ITALIC, false))
                );
            }
        });

        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.closeInventory();
                player.sendMessage(text("<yellow>Use /etable to access the Enchanting Table!"));
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Enchanting Table"),
                        Material.ENCHANTING_TABLE,
                        1,
                        List.of(miniMessage().deserialize("<gray>Access the personal enchanting table.").decoration(TextDecoration.ITALIC, false))
                );
            }
        });

        set(new GUIClickableItem(30) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.closeInventory();
                player.sendMessage(text("<yellow>Use /anvil to access the Anvil!"));
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Anvil"),
                        Material.ANVIL,
                        1,
                        List.of(miniMessage().deserialize("<gray>Access the personal anvil.").decoration(TextDecoration.ITALIC, false))
                );
            }
        });

        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Sacks"),
                        Material.SPLASH_POTION,
                        1,
                        List.of(miniMessage().deserialize("<gray>Manage and access your personal sacks.").decoration(TextDecoration.ITALIC, false))
                );
            }
        });

        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.closeInventory();
                player.sendMessage(text("<yellow>Use /bazaar to access the Bazaar!"));
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Bazaar"),
                        Material.GOLDEN_HORSE_ARMOR,
                        1,
                        List.of(miniMessage().deserialize("<gray>Access the global Bazaar trade network.").decoration(TextDecoration.ITALIC, false))
                );
            }
        });

        set(new GUIClickableItem(34) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStackHead(
                        miniMessage().deserialize("<green>Trades"),
                        "2f13f0b2f70b43e8d2e85a5a1f6a1e838e12bd810e7b897852fcdfd9cb57b", // Trades master head
                        1,
                        List.of(miniMessage().deserialize("<gray>Access fast trading features.").decoration(TextDecoration.ITALIC, false))
                );
            }
        });
    }

    private void setNavigation() {
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                SkyblockMenu.open((SkyblockPlayer) player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Back"),
                        Material.ARROW,
                        1,
                        List.of(miniMessage().deserialize("<gray>Return to SkyBlock Menu").decoration(TextDecoration.ITALIC, false))
                );
            }
        });

        set(new GUIClickableItem(49) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Sell Item"),
                        Material.HOPPER,
                        1,
                        List.of(
                                miniMessage().deserialize("<gray>Click items in your inventory to sell").decoration(TextDecoration.ITALIC, false),
                                miniMessage().deserialize("<gray>them to this Shop!").decoration(TextDecoration.ITALIC, false)
                        )
                );
            }
        });

        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Fame Rank"),
                        Material.EMERALD,
                        1,
                        List.of(
                                miniMessage().deserialize("<gray>View your current Fame Rank status").decoration(TextDecoration.ITALIC, false),
                                miniMessage().deserialize("<gray>and bit multiplier bonuses.").decoration(TextDecoration.ITALIC, false)
                        )
                );
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
