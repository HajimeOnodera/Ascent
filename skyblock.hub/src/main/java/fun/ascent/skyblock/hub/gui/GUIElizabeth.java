package fun.ascent.skyblock.hub.gui;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.component.DataComponents;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class GUIElizabeth extends InventoryGUI {
    
    private Tab activeTab = Tab.CITY_PROJECTS;

    public enum Tab {
        CITY_PROJECTS,
        UPGRADES,
        BOOSTER_COOKIE,
        BITS_SHOP,
        FIRE_SALES
    }

    public GUIElizabeth() {
        super("Community Shop", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        render();
    }

    private void render() {
        // First fill everything with filler (this handles safety)
        fill(FILLER_ITEM);
        
        // Close button (Slot 49)
        set(GUIClickableItem.getCloseItem(49));

        // Row 1 Tabs (Slots 0 to 8)
        // Slot 2: City Projects
        set(new GUIClickableItem(2) {
            @Override
            public void run(InventoryPreClickEvent e, Player p) {
                activeTab = Tab.CITY_PROJECTS;
                render();
                updateItemStacks(getInventory(), p);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Help community members rebuild the Hub!"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to view City Projects!"));
                return ItemStackCreator.getStack("<aqua>City Projects", Material.GOLDEN_HORSE_ARMOR, 1, lore);
            }
        });

        // Slot 3: Account & Profile Upgrades
        set(new GUIClickableItem(3) {
            @Override
            public void run(InventoryPreClickEvent e, Player p) {
                activeTab = Tab.UPGRADES;
                render();
                updateItemStacks(getInventory(), p);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Purchase permanent upgrades for all your"));
                lore.add(text("<gray>profiles or your active profile!"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to view Upgrades!"));
                return ItemStackCreator.getStack("<green>Account & Profile Upgrades", Material.HOPPER, 1, lore);
            }
        });

        // Slot 4: Booster Cookie Shop
        set(new GUIClickableItem(4) {
            @Override
            public void run(InventoryPreClickEvent e, Player p) {
                activeTab = Tab.BOOSTER_COOKIE;
                render();
                updateItemStacks(getInventory(), p);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Buy Booster Cookies to get massive boosts"));
                lore.add(text("<gray>and start earning Bits!"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to view Booster Cookie Shop!"));
                return ItemStackCreator.getStack("<gold>Booster Cookie Shop", Material.COOKIE, 1, lore);
            }
        });

        // Slot 5: Bits Shop
        set(new GUIClickableItem(5) {
            @Override
            public void run(InventoryPreClickEvent e, Player p) {
                activeTab = Tab.BITS_SHOP;
                render();
                updateItemStacks(getInventory(), p);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Spend your Bits on exclusive items,"));
                lore.add(text("<gray>convenience upgrades, and cosmetics."));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to view Bits Shop!"));
                return ItemStackCreator.getStack("<aqua>Bits Shop", Material.DIAMOND, 1, lore);
            }
        });

        // Slot 6: Fire Sales
        set(new GUIClickableItem(6) {
            @Override
            public void run(InventoryPreClickEvent e, Player p) {
                activeTab = Tab.FIRE_SALES;
                render();
                updateItemStacks(getInventory(), p);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Purchase exclusive limited-edition"));
                lore.add(text("<gray>cosmetic skins."));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to view Fire Sales!"));
                return ItemStackCreator.getStack("<red>Fire Sales", Material.FIRE_CHARGE, 1, lore);
            }
        });

        // Slot 8: Close Button
        set(new GUIClickableItem(8) {
            @Override
            public void run(InventoryPreClickEvent e, Player p) {
                p.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack("<red>Close Menu", Material.EMERALD, 1, "<gray>Exit the Community Shop");
            }
        });

        // Row 2 Stained Glass indicators (Slots 9 to 17)
        int activeCol = switch (activeTab) {
            case CITY_PROJECTS -> 2;
            case UPGRADES -> 3;
            case BOOSTER_COOKIE -> 4;
            case BITS_SHOP -> 5;
            case FIRE_SALES -> 6;
        };

        for (int col = 0; col < 9; col++) {
            int slot = 9 + col;
            if (col == activeCol) {
                set(slot, ItemStack.builder(Material.LIME_STAINED_GLASS_PANE).set(DataComponents.CUSTOM_NAME, text("<green>Active Tab")), false);
            } else if (col >= 2 && col <= 6) {
                set(slot, ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE).set(DataComponents.CUSTOM_NAME, Component.space()), false);
            } else {
                set(slot, FILLER_ITEM);
            }
        }

        // Row 3 (Slots 18 to 26) are filled with FILLER_ITEM by default from fill()

        // Page-specific lower items (Row 4 & Row 5)
        if (activeTab == Tab.CITY_PROJECTS) {
            // Slot 31: Jukebox
            set(new GUIClickableItem(31) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Active Project: <aqua>Colosseum Rebuild <gray>is currently at 84.3%! Check back later."));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    List<Component> lore = new ArrayList<>();
                    lore.add(text("<gray>Active Project: <green>Colosseum Rebuild"));
                    lore.add(text("<gray>Progress: <yellow>84.3%"));
                    lore.add(Component.empty());
                    lore.add(text("<yellow>Click to view project details!"));
                    return ItemStackCreator.getStack("<aqua>City Projects", Material.JUKEBOX, 1, lore);
                }
            });

            // Slot 40: Emerald (Contributions)
            set(new GUIClickableItem(40) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Contribute materials or coins to earn Fame!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Contributions", Material.EMERALD, 1, "<gray>Contribute coins or items to active project");
                }
            });

            // Slot 41: Bookshelf (Project Benefits)
            set(new GUIClickableItem(41) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>You have unlocked all current project benefits!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<gold>Project Benefits", Material.BOOKSHELF, 1, "<gray>View permanent town upgrades");
                }
            });
        }
        else if (activeTab == Tab.UPGRADES) {
            // Account & Profile Upgrades Page
            // Slot 27: Grass block (Island Size)
            set(new GUIClickableItem(27) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Island Size Upgrades are currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Island Size Upgrade", Material.GRASS_BLOCK, 1, "<gray>Expand your Private Island buildable area");
                }
            });

            // Slot 28: Sugar Cane (Farming Fortune / Minions)
            set(new GUIClickableItem(28) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Farming Fortune Upgrades are currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Farming Fortune Upgrade", Material.SUGAR_CANE, 1, "<gray>Increase farming fortune permanently");
                }
            });

            // Slot 29: Co-op Slots (Builder skull)
            set(new GUIClickableItem(29) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Co-op Slots are currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Co-op Slots Upgrade", Material.PLAYER_HEAD, 1, "<gray>Add more players to your co-op profile");
                }
            });

            // Slot 30: Island Guest limit (Grass Block)
            set(new GUIClickableItem(30) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Guest Limits are currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Island Guest Limit", Material.GRASS_BLOCK, 1, "<gray>Increase the maximum guests on your island");
                }
            });

            // Slot 31: Magic Find (Beacon)
            set(new GUIClickableItem(31) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Magic Find Upgrades are currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Magic Find Upgrade", Material.BEACON, 1, "<gray>Gain permanent Magic Find stats");
                }
            });

            // Slot 32: Minion slots (Quartz Block)
            set(new GUIClickableItem(32) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Extra Minion Slots are currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Extra Minion Slots", Material.QUARTZ_BLOCK, 1, "<gray>Unlock extra slots for placing active minions");
                }
            });

            // Slot 36: Account Upgrades Info (Steve head)
            set(new GUIClickableItem(36) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {}

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<light_purple>Account Upgrades", Material.PLAYER_HEAD, 1, "<gray>Permanent upgrades applying across all profiles");
                }
            });

            // Slot 37: Ender Chest capacity (Ender Chest)
            set(new GUIClickableItem(37) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Ender Chest Capacity is currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<light_purple>Ender Chest Capacity", Material.ENDER_CHEST, 1, "<gray>Increase personal Ender Chest storage size");
                }
            });

            // Slot 38: Sack capacity (Sack/Bundle)
            set(new GUIClickableItem(38) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Sack Capacity is currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<light_purple>Sack Capacity", Material.BUNDLE, 1, "<gray>Increase maximum capacity of all Item Sacks");
                }
            });

            // Slot 39: Wardrobe Slots (Armor Stand)
            set(new GUIClickableItem(39) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Wardrobe Slots are currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<light_purple>Wardrobe Slots", Material.ARMOR_STAND, 1, "<gray>Add more armor preset slots to your Wardrobe");
                }
            });

            // Slot 40: Bazaar Tax Reduction (Gold Nugget/Stick)
            set(new GUIClickableItem(40) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Bazaar Tax Reduction is currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<light_purple>Bazaar Tax Reduction", Material.GOLD_NUGGET, 1, "<gray>Reduce Bazaar transaction taxation");
                }
            });

            // Slot 41: Bazaar Order Limit (Emerald)
            set(new GUIClickableItem(41) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<green>Bazaar Order Limit is currently maxed out!"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<light_purple>Bazaar Order Limit", Material.EMERALD, 1, "<gray>Increase active Bazaar buy/sell orders limit");
                }
            });
        }
        else if (activeTab == Tab.BOOSTER_COOKIE) {
            // Booster Cookie
            set(new GUIClickableItem(31) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    if (!(p instanceof SkyblockPlayer sPlayer) || sPlayer.getActiveProfileData() == null) return;
                    ProfilePlayer profile = sPlayer.getActiveProfileData();

                    if (!profile.claimedFreeCookie) {
                        profile.claimedFreeCookie = true;
                        SkyblockItem cookieItem = ItemRegistry.getItem("BOOSTER_COOKIE");
                        if (cookieItem != null) {
                            sPlayer.getInventory().addItemStack(cookieItem.buildItemStack());
                        }
                        sPlayer.sendMessage(text("<green>You successfully claimed your first FREE Booster Cookie!"));
                        sPlayer.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1f, 1f));
                        render();
                        updateItemStacks(getInventory(), p);
                    } else {
                        if (sPlayer.getCoins() >= 3000000) {
                            sPlayer.setCoins(sPlayer.getCoins() - 3000000);
                            SkyblockItem cookieItem = ItemRegistry.getItem("BOOSTER_COOKIE");
                            if (cookieItem != null) {
                                sPlayer.getInventory().addItemStack(cookieItem.buildItemStack());
                            }
                            sPlayer.sendMessage(text("<green>You purchased a Booster Cookie for 3,000,000 Coins!"));
                            sPlayer.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 1f));
                            render();
                            updateItemStacks(getInventory(), p);
                        } else {
                            sPlayer.sendMessage(text("<red>You do not have enough Coins to purchase a Booster Cookie!"));
                        }
                    }
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    List<Component> lore = new ArrayList<>();
                    lore.add(text("<gray>Acquire booster cookies from the"));
                    lore.add(text("<gray>community shop in the hub."));
                    lore.add(Component.empty());
                    lore.add(text("<light_purple>Cookie Buff:"));
                    lore.add(text("<dark_gray>› <gray>Ability to gain <aqua>Bits!"));
                    lore.add(text("<dark_gray>› <aqua>+25☴ <gray>on all <aqua>Wisdom <gray>stats"));
                    lore.add(text("<dark_gray>› <aqua>+15✯ Magic Find"));
                    lore.add(text("<dark_gray>› <gray>Keep <gold>coins <gray>on death"));
                    lore.add(text("<dark_gray>› <yellow>Permafly <gray>on private islands and gardens"));
                    lore.add(text("<dark_gray>› <gray>Quick access to some menus using their"));
                    lore.add(text("  <gray>respective commands:"));
                    lore.add(text("  <gold>/ah<gray>, <green>/bazaar<gray>, <green>/bank<gray>, <gold>/accessorybag<gray>,"));
                    lore.add(text("  <aqua>/fishingbag<gray>, <light_purple>/timepocket<gray>, <light_purple>/anvil<gray>, <light_purple>/hex<gray>,"));
                    lore.add(text("  <aqua>/etable<gray>, <light_purple>/potionbag<gray>, <light_purple>/rngmeter<gray>,"));
                    lore.add(text("  <aqua>/attributemenu <gray>and <yellow>/quiver"));
                    lore.add(text("<dark_gray>› <gray>Sell items directly to the trades and cookie menu"));
                    lore.add(text("<dark_gray>› <gray>AFK <green>immunity <gray>on your island and garden"));
                    lore.add(text("<dark_gray>› <gray>Toggle specific <light_purple>potion effects"));
                    lore.add(text("<dark_gray>› <gray>Link your items in chat using <yellow>/show"));
                    lore.add(text("<dark_gray>› <gray>Insta-sell your Material stash to the <gold>Bazaar"));
                    lore.add(text("<dark_gray>› <gray>Increases <gold>Chocolate Factory <gray>production by <gold>+0.25x"));
                    lore.add(text("<dark_gray>› <gray>Allows consuming <blue>Mixins <gray>directly from your inventory"));
                    lore.add(Component.empty());
                    lore.add(text("<gray>Duration: <green>4d"));
                    lore.add(text("<dark_gray>Farm bits from playing with the"));
                    lore.add(text("<dark_gray>Cookie Buff active and completing"));
                    lore.add(text("<dark_gray>specific tasks."));
                    lore.add(Component.empty());

                    if (p instanceof SkyblockPlayer sPlayer && sPlayer.getActiveProfileData() != null) {
                        if (!sPlayer.getActiveProfileData().claimedFreeCookie) {
                            lore.add(text("<gray>Price: <green>FREE"));
                            lore.add(Component.empty());
                            lore.add(text("<yellow>Click to claim your FREE Booster Cookie!"));
                        } else {
                            lore.add(text("<gray>Price: <gold>1,200 Gems <gray>or <yellow>3,000,000 Coins"));
                            lore.add(Component.empty());
                            if (sPlayer.getCoins() >= 3000000) {
                                lore.add(text("<yellow>Click to purchase Booster Cookie!"));
                            } else {
                                lore.add(text("<red>You cannot afford this!"));
                            }
                        }
                    } else {
                        lore.add(text("<gray>Price: <gold>1,200 Gems <gray>or <yellow>3,000,000 Coins"));
                    }

                    return ItemStackCreator.getStack("<gold>Booster Cookie", Material.COOKIE, 1, lore);
                }
            });
        }
        else if (activeTab == Tab.BITS_SHOP) {
            // Bits Shop items
            set(new GUIClickableItem(29) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<aqua>Purchased God Potion! (Simulated)"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<light_purple>God Potion", Material.POTION, 1, "<gray>Grants positive combat/utility buffs for 24h", "<gray>Cost: <aqua>1,500 Bits");
                }
            });

            set(new GUIClickableItem(30) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<aqua>Purchased Kat Flower! (Simulated)"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<green>Kat Flower", Material.RED_DYE, 1, "<gray>Reduces Pet Upgrade time by 24 hours", "<gray>Cost: <aqua>500 Bits");
                }
            });

            set(new GUIClickableItem(31) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {
                    p.sendMessage(StringUtility.text("<aqua>Purchased Autopet Rule! (Simulated)"));
                }

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<light_purple>Autopet Rule", Material.REDSTONE_TORCH, 1, "<gray>Allows automation of Pet swapping rules", "<gray>Cost: <aqua>21,000 Bits");
                }
            });
        }
        else if (activeTab == Tab.FIRE_SALES) {
            // Fire Sales
            set(new GUIClickableItem(31) {
                @Override
                public void run(InventoryPreClickEvent e, Player p) {}

                @Override
                public ItemStack.Builder getItem(Player p) {
                    return ItemStackCreator.getStack("<red>Fire Sales", Material.FIRE_CHARGE, 1, "<gray>No active or scheduled Fire Sales at this time");
                }
            });
        }
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
