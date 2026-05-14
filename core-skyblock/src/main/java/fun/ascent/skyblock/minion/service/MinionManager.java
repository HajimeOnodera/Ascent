package fun.ascent.skyblock.minion.service;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.minion.base.SkyblockMinion;
import fun.ascent.skyblock.minion.gui.MinionMenu;
import fun.ascent.skyblock.minion.model.MinionType;
import fun.ascent.skyblock.minion.visual.MinionItems;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fun.ascent.common.StringUtility.text;

public final class MinionManager {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Map<UUID, SkyblockMinion> MINIONS_BY_ID = new HashMap<>();
    private static final Map<UUID, List<SkyblockMinion>> MINIONS_BY_OWNER = new HashMap<>();

    private MinionManager() {
    }

    public static void init() {
        EventManager.registerEvent(new SEvent<PlayerEntityInteractEvent>() {
            @Override
            public void onEvent(PlayerEntityInteractEvent event) {
                if (!(event.getPlayer() instanceof SkyblockPlayer player)) {
                    return;
                }
                SkyblockMinion minion = getByEntityUuid(event.getTarget().getUuid());
                if (minion == null) {
                    return;
                }
                SkyblockProfile profile = player.getActiveProfile();
                if(profile == null) return;
                if (!minion.getOwnerUuid().equals(profile.profileID)) {
                    player.sendMessage(text(MINI_MESSAGE.deserialize("<red>This minion belongs to someone else.</red>")));
                    return;
                }
                MinionMenu.open(player, minion);
            }
        });
        EventManager.registerEvent(new SEvent<PlayerBlockPlaceEvent>() {
            @Override
            public void onEvent(PlayerBlockPlaceEvent event) {
                if (!(event.getPlayer() instanceof SkyblockPlayer player)) {
                    return;
                }
                if (event.getHand() != PlayerHand.MAIN) {
                    return;
                }
                ItemStack itemStack = player.getInventory().getItemStack(player.getHeldSlot());
                if (!MinionItems.isMinionItem(itemStack)) {
                    return;
                }
                MinionType type = MinionItems.getType(itemStack);
                if (type == null) {
                    return;
                }
                int tier = MinionItems.getTier(itemStack);
                event.setCancelled(true);
                placeMinionAt(player, type, tier, event.getBlockPosition().blockX(), event.getBlockPosition().blockY(), event.getBlockPosition().blockZ(), true);
            }
        });

        MinecraftServer.getSchedulerManager()
                .buildTask(MinionManager::tickAll)
                .repeat(TaskSchedule.tick(20))
                .schedule();
    }

    public static void registerMinion(SkyblockMinion minion) {
        MINIONS_BY_ID.put(minion.getId(), minion);
        MINIONS_BY_OWNER.computeIfAbsent(minion.getOwnerUuid(), ignored -> new ArrayList<>()).add(minion);
    }

    public static void placeMinion(SkyblockPlayer player, MinionType type) {
        placeMinion(player, type, 1);
    }

    public static void placeMinion(SkyblockPlayer player, MinionType type, int tier) {
        Pos base = commandBasePosition(player);
        placeMinionAt(player, type, tier, base.blockX(), base.blockY(), base.blockZ(), false);
    }

