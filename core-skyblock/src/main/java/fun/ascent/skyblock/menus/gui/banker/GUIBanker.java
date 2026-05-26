package fun.ascent.skyblock.menus.gui.banker;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.gui.RefreshingGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.*;

public class GUIBanker extends InventoryGUI implements RefreshingGUI {

    public GUIBanker() {
        super("Banker", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getCloseItem(31));

        Player player = e.player();
        if (!(player instanceof SkyblockPlayer sp)) return;

        SkyblockProfile profile = sp.getActiveProfile();
        if (profile == null) return;

        if (profile.profilePlayers.size() > 1) {
            setTitle("Co-op Bank");
        } else {
            setTitle("Personal Bank");
        }

        refreshItems(player);
    }

    @Override
    public void refreshItems(Player p) {
        if (!(p instanceof SkyblockPlayer player)) return;
        SkyblockProfile profile = player.getActiveProfile();
        if (profile == null) return;

        double bankBalance = profile.bankCoins;
        double purseBalance = player.getCoins();

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                new GUIBankerDeposit().open(pl);
            }

            @Override
            public net.minestom.server.item.ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Store your coins safely in the bank"));
                lore.add(text("<gray>to earn interest and prevent losing"));
                lore.add(text("<gray>them on death."));
                lore.add(Component.empty());
                lore.add(text("<gray>Bank Balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(text("<gray>Purse: <gold>" + commaify(purseBalance) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to deposit coins!"));

                return ItemStackCreator.getStack("<green>Deposit", Material.CHEST, 1, lore);
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, Player pl) {
                new GUIBankerWithdraw().open(pl);
            }

            @Override
            public net.minestom.server.item.ItemStack.Builder getItem(Player pl) {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Withdraw your coins from the bank"));
                lore.add(text("<gray>to use them for buying items or"));
                lore.add(text("<gray>trading with other players."));
                lore.add(Component.empty());
                lore.add(text("<gray>Bank Balance: <gold>" + commaify(bankBalance) + " coins"));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to withdraw coins!"));

                return ItemStackCreator.getStack("<green>Withdraw", Material.DISPENSER, 1, lore);
            }
        });

        set(15, ItemStackCreator.getStack("<green>Recent Transactions", Material.FILLED_MAP, 1, buildTransactionLore(profile)), false);
        set(32, ItemStackCreator.getStack("<green>Bank Information", Material.REDSTONE_TORCH, 1, buildInfoLore(profile)), false);
    }

    private List<Component> buildTransactionLore(SkyblockProfile profile) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Recent activity on this bank account:"));
        lore.add(Component.empty());

        List<SkyblockProfile.BankTransaction> txs = profile.bankTransactions;
        if (txs == null || txs.isEmpty()) {
            lore.add(text("<red>No recent transactions."));
        } else {
            for (int i = txs.size() - 1; i >= 0; i--) {
                SkyblockProfile.BankTransaction tx = txs.get(i);
                boolean isDeposit = tx.amount() >= 0;
                String prefix = isDeposit ? "<green>+" : "<red>-";
                String formattedAmount = commaify(Math.abs(tx.amount()));
                String relativeTime = formatTimeAsAgo(tx.timestamp());

                lore.add(text(prefix + formattedAmount + " <gray>by <yellow>" + tx.originator() + " <dark_gray>(" + relativeTime + ")"));
            }
        }
        return lore;
    }

    private List<Component> buildInfoLore(SkyblockProfile profile) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Information about your bank account:"));
        lore.add(Component.empty());
        lore.add(text("<gray>Current Account Tier: <green>Starter"));
        lore.add(text("<gray>Max Capacity Limit: <gold>" + commaify(profile.bankLimit) + " coins"));
        lore.add(text("<gray>Interest Rate: <gold>2% <gray>(up to limit)"));
        lore.add(Component.empty());

        long currentMonth = (System.currentTimeMillis() - 1560275700000L) / (1200L * 31 * 1000);
        long monthStartMs = 1560275700000L + (currentMonth + 1) * (1200L * 31 * 1000);
        long timeUntilMs = monthStartMs - System.currentTimeMillis();
        String timeLeft = formatTimeLeft(timeUntilMs);

        lore.add(text("<gray>Next Interest Payout in: <yellow>" + timeLeft));
        return lore;
    }

    @Override
    public int refreshRate() {
        return 20;
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
