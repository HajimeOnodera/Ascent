package fun.ascent.skyblock.minion;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.SendablePacket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class SkyblockMinion {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final UUID id;
    private final UUID ownerUuid;
    private final Instance instance;
    private final Pos position;
    private final LivingEntity entity;
    private final Entity warningTop;
    private final Entity warningBottom;
    private final MinionStorage storage;
    private final List<LivingEntity> spawnedMobs;

    private final MinionType type;
    private final MinionProfile profile;
    private MinionData data;
    private long nextActionAt;
    private long totalGenerated;
    private boolean busy;
    private MinionTask currentTask;

    protected SkyblockMinion(UUID ownerUuid, MinionType type, int tier, Instance instance, Pos position) {
        this.id = UUID.randomUUID();
        this.ownerUuid = ownerUuid;
        this.type = type;
        this.profile = MinionProfiles.get(type);
        this.data = new MinionData(type, tier);
        this.instance = instance;
        this.position = position;
        this.entity = new LivingEntity(EntityType.ARMOR_STAND);
        this.warningTop = new Entity(EntityType.ARMOR_STAND);
        this.warningBottom = new Entity(EntityType.ARMOR_STAND);
        this.storage = new MinionStorage();
        this.spawnedMobs = new ArrayList<>();
        this.nextActionAt = System.currentTimeMillis() + data.getActionDelaySeconds() * 1000L;
        this.currentTask = MinionTask.FILL;
    }

    public void spawn() {
        entity.editEntityMeta(ArmorStandMeta.class, meta -> {
            meta.setHasNoBasePlate(true);
            meta.setSmall(true);
            meta.setHasArms(true);
            meta.setRightArmRotation(new Vec(0, 0, 15));
            meta.setLeftArmRotation(new Vec(0, 0, -15));
        });
        entity.setNoGravity(true);
        equipEntity();
        entity.setInstance(instance, position);

        warningTop.editEntityMeta(ArmorStandMeta.class, meta -> {
            meta.setInvisible(true);
            meta.setHasNoBasePlate(true);
            meta.setMarker(true);
        });
        warningTop.setNoGravity(true);
        warningTop.setCustomNameVisible(false);
        warningTop.setInstance(instance, position.add(0, 1.55, 0));

        warningBottom.editEntityMeta(ArmorStandMeta.class, meta -> {
            meta.setInvisible(true);
            meta.setHasNoBasePlate(true);
            meta.setMarker(true);
        });
        warningBottom.setNoGravity(true);
        warningBottom.setCustomNameVisible(false);
        warningBottom.setInstance(instance, position.add(0, 1.3, 0));
    }

    public void tick() {
        if (busy) {
            return;
        }
        MinionLayoutValidator.ValidationResult validation = MinionLayoutValidator.validate(this);
        if (!validation.valid()) {
            setWarning(validation.message(), "/!\\");
            return;
        }
        if (storage.totalItems() >= data.getMaxStorage()) {
            setWarning("My storage is full!", "/!\\");
            return;
        }
        clearWarning();

        long now = System.currentTimeMillis();
        if (now < nextActionAt) {
            return;
        }
        nextActionAt = now + data.getActionDelaySeconds() * 1000L;
        performAction();
    }

    protected abstract void performAction();

    public List<ItemStack> collectAll() {
        return storage.collectAll();
    }

    public boolean canUpgrade() {
        return type.hasNextTier(data.getTier());
    }

    public int getUpgradeCost() {
        return type.getUpgradeCost(data.getTier());
    }

    public void upgrade() {
        if (!canUpgrade()) {
            return;
        }
        this.data = new MinionData(type, data.getTier() + 1);
        this.nextActionAt = System.currentTimeMillis() + data.getActionDelaySeconds() * 1000L;
        equipEntity();
        clearWarning();
    }

    public void remove() {
        for (LivingEntity spawnedMob : spawnedMobs) {
            spawnedMob.remove();
        }
        spawnedMobs.clear();
        warningBottom.remove();
        warningTop.remove();
        entity.remove();
    }

    public ItemStack toPlacementItem() {
        return MinionItems.createPlacementItem(type, data.getTier());
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public MinionType getType() {
        return type;
    }

    public MinionProfile getProfile() {
        return profile;
    }

    public MinionData getData() {
        return data;
    }

    public Instance getInstance() {
        return instance;
    }

    public Pos getPosition() {
        return position;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public int getTier() {
        return data.getTier();
    }

    public int getStoredAmount() {
        return storage.totalItems();
    }

    public long getTotalGenerated() {
        return totalGenerated;
    }

    public List<ItemStack> getStoredStacks() {
        return storage.snapshotSlots(data.getStorageSlots());
    }

    public boolean hasIdealLayout() {
        return MinionLayoutValidator.validate(this).valid();
    }

    public MinionTask getCurrentTask() {
        return currentTask;
    }

    protected void setCurrentTask(MinionTask currentTask) {
        this.currentTask = currentTask;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void rotateToward(Pos target) {
        double dx = target.x() - position.x();
        double dz = target.z() - position.z();
        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        entity.teleport(position.withYaw(yaw));
        setHeadRotation(new Vec(20, 0, 0));
    }

    public void setHeadRotation(Vec rotation) {
        entity.editEntityMeta(ArmorStandMeta.class, meta -> meta.setHeadRotation(rotation));
    }

    public void setRightArmRotation(Vec rotation) {
        entity.editEntityMeta(ArmorStandMeta.class, meta -> meta.setRightArmRotation(rotation));
    }

    public void setLeftArmRotation(Vec rotation) {
        entity.editEntityMeta(ArmorStandMeta.class, meta -> meta.setLeftArmRotation(rotation));
    }

    public void broadcastPacket(SendablePacket packet) {
        instance.getPlayers().forEach(player -> player.sendPacket(packet));
    }

    public List<Pos> getMiningPositions() {
        return buildFiveByFivePattern(position.blockY() - 1);
    }

    public List<Pos> getCropPositions() {
        return buildFiveByFivePattern(position.blockY());
    }

    public List<Pos> getTreePositions() {
        int x = position.blockX();
        int y = position.blockY() - 1;
        int z = position.blockZ();
        return List.of(
                new Pos(x + 2, y, z - 2),
                new Pos(x - 2, y, z + 2),
                new Pos(x - 2, y, z - 2),
                new Pos(x + 2, y, z + 2),
                new Pos(x, y, z + 2),
                new Pos(x + 2, y, z),
                new Pos(x - 2, y, z),
                new Pos(x, y, z - 2)
        );
    }

    protected Pos findAir(List<Pos> positions) {
        for (Pos pos : positions) {
            if (instance.getBlock(pos).isAir()) {
                return pos;
            }
        }
        return null;
    }

    protected Pos findBreakBlock(List<Pos> positions, Block blockType) {
        List<Pos> matches = new ArrayList<>();
        for (Pos pos : positions) {
            if (instance.getBlock(pos) == blockType) {
                matches.add(pos);
            }
        }
        if (matches.isEmpty()) {
            return null;
        }
        return matches.get(ThreadLocalRandom.current().nextInt(matches.size()));
    }

    protected Pos findCropPlacement() {
        for (Pos pos : getCropPositions()) {
            if (!instance.getBlock(pos).isAir()) {
                continue;
            }
            Block below = instance.getBlock(pos.blockX(), pos.blockY() - 1, pos.blockZ());
            if (type == MinionType.SUGARCANE) {
                if (below == Block.SAND && hasAdjacentWater(pos)) {
                    return pos;
                }
                continue;
            }
            if (below == profile.idealLayoutBlock()) {
                return pos;
            }
        }
        return null;
    }

    protected Pos findTreePlacement() {
        for (Pos pos : getTreePositions()) {
            Block base = instance.getBlock(pos);
            if (base != Block.DIRT && base != Block.GRASS_BLOCK) {
                continue;
            }
            boolean clear = true;
            for (int y = 1; y <= 4; y++) {
                if (!instance.getBlock(pos.blockX(), pos.blockY() + y, pos.blockZ()).isAir()) {
                    clear = false;
                    break;
                }
            }
            if (clear) {
                return pos;
            }
        }
        return null;
    }

    protected Pos findTreeBreak() {
        for (Pos pos : getTreePositions()) {
            if (instance.getBlock(pos.blockX(), pos.blockY() + 1, pos.blockZ()) == profile.generatedBlock()) {
                return new Pos(pos.blockX(), pos.blockY() + 1, pos.blockZ());
            }
        }
        return null;
    }

    protected Pos findMobSpawnPosition() {
        for (Pos pos : getCropPositions()) {
            if (!instance.getBlock(pos).isAir() || !instance.getBlock(pos.blockX(), pos.blockY() + 1, pos.blockZ()).isAir()) {
                continue;
            }
            if (hasSpawnedMobAt(pos)) {
                continue;
            }
            return pos;
        }
        return null;
    }

    protected LivingEntity findMobToKill() {
        return spawnedMobs.stream()
                .filter(mob -> !mob.isRemoved())
                .min(Comparator.comparingDouble(mob -> mob.getPosition().distanceSquared(position)))
                .orElse(null);
    }

    protected void addSpawnedMob(LivingEntity spawnedMob) {
        spawnedMobs.add(spawnedMob);
    }

    protected void removeSpawnedMob(LivingEntity spawnedMob) {
        spawnedMobs.remove(spawnedMob);
    }

    protected void pruneSpawnedMobs() {
        spawnedMobs.removeIf(LivingEntity::isRemoved);
    }

    protected void createTreeAt(int x, int y, int z) {
        instance.setBlock(x, y + 1, z, profile.generatedBlock());
        instance.setBlock(x, y + 2, z, profile.generatedBlock());
        instance.setBlock(x, y + 3, z, profile.generatedBlock());
        if (profile.secondaryGeneratedBlock() != null) {
            instance.setBlock(x, y + 4, z, profile.secondaryGeneratedBlock());
            instance.setBlock(x + 1, y + 3, z, profile.secondaryGeneratedBlock());
            instance.setBlock(x - 1, y + 3, z, profile.secondaryGeneratedBlock());
            instance.setBlock(x, y + 3, z + 1, profile.secondaryGeneratedBlock());
            instance.setBlock(x, y + 3, z - 1, profile.secondaryGeneratedBlock());
        }
    }

    protected void removeTreeAt(int x, int y, int z) {
        for (int dy = 1; dy <= 4; dy++) {
            Block block = instance.getBlock(x, y + dy, z);
            if (block == profile.generatedBlock() || block == profile.secondaryGeneratedBlock()) {
                instance.setBlock(x, y + dy, z, Block.AIR);
            }
        }
        clearLeaf(x + 1, y + 3, z);
        clearLeaf(x - 1, y + 3, z);
        clearLeaf(x, y + 3, z + 1);
        clearLeaf(x, y + 3, z - 1);
    }

    protected Pos findWater() {
        for (Pos pos : getCropPositions()) {
            if (instance.getBlock(pos) == Block.WATER) {
                return pos;
            }
        }
        return null;
    }

    protected boolean canStoreDrops(List<ItemStack> drops) {
        if (storage.canFit(drops, data.getStorageSlots())) {
            return true;
        }
        setWarning("My storage is full!", "/!\\");
        return false;
    }

    protected void storeDrops(List<ItemStack> drops) {
        storage.addAll(drops, data.getStorageSlots());
        for (ItemStack stack : drops) {
            totalGenerated += stack.amount();
        }
    }

    protected void setWarning(String top, String bottom) {
        warningTop.setCustomName(MINI_MESSAGE.deserialize("<red>" + top));
        warningBottom.setCustomName(MINI_MESSAGE.deserialize("<red>" + bottom));
        warningTop.setCustomNameVisible(true);
        warningBottom.setCustomNameVisible(true);
    }

    protected void clearWarning() {
        warningTop.setCustomNameVisible(false);
        warningBottom.setCustomNameVisible(false);
    }

    private void equipEntity() {
        entity.setEquipment(EquipmentSlot.HELMET, MinionItems.createHead(profile.texture()));
        entity.setEquipment(EquipmentSlot.CHESTPLATE, leather(Material.LEATHER_CHESTPLATE));
        entity.setEquipment(EquipmentSlot.LEGGINGS, leather(Material.LEATHER_LEGGINGS));
        entity.setEquipment(EquipmentSlot.BOOTS, leather(Material.LEATHER_BOOTS));
        entity.setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.of(profile.heldTool(data.getTier())));
    }

    private ItemStack leather(Material material) {
        return ItemStack.builder(material)
                .set(net.minestom.server.component.DataComponents.DYED_COLOR, profile.armorColor())
                .build();
    }

    private List<Pos> buildFiveByFivePattern(int y) {
        List<Pos> positions = new ArrayList<>();
        int baseX = position.blockX();
        int baseZ = position.blockZ();
        int[][] offsets = {
                {0, -1}, {1, 0}, {0, 1}, {-1, 0},
                {1, -1}, {1, 1}, {-1, 1}, {-1, -1},
                {0, -2}, {2, 0}, {0, 2}, {-2, 0},
                {1, -2}, {2, -1}, {2, 1}, {1, 2},
                {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2},
                {2, -2}, {2, 2}, {-2, 2}, {-2, -2}
        };
        for (int[] offset : offsets) {
            positions.add(new Pos(baseX + offset[0], y, baseZ + offset[1]));
        }
        return positions;
    }

    private boolean hasSpawnedMobAt(Pos pos) {
        for (LivingEntity mob : spawnedMobs) {
            if (mob.isRemoved()) {
                continue;
            }
            if (mob.getPosition().blockX() == pos.blockX() && mob.getPosition().blockY() == pos.blockY() && mob.getPosition().blockZ() == pos.blockZ()) {
                return true;
            }
        }
        return false;
    }

    private void clearLeaf(int x, int y, int z) {
        if (instance.getBlock(x, y, z) == profile.secondaryGeneratedBlock()) {
            instance.setBlock(x, y, z, Block.AIR);
        }
    }

    private boolean hasAdjacentWater(Pos pos) {
        return instance.getBlock(pos.blockX() + 1, pos.blockY() - 1, pos.blockZ()) == Block.WATER
                || instance.getBlock(pos.blockX() - 1, pos.blockY() - 1, pos.blockZ()) == Block.WATER
                || instance.getBlock(pos.blockX(), pos.blockY() - 1, pos.blockZ() + 1) == Block.WATER
                || instance.getBlock(pos.blockX(), pos.blockY() - 1, pos.blockZ() - 1) == Block.WATER;
    }
}