    private static void placeMinionAt(SkyblockPlayer player, MinionType type, int tier, int placeX, int placeY, int placeZ, boolean consumeItem) {
        SkyblockProfile profile = player.getActiveProfile();
        if(profile == null) return;
        List<SkyblockMinion> owned = MINIONS_BY_OWNER.computeIfAbsent(profile.profileID, ignored -> new ArrayList<>());

        if (owned.size() >= profile.minionSlots) {
            player.sendMessage(text(MINI_MESSAGE.deserialize("<red>You already have the maximum number of placed minions.</red>")));
            return;
        }

        Instance instance = player.getInstance();
        if (instance == null) {
            return;
        }

        Pos placeAt = new Pos(placeX + 0.5, placeY, placeZ + 0.5, snappedYaw(player), 0f);

        for (SkyblockMinion existing : MINIONS_BY_ID.values()) {
            if (existing.getInstance() == instance && existing.getPosition().distanceSquared(placeAt) < 1) {
                player.sendMessage(text(MINI_MESSAGE.deserialize("<red>This block is already occupied.</red>")));
                return;
            }
        }

        Block feetBlock = instance.getBlock(placeX, placeY, placeZ);
        Block headBlock = instance.getBlock(placeX, placeY + 1, placeZ);
        if (!feetBlock.isAir() || !headBlock.isAir()) {
            player.sendMessage(text(MINI_MESSAGE.deserialize("<red>You need more space to place this minion.</red>")));
            return;
        }

        if (consumeItem) {
            ItemStack heldItem = player.getInventory().getItemStack(player.getHeldSlot());
            if (!MinionItems.isMinionItem(heldItem)) {
                return;
            }
            int newAmount = heldItem.amount() - 1;
            player.getInventory().setItemStack(player.getHeldSlot(), newAmount <= 0 ? ItemStack.AIR : heldItem.withAmount(newAmount));
        }

        SkyblockMinion minion = MinionFactory.create(profile.profileID, type, tier, instance, placeAt);
        minion.spawn();

        MINIONS_BY_ID.put(minion.getId(), minion);
        owned.add(minion);

        player.sendMessage(text(MINI_MESSAGE.deserialize("<aqua>You placed a minion! <gray>(" + owned.size() + "/" + profile.minionSlots + ")</gray>")));
    }

    public static void removeMinion(SkyblockMinion minion) {
        if (minion == null) {
            return;
        }
        MINIONS_BY_ID.remove(minion.getId());
        List<SkyblockMinion> owned = MINIONS_BY_OWNER.get(minion.getOwnerUuid());
        if (owned != null) {
            owned.removeIf(existing -> existing.getId().equals(minion.getId()));
            if (owned.isEmpty()) {
                MINIONS_BY_OWNER.remove(minion.getOwnerUuid());
            }
        }
        minion.remove();
    }

    public static SkyblockMinion getByEntityUuid(UUID entityUuid) {
        if (entityUuid == null) {
            return null;
        }
        for (SkyblockMinion minion : MINIONS_BY_ID.values()) {
            if (minion.getEntity().getUuid().equals(entityUuid)) {
                return minion;
            }
        }
        return null;
    }

    public static SkyblockMinion getNearestOwnedMinion(SkyblockPlayer player, double maxDistance) {
        List<SkyblockMinion> owned = MINIONS_BY_OWNER.get(player.getActiveProfile().profileID);
        if (owned == null || owned.isEmpty()) {
            return null;
        }
        double maxDistanceSquared = maxDistance * maxDistance;
        return owned.stream()
                .filter(minion -> minion.getInstance() == player.getInstance())
                .filter(minion -> minion.getPosition().distanceSquared(player.getPosition()) <= maxDistanceSquared)
                .min(Comparator.comparingDouble(minion -> minion.getPosition().distanceSquared(player.getPosition())))
                .orElse(null);
    }

    public static Collection<SkyblockMinion> getOwnedMinions(SkyblockPlayer player) {
        SkyblockProfile profile = player.getActiveProfile();
        if(profile == null) return List.of();
        return getOwnedMinions(profile.profileID);
    }

    public static Collection<SkyblockMinion> getOwnedMinions(UUID profileId) {
        if(profileId == null) return List.of();
        return MINIONS_BY_OWNER.getOrDefault(profileId, List.of());
    }

    private static void tickAll() {
        for (SkyblockMinion minion : MINIONS_BY_ID.values()) {
            minion.tick();
        }
    }

    private static float snappedYaw(SkyblockPlayer player) {
        float yaw = player.getPosition().yaw() - 180;
        return Math.round(yaw / 90f) * 90f;
    }

    private static Pos commandBasePosition(SkyblockPlayer player) {
        Pos position = player.getPosition();
        float yaw = snappedYaw(player);
        int dx = 0;
        int dz = 0;
        if (yaw == 0f) {
            dz = 1;
        } else if (yaw == 90f || yaw == -270f) {
            dx = -1;
        } else if (yaw == 180f || yaw == -180f) {
            dz = -1;
        } else if (yaw == -90f || yaw == 270f) {
            dx = 1;
        }
        return new Pos(position.blockX() + dx, position.blockY() - 1, position.blockZ() + dz);
    }
}
