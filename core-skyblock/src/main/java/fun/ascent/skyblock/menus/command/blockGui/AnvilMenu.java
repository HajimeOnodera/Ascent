package fun.ascent.skyblock.menus.command.blockGui;

import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.reforge.RarityStat;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.item.reforge.ReforgeStoneRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class AnvilMenu {

    private static final int[] UPGRADE_DECO = {11, 12, 20};
    private static final int[] SACRIFICE_DECO = {14, 15, 24};
    private static final int OUTPUT_SLOT = 13;
    private static final int COMBINE_SLOT = 22;
    private static final int UPGRADE_INPUT = 29;
    private static final int SACRIFICE_INPUT = 33;
    private static final int CLOSE_SLOT = 49;
    private static final int[] BOTTOM_PANES = {45, 46, 47, 48, 50, 51, 52, 53};

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private static Component t(String text) {
        return MM.deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    private static final ItemStack UPGRADE_PANE_RED = ItemStack.builder(Material.RED_STAINED_GLASS_PANE)
            .customName(t("<gold>Item to Upgrade"))
            .lore(List.of(
                    t("<gray>The item you want to upgrade should"),
                    t("<gray>be placed in the slot on this side.")))
            .build();
    private static final ItemStack UPGRADE_PANE_GREEN = ItemStack.builder(Material.GREEN_STAINED_GLASS_PANE)
            .customName(t("<gold>Item to Upgrade"))
            .lore(List.of(
                    t("<gray>The item you want to upgrade should"),
                    t("<gray>be placed in the slot on this side.")))
            .build();

    private static final ItemStack SACRIFICE_PANE_RED = ItemStack.builder(Material.RED_STAINED_GLASS_PANE)
            .customName(t("<gold>Item to Sacrifice"))
            .lore(List.of(
                    t("<gray>The item you are sacrificing in order"),
                    t("<gray>to upgrade the item on the left"),
                    t("<gray>should be placed in the slot on this side.")))
            .build();
    private static final ItemStack SACRIFICE_PANE_GREEN = ItemStack.builder(Material.GREEN_STAINED_GLASS_PANE)
            .customName(t("<gold>Item to Sacrifice"))
            .lore(List.of(
                    t("<gray>The item you are sacrificing in order"),
                    t("<gray>to upgrade the item on the left"),
                    t("<gray>should be placed in the slot on this side.")))
            .build();

    private static final ItemStack BOTTOM_RED = ItemStack.builder(Material.RED_STAINED_GLASS_PANE)
            .customName(Component.empty()).build();
    private static final ItemStack BOTTOM_GREEN = ItemStack.builder(Material.GREEN_STAINED_GLASS_PANE)
            .customName(Component.empty()).build();

    private static final ItemStack DEFAULT_OUTPUT = ItemStack.builder(Material.BARRIER)
            .customName(t("<red>Anvil"))
            .lore(List.of(
                    t("<gray>Place the target item in the left slot"),
                    t("<gray>and a sacrifice item in the right slot"),
                    t("<gray>to combine them!")))
            .build();

    private static final ItemStack COMBINE_BUTTON = ItemStack.builder(Material.ANVIL)
            .customName(t("<green>Combine Items"))
            .lore(List.of(
                    t("<gray>Combine the items in the slots to the"),
                    t("<gray>left and right below.")))
            .build();

    private static final ItemStack CLOSE_BUTTON = ItemStack.builder(Material.BARRIER)
            .customName(t("<red>Close"))
            .build();

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, MM.deserialize("Anvil"));

        fill(inv);
        inv.setItemStack(UPGRADE_INPUT, ItemStack.AIR);
        inv.setItemStack(SACRIFICE_INPUT, ItemStack.AIR);
        setDecoPanes(inv, false, false, false);
        inv.setItemStack(OUTPUT_SLOT, DEFAULT_OUTPUT);
        inv.setItemStack(COMBINE_SLOT, COMBINE_BUTTON);
        inv.setItemStack(CLOSE_SLOT, CLOSE_BUTTON);

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            int slot = event.getSlot();

            if (slot == UPGRADE_INPUT || slot == SACRIFICE_INPUT) {
                MinecraftServer.getSchedulerManager().scheduleNextTick(() -> updateState(inv, player));
                return;
            }
            if (slot >= inv.getSize()) {
                if (event.getClick() instanceof Click.LeftShift || event.getClick() instanceof Click.RightShift) {
                    MinecraftServer.getSchedulerManager().scheduleNextTick(() -> updateState(inv, player));
                }
                return;
            }

            event.setCancelled(true);

            if (slot == OUTPUT_SLOT) {
                MinecraftServer.getSchedulerManager().scheduleNextTick(() -> handleOutputTake(inv, player));
            } else if (slot == COMBINE_SLOT) {
                if (!inv.getItemStack(UPGRADE_INPUT).isAir() && !inv.getItemStack(SACRIFICE_INPUT).isAir()) {
                    handleCombine(inv, player);
                } else {
                    ItemStack output = inv.getItemStack(OUTPUT_SLOT);
                    if (!output.equals(DEFAULT_OUTPUT) && !output.isAir()) {
                        player.getInventory().addItemStack(output);
                        inv.setItemStack(OUTPUT_SLOT, DEFAULT_OUTPUT);
                        setDecoPanes(inv, false, false, false);
                    }
                }
            } else if (slot == CLOSE_SLOT) {
                player.closeInventory();
            }
        });

        inv.eventNode().addListener(InventoryClickEvent.class, event -> {
            int slot = event.getSlot();
            if (slot == UPGRADE_INPUT || slot == SACRIFICE_INPUT) {
                updateState(inv, player);
                MinecraftServer.getSchedulerManager().scheduleNextTick(() -> updateState(inv, player));
            } else if (slot >= inv.getSize() && event.getClickType() == ClickType.SHIFT_CLICK) {
                MinecraftServer.getSchedulerManager().scheduleNextTick(() -> updateState(inv, player));
            }
        });

        inv.eventNode().addListener(InventoryCloseEvent.class, event -> {
            returnItem(inv, player, UPGRADE_INPUT);
            returnItem(inv, player, SACRIFICE_INPUT);
            ItemStack output = inv.getItemStack(OUTPUT_SLOT);
            if (!output.equals(DEFAULT_OUTPUT) && !output.isAir()) {
                player.getInventory().addItemStack(output);
            }
        });

        player.openInventory(inv);
    }


    private static void handleOutputTake(Inventory inv, SkyblockPlayer player) {
        ItemStack output = inv.getItemStack(OUTPUT_SLOT);
        if (output.equals(DEFAULT_OUTPUT) || output.isAir()) return;
        // Only allow taking after a combine — input slots must be empty
        if (!inv.getItemStack(UPGRADE_INPUT).isAir() || !inv.getItemStack(SACRIFICE_INPUT).isAir()) return;
        player.getInventory().setCursorItem(output);
        inv.setItemStack(OUTPUT_SLOT, DEFAULT_OUTPUT);
        setDecoPanes(inv, false, false, false);
    }

    private sealed interface AnvilAction permits AnvilAction.Recombobulate, AnvilAction.ApplyReforge, AnvilAction.ApplyHotPotato {
        record Recombobulate() implements AnvilAction {}
        record ApplyReforge(Reforge reforge) implements AnvilAction {}
        record ApplyHotPotato() implements AnvilAction {}
    }

    private static void handleCombine(Inventory inv, SkyblockPlayer player) {
        ItemStack upgrade  = inv.getItemStack(UPGRADE_INPUT);
        ItemStack sacrifice = inv.getItemStack(SACRIFICE_INPUT);

        String upgradeId = ItemNBT.getItemId(upgrade);
        if (upgradeId == null) return;
        if (ItemRegistry.getItem(upgradeId) == null) return;

        String sacrificeId = ItemNBT.getItemId(sacrifice);
        if (sacrificeId == null) return;

        AnvilAction action = resolveAction(upgrade, sacrifice);
        if (action == null) return;

        inv.setItemStack(OUTPUT_SLOT, buildResult(upgrade, action, player));
        inv.setItemStack(UPGRADE_INPUT, ItemStack.AIR);
        inv.setItemStack(SACRIFICE_INPUT, ItemStack.AIR);
        setDecoPanes(inv, false, false, false);
    }

    private static void updateState(Inventory inv, SkyblockPlayer player) {
        ItemStack upgrade = inv.getItemStack(UPGRADE_INPUT);
        ItemStack sacrifice = inv.getItemStack(SACRIFICE_INPUT);

        boolean hasUpgrade = !upgrade.isAir();
        boolean hasSacrifice = !sacrifice.isAir();

        if (hasUpgrade && hasSacrifice) {
            AnvilAction action = resolveAction(upgrade, sacrifice);
            boolean compatible = action != null;
            inv.setItemStack(OUTPUT_SLOT, compatible ? buildResult(upgrade, action, player, true) : DEFAULT_OUTPUT);
            setDecoPanes(inv, compatible, compatible, compatible);
        } else {
            inv.setItemStack(OUTPUT_SLOT, DEFAULT_OUTPUT);
            setDecoPanes(inv, hasUpgrade, hasSacrifice, false);
        }
    }

    private static AnvilAction resolveAction(ItemStack upgrade, ItemStack sacrifice) {
        String upgradeId = ItemNBT.getItemId(upgrade);
        if (upgradeId == null) return null;
        SkyblockItem base = ItemRegistry.getItem(upgradeId);
        if (base == null) return null;

        String sacrificeId = ItemNBT.getItemId(sacrifice);
        if (sacrificeId == null) return null;

        if ("RECOMBOBULATOR_3000".equals(sacrificeId)) {
            if (ItemNBT.isRecombobulated(upgrade)) return null;
            if (base.getRarity().getNextRarity() == null) return null;
            return new AnvilAction.Recombobulate();
        }

        if ("HOT_POTATO_BOOK".equals(sacrificeId) || "FUMING_POTATO_BOOK".equals(sacrificeId)) {
            ItemType type = base.getItemType();
            if (!type.isArmor() && !type.isWeapon() && type != ItemType.FISHING_ROD) return null;
            int count = ItemNBT.getHotPotatoCount(upgrade);
            if ("HOT_POTATO_BOOK".equals(sacrificeId) && count < 10) return new AnvilAction.ApplyHotPotato();
            if ("FUMING_POTATO_BOOK".equals(sacrificeId) && count >= 10 && count < 15) return new AnvilAction.ApplyHotPotato();
            return null;
        }

        if (!base.isReforgeable()) return null;
        Reforge reforge = ReforgeStoneRegistry.getReforgeForStone(sacrificeId);
        if (reforge == null || !reforge.canApplyTo(base.getItemType())) return null;
        return new AnvilAction.ApplyReforge(reforge);
    }

    private static ItemStack buildResult(ItemStack upgradeItem, AnvilAction action, SkyblockPlayer player) {
        return buildResult(upgradeItem, action, player, false);
    }

    private static ItemStack buildResult(ItemStack upgradeItem, AnvilAction action, SkyblockPlayer player, boolean preview) {
        SkyblockItem base = ItemRegistry.getItem(ItemNBT.getItemId(upgradeItem));
        boolean wasRecombobulated = ItemNBT.isRecombobulated(upgradeItem);
        int currentHpb = ItemNBT.getHotPotatoCount(upgradeItem);
        SkyblockItem.Builder builder = base.toBuilder();

        switch (action) {
            case AnvilAction.Recombobulate() -> {
                builder.recombobulated(true).hotPotatoCount(currentHpb);
                String existingModifier = ItemNBT.getModifier(upgradeItem);
                if (existingModifier != null) {
                    Reforge r = Reforge.getById(existingModifier.toUpperCase(), base.getItemType());
                    if (r != null) {
                        Rarity newRarity = base.getRarity().getNextRarity();
                        builder.modifier(r.getName()).reforgeLore(r.getLoreText());
                        for (Map.Entry<Stats, RarityStat> e : r.getStats().entrySet()) {
                            builder.reforgeStat(e.getKey(), e.getValue().fromRarity(newRarity));
                        }
                    }
                }
            }
            case AnvilAction.ApplyReforge(Reforge reforge) -> {
                Rarity effectiveRarity = wasRecombobulated && base.getRarity().getNextRarity() != null
                        ? base.getRarity().getNextRarity() : base.getRarity();
                builder.recombobulated(wasRecombobulated)
                        .hotPotatoCount(currentHpb)
                        .modifier(reforge.getName())
                        .reforgeLore(reforge.getLoreText());
                for (Map.Entry<Stats, RarityStat> e : reforge.getStats().entrySet()) {
                    builder.reforgeStat(e.getKey(), e.getValue().fromRarity(effectiveRarity));
                }
            }
            case AnvilAction.ApplyHotPotato() -> {
                builder.recombobulated(wasRecombobulated).hotPotatoCount(currentHpb + 1);
                String existingModifier = ItemNBT.getModifier(upgradeItem);
                if (existingModifier != null) {
                    Reforge r = Reforge.getById(existingModifier.toUpperCase(), base.getItemType());
                    if (r != null) {
                        Rarity effectiveRarity = wasRecombobulated && base.getRarity().getNextRarity() != null
                                ? base.getRarity().getNextRarity() : base.getRarity();
                        builder.modifier(r.getName()).reforgeLore(r.getLoreText());
                        for (Map.Entry<Stats, RarityStat> e : r.getStats().entrySet()) {
                            builder.reforgeStat(e.getKey(), e.getValue().fromRarity(effectiveRarity));
                        }
                    }
                }
            }
        }

        return builder.build().buildItemStack(player, preview);
    }

    private static void setDecoPanes(Inventory inv, boolean upgradeGreen, boolean sacrificeGreen, boolean bottomGreen) {
        for (int slot : UPGRADE_DECO) inv.setItemStack(slot, upgradeGreen ? UPGRADE_PANE_GREEN : UPGRADE_PANE_RED);
        for (int slot : SACRIFICE_DECO) inv.setItemStack(slot, sacrificeGreen ? SACRIFICE_PANE_GREEN : SACRIFICE_PANE_RED);
        for (int slot : BOTTOM_PANES) inv.setItemStack(slot, bottomGreen ? BOTTOM_GREEN : BOTTOM_RED);
    }

    private static void returnItem(Inventory inv, SkyblockPlayer player, int slot) {
        ItemStack item = inv.getItemStack(slot);
        if (!item.isAir()) {
            player.getInventory().addItemStack(item);
            inv.setItemStack(slot, ItemStack.AIR);
        }
    }

    private static void fill(Inventory inventory) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty()).build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItemStack(i, filler);
        }
    }
}
