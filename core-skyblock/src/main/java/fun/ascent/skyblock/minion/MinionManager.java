package fun.ascent.skyblock.minion;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.minion.gui.MinionMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MinionManager {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final int MAX_MINIONS_PER_PLAYER = 5;
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
                if (!minion.getOwnerUuid().equals(player.getUuid())) {
                    player.sendMessage(MINI_MESSAGE.deserialize("<red>This minion belongs to someone else.</red>"));
                    return;
                }
                MinionMenu.open(player, minion);
            }
        });
        EventManager.registerEvent(new SEvent<PlayerUseItemOnBlockEvent>() {
            @Override
            public void onEvent(PlayerUseItemOnBlockEvent event) {
                if (!(event.getPlayer() instanceof SkyblockPlayer player)) {
                    return;
                }
                if (event.getHand() != PlayerHand.MAIN) {
                    return;
                }
                ItemStack itemStack = event.getItemStack();
                if (!MinionItems.isMinionItem(itemStack)) {
                    return;
                }
                MinionType type = MinionItems.getType(itemStack);
                if (type == null) {
                    return;
                }
                int tier = MinionItems.getTier(itemStack);
                placeMinion(player, type, tier, event.getPosition().blockX(), event.getPosition().blockY(), event.getPosition().blockZ(), event.getBlockFace(), true);
            }
        });

        MinecraftServer.getSchedulerManager()
                .buildTask(MinionManager::tickAll)
                .repeat(TaskSchedule.tick(20))
                .schedule();
    }

    public static SkyblockMinion placeMinion(SkyblockPlayer player, MinionType type) {
        return placeMinion(player, type, 1);
    }

    public static SkyblockMinion placeMinion(SkyblockPlayer player, MinionType type, int tier) {
        Pos base = commandBasePosition(player);
        return placeMinion(player, type, tier, base.blockX(), base.blockY(), base.blockZ(), BlockFace.TOP, false);
    }

    private static SkyblockMinion placeMinion(SkyblockPlayer player, MinionType type, int tier, int blockX, int blockY, int blockZ, BlockFace face, boolean consumeItem) {
        List<SkyblockMinion> owned = MINIONS_BY_OWNER.computeIfAbsent(player.getUuid(), ignored -> new ArrayList<>());
        if (owned.size() >= MAX_MINIONS_PER_PLAYER) {
            player.sendMessage(MINI_MESSAGE.deserialize("<red>You already have the maximum number of placed minions.</red>"));
            return null;
        }

        Instance instance = player.getInstance();
        if (instance == null) {
            return null;
        }

        Direction direction = face.toDirection();
        int placeX = blockX + direction.normalX();
        int placeY = blockY + direction.normalY();
        int placeZ = blockZ + direction.normalZ();
        int baseY = placeY - 1;
        Pos placeAt = new Pos(placeX + 0.5, placeY, placeZ + 0.5, snappedYaw(player), 0f);

        for (SkyblockMinion existing : MINIONS_BY_ID.values()) {
            if (existing.getInstance() == instance && existing.getPosition().distanceSquared(placeAt) < 1) {
                player.sendMessage(MINI_MESSAGE.deserialize("<red>This block is already occupied.</red>"));
                return null;
            }
        }

        Block feetBlock = instance.getBlock(placeX, placeY, placeZ);
        Block headBlock = instance.getBlock(placeX, placeY + 1, placeZ);
        if (!feetBlock.isAir() || !headBlock.isAir()) {
            player.sendMessage(MINI_MESSAGE.deserialize("<red>You need more space to place this minion.</red>"));
            return null;
        }

        if (consumeItem) {
            ItemStack heldItem = player.getInventory().getItemStack(player.getHeldSlot());
            if (!MinionItems.isMinionItem(heldItem)) {
                return null;
            }
            int newAmount = heldItem.amount() - 1;
            player.getInventory().setItemStack(player.getHeldSlot(), newAmount <= 0 ? ItemStack.AIR : heldItem.withAmount(newAmount));
        }

        instance.setBlock(placeX, baseY, placeZ, type.getBaseBlock());
        SkyblockMinion minion = new SkyblockMinion(player.getUuid(), type, tier, instance, placeAt);
        minion.spawn();

        MINIONS_BY_ID.put(minion.getId(), minion);
        owned.add(minion);

        player.sendMessage(MINI_MESSAGE.deserialize("<aqua>You placed a minion! <gray>(" + owned.size() + "/" + MAX_MINIONS_PER_PLAYER + ")</gray>"));
        MinionLayoutValidator.ValidationResult validation = MinionLayoutValidator.validate(minion);
        if (!validation.valid()) {
            player.sendMessage(MINI_MESSAGE.deserialize("<red>" + validation.message() + "</red>"));
        }
        return minion;
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
        List<SkyblockMinion> owned = MINIONS_BY_OWNER.get(player.getUuid());
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
        return MINIONS_BY_OWNER.getOrDefault(player.getUuid(), List.of());
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
