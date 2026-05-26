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
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.*;

public class GUIBankerWithdraw extends InventoryGUI {

    public GUIBankerWithdraw() {
        super("Banker ➜ Withdraw", InventoryType.CHEST_4_ROW);
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

        // 1. Withdraw Everything (Slot 10)
        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;
                sp.closeInventory();
                attemptWithdrawal(sp, bankBalance);
            }

            @Override
            public ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Withdraw all of the coins in your bank."));
                lore.add(Component.empty());
                lore.add(text("<gray>Current bank balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(text("<gray>Amount to withdraw: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to withdraw everything!"));

                return ItemStackCreator.getStack("<green>Everything", Material.DISPENSER, 64, lore);
            }
        });

        // 2. Withdraw Half Account (Slot 12)
        set(new GUIClickableItem(12) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;
                sp.closeInventory();
                attemptWithdrawal(sp, bankBalance / 2);
            }

            @Override
            public ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Withdraw half of the coins in your bank."));
                lore.add(Component.empty());
                lore.add(text("<gray>Current bank balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(text("<gray>Amount to withdraw: <gold>" + commaify(bankBalance / 2) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to withdraw half!"));

                return ItemStackCreator.getStack("<green>Half Account", Material.DISPENSER, 32, lore);
            }
        });

        // 3. Withdraw 20% (Slot 14)
        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;
                sp.closeInventory();
                attemptWithdrawal(sp, bankBalance / 5);
            }

            @Override
            public ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Withdraw 20% of the coins in your bank."));
                lore.add(Component.empty());
                lore.add(text("<gray>Current bank balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(text("<gray>Amount to withdraw: <gold>" + commaify(bankBalance / 5) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to withdraw 20%!"));

                return ItemStackCreator.getStack("<green>20% of Account", Material.DISPENSER, 12, lore);
            }
        });

        // 4. Withdraw Custom Amount (Slot 16)
        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                if (!(pl instanceof SkyblockPlayer sp)) return;
                sp.closeInventory();
                
                BankSignGUI.open(sp, new String[]{"Enter withdraw", "amount below:"}).thenAccept(query -> MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                    if (query == null || query.isBlank()) {
                        sp.sendMessage(text("<red>Custom withdrawal cancelled."));
                        new GUIBankerWithdraw().open(sp);
                        return;
                    }
                    try {
                        double amount = Double.parseDouble(query.trim());
                        attemptWithdrawal(sp, amount);
                    } catch (NumberFormatException ex) {
                        sp.sendMessage(text("<red>Invalid amount entered. Please enter a valid number."));
                        new GUIBankerWithdraw().open(sp);
                    }
                }));
            }

            @Override
            public ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Enter a custom amount to withdraw."));
                lore.add(Component.empty());
                lore.add(text("<gray>Current bank balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to open sign board!"));

                return ItemStackCreator.getStack("<green>Custom Amount", Material.OAK_SIGN, 1, lore);
            }
        });
    }

    private void attemptWithdrawal(SkyblockPlayer player, double amount) {
        if (amount <= 0) {
            player.sendMessage(text("<red>You must enter a positive amount of coins."));
            new GUIBankerWithdraw().open(player);
            return;
        }
        SkyblockProfile profile = player.getActiveProfile();
        if (profile == null) return;

        if (profile.bankCoins < amount) {
            player.sendMessage(text("<red>You do not have enough coins in your bank."));
            new GUIBankerWithdraw().open(player);
            return;
        }

        profile.bankCoins -= amount;
        player.addCoins(amount);

        profile.bankTransactions.add(new SkyblockProfile.BankTransaction(
                System.currentTimeMillis(),
                -amount,
                player.getUsername(),
                "WITHDRAW"
        ));

        if (profile.bankTransactions.size() > 10) {
            profile.bankTransactions = new ArrayList<>(profile.bankTransactions.subList(profile.bankTransactions.size() - 10, profile.bankTransactions.size()));
        }

        player.sendMessage(text("<green>Successfully withdrew <gold>" + commaify(amount) + " coins<green>! New bank balance: <gold>" + commaify(profile.bankCoins) + " coins."));
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
