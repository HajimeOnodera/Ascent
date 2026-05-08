package fun.ascent.skyblock.hotm;

import fun.ascent.skyblock.hotm.upgrade.*;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.player.ResolvableProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fun.ascent.common.StringUtility.text;
import static fun.ascent.skyblock.player.stats.Stats.*;
import static net.minestom.server.component.DataComponents.*;

public class HotmTree {

    private static final int[] XP_THRESHOLDS = {
            3_000, 9_000, 25_000, 60_000, 100_000, 150_000, 210_000, 290_000, 400_000
    };

    private static final String HOTM_SKULL_TEXTURE =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQu" +
                    "bmV0L3RleHR1cmUvODZmMDZlYWEzMDA0YWVlZDA5YjNkNWI0NWQ5NzZkZTU4NGU2OTFjMGU5" +
                    "Y2FkZTEzMzYzNWRlOTNkMjNiOWVkYiJ9fX0=";

    public static final List<HotmUpgrade> ALL = List.of(
            new GemstoneInfusion(), new GiftsFromTheDeparted(), new FrozenSolid(),
            new DeadMansChest(), new Excavator(), new RagsToRiches(), new HazardousMiner(),
            new Surveyor(), new SubzeroMining(), new EagerAdventurer(),
            new KeenEye(), new WarmHearted(), new DustCollector(), new DailyGrind(),
            new StrongArm(), new NoStoneUnturned(), new MineshaftMayhem(),
            new MiningSpeed2(), new PowderBuff(), new MiningFortune2(),
            new VeinSeekder(), new LonesomeMiner(), new Professional(), new Mole(),
            new Fortunate(), new GreatExplorer(), new ManiacMiner(),
            new GoblinKiller(), new PeakOfTheMountain(), new StarPowder(),
            new SkyMall(), new MiningMadness(), new SeasonedMineman(), new EfficientMiner(),
            new Orbiter(), new FrontLoaded(), new PrecisionMining(),
            new LuckOfTheCave(), new DailyPowder(), new Crystallized(),
            new MiningSpeedBoost(), new TitaniumInsanium(), new MiningFortune(),
            new QuickForge(), new Pickobulus(),
            new MiningSpeed()
    );

    private static final int[] TIER_START = {0, 7, 10, 17, 20, 27, 30, 37, 40, 45};
    private static final int[][] TIER_COLS = {
            {1, 2, 3, 4, 5, 6, 7},
            {2, 4, 6},
            {1, 2, 3, 4, 5, 6, 7},
            {2, 4, 6},
            {1, 2, 3, 4, 5, 6, 7},
            {2, 4, 6},
            {1, 2, 3, 4, 5, 6, 7},
            {2, 4, 6},
            {2, 3, 4, 5, 6},
            {4}
    };

    private final SkyblockPlayer player;
    private final HotmData data;

    private HotmTree(SkyblockPlayer player, HotmData data) {
        this.player = player;
        this.data = data;
    }

    public static HotmTree of(SkyblockPlayer player) {
        return new HotmTree(player, player.getActiveProfileData().hotmData);
    }

    public int getLevel(HotmUpgrade u) {
        return data.upgradeLevels.getOrDefault(u.id(), 0);
    }

    public boolean isEnabled(HotmUpgrade u) {
        return data.upgradeEnabled.getOrDefault(u.id(), true);
    }

    public int getPowder(Powder p) {
        return data.powder.getOrDefault(p.id(), 0);
    }

    public void addPowder(Powder p, int amount) {
        data.powder.merge(p.id(), amount, Integer::sum);
    }

    public String getActiveAbilityId() {
        return data.activeAbilityId;
    }

