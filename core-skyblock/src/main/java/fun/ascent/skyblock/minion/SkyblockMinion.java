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
import java.util.List;
import java.util.UUID;

public final class SkyblockMinion {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final UUID id;
    private final UUID ownerUuid;
    private final Instance instance;
    private final Pos position;
    private final LivingEntity entity;
    private final Entity warningTop;
    private final Entity warningBottom;
    private final MinionStorage storage;

    private MinionType type;
    private MinionData data;
    private LivingEntity spawnedMob;
    private long nextActionAt;
    private long totalGenerated;
    private boolean busy;

    public SkyblockMinion(UUID ownerUuid, MinionType type, int tier, Instance instance, Pos position) {
        this.id = UUID.randomUUID();
        this.ownerUuid = ownerUuid;
        this.type = type;
        this.data = new MinionData(type, tier);
        this.instance = instance;
        this.position = position;
        this.entity = new LivingEntity(EntityType.ARMOR_STAND);
        this.warningTop = new Entity(EntityType.ARMOR_STAND);
        this.warningBottom = new Entity(EntityType.ARMOR_STAND);
        this.storage = new MinionStorage();
        this.nextActionAt = System.currentTimeMillis() + data.getActionDelaySeconds() * 1000L;
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
        switch (type.getActionKind()) {
            case MINING -> tickMining();
            case CROP -> tickCrop();
            case TREE -> tickTree();
            case MOB -> tickMob();
            case FISHING -> tickFishing();
        }
    }

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
        if (spawnedMob != null) {
            spawnedMob.remove();
        }
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
        List<Pos> positions = new ArrayList<>();
        int baseX = position.blockX();
        int baseY = position.blockY() - 1;
        int baseZ = position.blockZ();
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                positions.add(new Pos(baseX + x, baseY, baseZ + z));
            }
        }
        return positions;
    }

    public List<Pos> getCropPositions() {
        List<Pos> positions = new ArrayList<>();
        int baseX = position.blockX();
        int baseY = position.blockY();
        int baseZ = position.blockZ();
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                positions.add(new Pos(baseX + x, baseY, baseZ + z));
            }
        }
        return positions;
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

    private void tickMining() {
        Pos breakPos = findBreakBlock(getMiningPositions(), type.getGeneratedBlock());
        if (breakPos != null) {
            List<ItemStack> drops = type.createHarvestDrops();
            if (!storage.canFit(drops, data.getStorageSlots())) {
                setWarning("My storage is full!", "/!\\");
                return;
            }
            Block block = instance.getBlock(breakPos);
            MinionAnimation.animateBreak(this, breakPos, block, () -> {
                instance.setBlock(breakPos, Block.AIR);
                storeDrops(drops);
            });
            return;
        }
        Pos placePos = findAir(getMiningPositions());
        if (placePos != null) {
            MinionAnimation.animatePlace(this, placePos, () -> instance.setBlock(placePos, type.getGeneratedBlock()));
        }
    }

    private void tickCrop() {
        Pos breakPos = findBreakBlock(getCropPositions(), type.getGeneratedBlock());
        if (breakPos != null) {
            List<ItemStack> drops = type.createHarvestDrops();
            if (!storage.canFit(drops, data.getStorageSlots())) {
                setWarning("My storage is full!", "/!\\");
                return;
            }
            Block block = instance.getBlock(breakPos);
            MinionAnimation.animateBreak(this, breakPos, block, () -> {
                instance.setBlock(breakPos, Block.AIR);
                storeDrops(drops);
            });
            return;
        }
        Pos placePos = findCropPlacement();
        if (placePos != null) {
            MinionAnimation.animatePlace(this, placePos, () -> instance.setBlock(placePos, type.getGeneratedBlock()));
        }
    }

    private void tickTree() {
        Pos breakPos = findTreeBreak();
        if (breakPos != null) {
            List<ItemStack> drops = List.of(ItemStack.of(type.getOutputMaterial(), 3));
            if (!storage.canFit(drops, data.getStorageSlots())) {
                setWarning("My storage is full!", "/!\\");
                return;
            }
            Block block = instance.getBlock(breakPos);
            MinionAnimation.animateBreak(this, breakPos, block, () -> {
                removeTreeAt(breakPos.blockX(), breakPos.blockY() - 1, breakPos.blockZ());
                storeDrops(drops);
            });
            return;
        }
        Pos placePos = findTreePlacement();
        if (placePos != null) {
            MinionAnimation.animatePlace(this, placePos.add(0, 1, 0), () -> createTreeAt(placePos.blockX(), placePos.blockY(), placePos.blockZ()));
        }
    }

    private void tickMob() {
        if (spawnedMob != null && !spawnedMob.isRemoved()) {
            List<ItemStack> drops = type.createHarvestDrops();
            if (!storage.canFit(drops, data.getStorageSlots())) {
                setWarning("My storage is full!", "/!\\");
                return;
            }
            Pos target = spawnedMob.getPosition();
            MinionAnimation.animateKill(this, target, () -> {
                spawnedMob.remove();
                spawnedMob = null;
                storeDrops(drops);
            });
            return;
        }
        Pos spawnPos = findAir(getCropPositions());
        if (spawnPos != null && type.getMobEntityType() != null) {
            MinionAnimation.animatePlace(this, spawnPos, () -> {
                spawnedMob = new LivingEntity(type.getMobEntityType());
                spawnedMob.setInstance(instance, spawnPos.add(0.5, 0, 0.5));
            });
        }
    }

    private void tickFishing() {
        Pos waterPos = findWater();
        if (waterPos == null) {
            setWarning("This location is not perfect!", "/!\\");
            return;
        }
        List<ItemStack> drops = type.createHarvestDrops();
        if (!storage.canFit(drops, data.getStorageSlots())) {
            setWarning("My storage is full!", "/!\\");
            return;
        }
        MinionAnimation.animateFishing(this, waterPos, () -> storeDrops(drops));
    }

    private void equipEntity() {
        entity.setEquipment(EquipmentSlot.HELMET, MinionItems.createHead(type.getTexture()));
        entity.setEquipment(EquipmentSlot.CHESTPLATE, leather(Material.LEATHER_CHESTPLATE));
        entity.setEquipment(EquipmentSlot.LEGGINGS, leather(Material.LEATHER_LEGGINGS));
        entity.setEquipment(EquipmentSlot.BOOTS, leather(Material.LEATHER_BOOTS));
        entity.setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.of(type.getHeldTool(data.getTier())));
    }

    private ItemStack leather(net.minestom.server.item.Material material) {
        return ItemStack.builder(material)
                .set(net.minestom.server.component.DataComponents.DYED_COLOR, type.getArmorColor())
                .build();
    }

    private void setWarning(String top, String bottom) {
        warningTop.setCustomName(MINI_MESSAGE.deserialize("<red>" + top));
        warningBottom.setCustomName(MINI_MESSAGE.deserialize("<red>" + bottom));
        warningTop.setCustomNameVisible(true);
        warningBottom.setCustomNameVisible(true);
    }

    private void clearWarning() {
        warningTop.setCustomNameVisible(false);
        warningBottom.setCustomNameVisible(false);
    }

    private void storeDrops(List<ItemStack> drops) {
        storage.addAll(drops, data.getStorageSlots());
        for (ItemStack stack : drops) {
            totalGenerated += stack.amount();
        }
    }

    private Pos findBreakBlock(List<Pos> positions, Block blockType) {
        for (Pos pos : positions) {
            if (instance.getBlock(pos) == blockType) {
                return pos;
            }
        }
        return null;
    }

    private Pos findAir(List<Pos> positions) {
        for (Pos pos : positions) {
            if (instance.getBlock(pos).isAir()) {
                return pos;
            }
        }
        return null;
    }

    private Pos findCropPlacement() {
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
            if (below == type.getIdealLayoutBlock()) {
                return pos;
            }
        }
        return null;
    }

    private Pos findTreePlacement() {
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

    private Pos findTreeBreak() {
        for (Pos pos : getTreePositions()) {
            if (instance.getBlock(pos.blockX(), pos.blockY() + 1, pos.blockZ()) == type.getGeneratedBlock()) {
                return new Pos(pos.blockX(), pos.blockY() + 1, pos.blockZ());
            }
        }
        return null;
    }

    private void createTreeAt(int x, int y, int z) {
        instance.setBlock(x, y + 1, z, type.getGeneratedBlock());
        instance.setBlock(x, y + 2, z, type.getGeneratedBlock());
        instance.setBlock(x, y + 3, z, type.getGeneratedBlock());
        if (type.getSecondaryGeneratedBlock() != null) {
            instance.setBlock(x, y + 4, z, type.getSecondaryGeneratedBlock());
            instance.setBlock(x + 1, y + 3, z, type.getSecondaryGeneratedBlock());
            instance.setBlock(x - 1, y + 3, z, type.getSecondaryGeneratedBlock());
            instance.setBlock(x, y + 3, z + 1, type.getSecondaryGeneratedBlock());
            instance.setBlock(x, y + 3, z - 1, type.getSecondaryGeneratedBlock());
        }
    }

    private void removeTreeAt(int x, int y, int z) {
        for (int dy = 1; dy <= 4; dy++) {
            Block block = instance.getBlock(x, y + dy, z);
            if (block == type.getGeneratedBlock() || block == type.getSecondaryGeneratedBlock()) {
                instance.setBlock(x, y + dy, z, Block.AIR);
            }
        }
        clearLeaf(x + 1, y + 3, z);
        clearLeaf(x - 1, y + 3, z);
        clearLeaf(x, y + 3, z + 1);
        clearLeaf(x, y + 3, z - 1);
    }

    private void clearLeaf(int x, int y, int z) {
        if (instance.getBlock(x, y, z) == type.getSecondaryGeneratedBlock()) {
            instance.setBlock(x, y, z, Block.AIR);
        }
    }

    private Pos findWater() {
        for (Pos pos : getCropPositions()) {
            if (instance.getBlock(pos) == Block.WATER) {
                return pos;
            }
        }
        return null;
    }

    private boolean hasAdjacentWater(Pos pos) {
        return instance.getBlock(pos.blockX() + 1, pos.blockY() - 1, pos.blockZ()) == Block.WATER
                || instance.getBlock(pos.blockX() - 1, pos.blockY() - 1, pos.blockZ()) == Block.WATER
                || instance.getBlock(pos.blockX(), pos.blockY() - 1, pos.blockZ() + 1) == Block.WATER
                || instance.getBlock(pos.blockX(), pos.blockY() - 1, pos.blockZ() - 1) == Block.WATER;
    }
}
