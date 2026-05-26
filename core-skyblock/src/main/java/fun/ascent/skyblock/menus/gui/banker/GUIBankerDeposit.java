package fun.ascent.skyblock.menus.gui.banker;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.*;

public class GUIBankerDeposit extends InventoryGUI {

    public GUIBankerDeposit() {
        super("Banker ➜ Deposit", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getGoBackItem(31, new GUIBanker()));

        Player p = e.player();
        if (!(p instanceof SkyblockPlayer player)) return;

        SkyblockProfile profile = player.getActiveProfile();
        if (profile == null) return;

        double bankBalance = profile.bankCoins;
        double purseBalance = player.getCoins();

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;
                double coins = sp.getCoins();
                sp.closeInventory();
                attemptDeposit(sp, coins);
            }

            @Override
            public net.minestom.server.item.ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Deposit all of the coins in your purse."));
                lore.add(Component.empty());
                lore.add(text("<gray>Current bank balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(text("<gray>Amount to deposit: <gold>" + commaify(purseBalance) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to deposit whole purse!"));

                return ItemStackCreator.getStack("<green>Entire Purse", Material.CHEST, 64, lore);
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;
                double coins = sp.getCoins() / 2;
                sp.closeInventory();
                attemptDeposit(sp, coins);
            }

            @Override
            public net.minestom.server.item.ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Deposit half of the coins in your purse."));
                lore.add(Component.empty());
                lore.add(text("<gray>Current bank balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(text("<gray>Amount to deposit: <gold>" + commaify(purseBalance / 2) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to deposit half purse!"));

                return ItemStackCreator.getStack("<green>Half Purse", Material.CHEST, 32, lore);
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;
                sp.closeInventory();
                
                BankSignGUI.open(sp, new String[]{"Enter deposit", "amount below:"}).thenAccept(query -> MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                    if (query == null || query.isBlank()) {
                        sp.sendMessage(text("<red>Custom deposit cancelled."));
                        new GUIBankerDeposit().open(sp);
                        return;
                    }
                    try {
                        double amount = Double.parseDouble(query.trim());
                        attemptDeposit(sp, amount);
                    } catch (NumberFormatException ex) {
                        sp.sendMessage(text("<red>Invalid amount entered. Please enter a valid number."));
                        new GUIBankerDeposit().open(sp);
                    }
                }));
            }

            @Override
            public net.minestom.server.item.ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Enter a custom amount to deposit."));
                lore.add(Component.empty());
                lore.add(text("<gray>Current bank balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(text("<gray>Purse coins: <gold>" + commaify(purseBalance) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to open sign board!"));

                return ItemStackCreator.getStack("<green>Custom Amount", Material.OAK_SIGN, 1, lore);
            }
        });
    }

    private void attemptDeposit(SkyblockPlayer player, double amount) {
        if (amount <= 0) {
            player.sendMessage(text("<red>You must enter a positive amount of coins."));
            new GUIBankerDeposit().open(player);
            return;
        }
        if (player.getCoins() < amount) {
            player.sendMessage(text("<red>You do not have enough coins in your purse."));
            new GUIBankerDeposit().open(player);
            return;
        }
        SkyblockProfile profile = player.getActiveProfile();
        if (profile == null) return;

        if (profile.bankCoins + amount > profile.bankLimit) {
            player.sendMessage(text("<red>This deposit would exceed your bank capacity limit of " + commaify(profile.bankLimit) + " coins!"));
            new GUIBankerDeposit().open(player);
            return;
        }

        player.removeCoins(amount);
        profile.bankCoins += amount;
        profile.bankTransactions.add(new SkyblockProfile.BankTransaction(
                System.currentTimeMillis(),
                amount,
                player.getUsername(),
                "DEPOSIT"
        ));

        if (profile.bankTransactions.size() > 10) {
            profile.bankTransactions = new ArrayList<>(profile.bankTransactions.subList(profile.bankTransactions.size() - 10, profile.bankTransactions.size()));
        }

        player.sendMessage(text("<green>Successfully deposited <gold>" + commaify(amount) + " coins<green>! New bank balance: <gold>" + commaify(profile.bankCoins) + " coins."));
        ProfileManager.saveProfile(profile.profileID);
        new GUIBanker().open(player);
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