    @SuppressWarnings("unchecked")
    public <T extends HotmUpgrade> T get(Class<T> type) {
        return (T) ALL.stream()
                .filter(u -> u.getClass() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown upgrade: " + type.getSimpleName()));
    }

    public boolean canUnlock(HotmUpgrade u) {
        if (data.level < u.tierRequirement()) return false;
        List<Class<? extends HotmUpgrade>> prereqs = u.prerequisites();
        return prereqs.isEmpty() || prereqs.stream().anyMatch(p -> getLevel(get(p)) > 0);
    }

    public void addXp(int xp) {
        data.xp += xp;
        while (data.level < 10 && data.xp >= XP_THRESHOLDS[data.level]) {
            data.xp -= XP_THRESHOLDS[data.level];
            data.level++;
            data.tokens += data.level == 1 ? 1 : 2;
            if (data.level == 7) data.tokens++;
        }
    }

    private void unlock(HotmUpgrade u) {
        if (!canUnlock(u)) {
            player.sendMessage(text(c("<red>You need HOTM Level " + u.tierRequirement() + " to unlock this!")));
            return;
        }
        if (data.tokens < 1) {
            player.sendMessage(text(c("<red>You don't have any Tokens of the Mountain!")));
            return;
        }
        data.tokens--;
        data.upgradeLevels.put(u.id(), 1);
        if (u instanceof PickaxeAbility && data.activeAbilityId == null) {
            data.activeAbilityId = u.id();
        }
        player.sendMessage(text(c("<green>Unlocked <bold>" + u.name() + "<green>!")));
    }

    private void upgrade(HotmUpgrade u, int levels) {
        if (getLevel(u) == 0) { unlock(u); return; }

        int current = getLevel(u);
        int remaining = u.maxLevel() - current;
        if (remaining <= 0) { player.sendMessage(text(c("<red>Already maxed!"))); return; }

        int toBuy = Math.min(levels, remaining);
        int bought = 0;
        for (int i = 0; i < toBuy; i++) {
            int lv = current + i;
            Powder p = u.powder(lv);
            int price = u.cost(lv);
            if (getPowder(p) < price) break;
            data.powder.merge(p.id(), -price, Integer::sum);
            bought++;
        }

        if (bought == 0) {
            player.sendMessage(text(c("<red>Not enough " + u.powder(current).displayName() + "<red>!")));
            return;
        }
        data.upgradeLevels.put(u.id(), current + bought);
        player.sendMessage(text(c("<green>Upgraded <bold>" + u.name() + " <green>to level <bold>" + (current + bought) + "<green>!")));
    }

    private void toggleEnabled(HotmUpgrade u) {
        if (getLevel(u) == 0) return;
        boolean now = !isEnabled(u);
        data.upgradeEnabled.put(u.id(), now);
        player.sendMessage(text(c(now ? "<green>Enabled <bold>" + u.name() : "<red>Disabled <bold>" + u.name())));
    }

    private void selectAbility(PickaxeAbility ability) {
        if (getLevel(ability) == 0) return;
        data.activeAbilityId = ability.id();
        player.sendMessage(text(c("<green>Selected <bold>" + ability.name() + " <green>as your active Pickaxe Ability!")));
    }

    public void openMenu(int page) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, c("<dark_purple>Heart of the Mountain"));
        Map<Integer, HotmUpgrade> slotMap = new HashMap<>();

        buildContent(inv, page, slotMap);

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();

            if (slot == 53) { player.closeInventory(); return; }
            if (slot == 48 && page > 0) { player.closeInventory(); openMenu(0); return; }
            if (slot == 50 && page < 1) { player.closeInventory(); openMenu(1); return; }

            HotmUpgrade upgrade = slotMap.get(slot);
            if (upgrade == null) return;

            Click click = event.getClick();
            if (upgrade instanceof PickaxeAbility ability) {
                if (click instanceof Click.Left) {
                    if (getLevel(ability) == 0) unlock(ability);
                    else selectAbility(ability);
                }
            } else {
                switch (click) {
                    case Click.Left left -> upgrade(upgrade, 1);
                    case Click.LeftShift leftShift -> upgrade(upgrade, 10);
                    case Click.Right right -> toggleEnabled(upgrade);
                    default -> {
                    }
                }
            }

            slotMap.clear();
            buildContent(inv, page, slotMap);
        });

        player.openInventory(inv);
    }

    private void buildContent(Inventory inv, int page, Map<Integer, HotmUpgrade> slotMap) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) inv.setItemStack(i, filler);

        for (int row = 0; row < 5; row++) {
            int tierIdx = page == 0 ? row : row + 5;
            int tierNum = 10 - tierIdx;

            Material paneMat = data.level >= tierNum
                    ? Material.LIME_STAINED_GLASS_PANE
                    : Material.RED_STAINED_GLASS_PANE;
            inv.setItemStack(row * 9, ItemStack.builder(paneMat)
                    .customName(c("<gray>Tier " + tierNum)).build());

            int[] cols = TIER_COLS[tierIdx];
            for (int i = 0; i < cols.length; i++) {
                HotmUpgrade upgrade = ALL.get(TIER_START[tierIdx] + i);
                int slot = row * 9 + cols[i];
                inv.setItemStack(slot, upgrade.buildItem(this));
                slotMap.put(slot, upgrade);
            }
        }

        inv.setItemStack(45, buildInfoItem());
        inv.setItemStack(46, buildPowderItem());
        if (page > 0) inv.setItemStack(48, ItemStack.builder(Material.ARROW)
                .customName(c("<green>Tiers 6-10")).build());
        if (page < 1) inv.setItemStack(50, ItemStack.builder(Material.ARROW)
                .customName(c("<green>Tiers 1-5")).build());
        inv.setItemStack(53, ItemStack.builder(Material.BARRIER)
                .customName(c("<red>Close")).build());
    }

    private ItemStack buildInfoItem() {
        String xpLine = data.level < 10
                ? "<gray>XP: <green>" + fmt(data.xp) + "<dark_gray>/<green>" + fmt(XP_THRESHOLDS[data.level])
                : "<gray>XP: <green>MAX";

        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(PROFILE, new ResolvableProfile(new PlayerSkin(HOTM_SKULL_TEXTURE, "")))
                .customName(c("<dark_purple>Heart of the Mountain"))
                .lore(List.of(
                        c("<dark_gray>Level " + data.level + "<dark_gray>/<gray>10"),
                        c(xpLine),
                        Component.empty(),
                        c("<gray>Tokens of the Mountain: <dark_purple>" + data.tokens)
                ))
                .build();
    }

    private ItemStack buildPowderItem() {
        return ItemStack.builder(Material.EMERALD)
                .customName(c("<green>Powder"))
                .lore(List.of(
                        c("<dark_green>Mithril Powder: <green>" + fmt(getPowder(Powder.MITHRIL))),
                        c("<light_purple>Gemstone Powder: <light_purple>" + fmt(getPowder(Powder.GEMSTONE))),
                        c("<aqua>Glacial Powder: <aqua>" + fmt(getPowder(Powder.GLACIAL)))
                ))
                .build();
    }

    public void applyStats(ProfilePlayer profile) {
        int ms = getLevel(get(MiningSpeed.class)) * 20
                + getLevel(get(MiningSpeed2.class)) * 40
                + getLevel(get(EagerAdventurer.class)) * 2
                + getLevel(get(StrongArm.class)) * 5;

        int mf = getLevel(get(MiningFortune.class)) * 5
                + getLevel(get(MiningFortune2.class)) * 5
                + getLevel(get(SubzeroMining.class))
                + getLevel(get(RagsToRiches.class)) * 2;

        if (isEnabled(get(MiningMadness.class)) && getLevel(get(MiningMadness.class)) > 0) {
            ms += 50;
            mf += 50;
        }

        int mw = (int) (5 + getLevel(get(SeasonedMineman.class)) * 0.1);
        int cr = (int) (getLevel(get(WarmHearted.class)) * 0.2);

        if (ms > 0) profile.addToStat(MINING_SPEED, ms);
        if (mf > 0) profile.addToStat(MINING_FORTUNE, mf);
        if (mw > 0) profile.addToStat(MINING_WISDOM, mw);
        if (cr > 0) profile.addToStat(COLD_RESISTANCE, cr);
    }

    private static Component c(String s) {
        return text(s);
    }

    private static String fmt(int n) {
        return String.format("%,d", n);
    }
}
