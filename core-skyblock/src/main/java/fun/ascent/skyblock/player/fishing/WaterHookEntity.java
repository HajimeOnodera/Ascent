package fun.ascent.skyblock.player.fishing;

import fun.ascent.skyblock.player.fishing.bait.BaitDefinitions;
import fun.ascent.skyblock.player.fishing.bait.BaitEffect;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.events.impl.RodInteractionHandler;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.FishingHookMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class WaterHookEntity extends Entity {

    private final SkyblockPlayer owner;
    private HookPhase phase = HookPhase.AIRBORNE;
    private Entity latchedEntity;
    private int ticksInGround = 0;
    private int ticksInState = 0;
    private int biteCountdownTicks = -1;
    private int nibbleDurationTicks = -1;

    private double wakeAngle = 0;
    private double wakeDistance = -1;

    public WaterHookEntity(@NotNull SkyblockPlayer owner) {
        super(EntityType.FISHING_BOBBER);
        this.owner = owner;
        setOwnerEntity(owner);
        
        setAerodynamics(getAerodynamics()
                .withGravity(0)
                .withHorizontalAirResistance(0.92)
                .withVerticalAirResistance(0.92));
    }

    private void setOwnerEntity(@Nullable Entity entity) {
        ((FishingHookMeta) getEntityMeta()).setOwnerEntity(entity);
    }

    private void setHookedEntity(@Nullable Entity entity) {
        ((FishingHookMeta) getEntityMeta()).setHookedEntity(entity);
    }

    @Override
    public void tick(long time) {
        ticksInState++;
        Instance instance = getInstance();
        if (instance == null || owner.isRemoved() || !owner.isOnline() || isRemoved()) {
            remove();
            return;
        }

        boolean main = owner.getItemInMainHand().material() == net.minestom.server.item.Material.FISHING_ROD;
        boolean off = owner.getItemInOffHand().material() == net.minestom.server.item.Material.FISHING_ROD;
        if (owner.isDead() || (!main && !off) || getDistanceSquared(owner) > 1024.0) {
            remove();
            return;
        }

        if (phase == HookPhase.AIRBORNE) {
            setVelocity(getVelocity().add(0, -0.04 * 20.0, 0));
            checkEntityCollisions();
            if (isInWater()) {
                enterWater();
            } else if (onGround) {
                ticksInGround++;
                if (ticksInGround > 300) {
                    remove();
                }
            } else {
                ticksInGround = 0;
            }
        } else if (phase == HookPhase.BOBBING) {
            applyWaterFloatPhysics();
            if (!isInWater()) {
                phase = HookPhase.AIRBORNE;
                ticksInState = 0;
                biteCountdownTicks = -1;
                wakeDistance = -1;
            } else {
                handleBobbingLogic();
            }
        } else if (phase == HookPhase.NIBBLE) {
            applyWaterFloatPhysics();
            if (!isInWater()) {
                phase = HookPhase.AIRBORNE;
                ticksInState = 0;
                biteCountdownTicks = -1;
                wakeDistance = -1;
            } else {
                handleNibbleLogic();
            }
        } else if (phase == HookPhase.LATCHED) {
            if (latchedEntity == null || latchedEntity.isRemoved() || latchedEntity.getInstance() != instance) {
                latchedEntity = null;
                setHookedEntity(null);
                phase = HookPhase.AIRBORNE;
                ticksInState = 0;
            } else {
                Pos targetPos = latchedEntity.getPosition();
                teleport(targetPos.withY(targetPos.y() + latchedEntity.getBoundingBox().height() * 0.75));
            }
        }

        try {
            super.tick(time);
        } catch (Exception ignored) {}
    }

    private void applyWaterFloatPhysics() {
        Vec vel = getVelocity();
        Pos pos = getPosition();
        Instance inst = getInstance();
        if (inst == null) return;

        Block block = inst.getBlock(pos.blockX(), pos.blockY(), pos.blockZ());
        Block blockAbove = inst.getBlock(pos.blockX(), pos.blockY() + 1, pos.blockZ());

        double targetY = pos.blockY() + 0.85;
        double dy = targetY - pos.y();

        if (block == Block.WATER && blockAbove != Block.WATER) {
            double sineBob = Math.sin(ticksInState * 0.15) * 0.05;
            if (phase == HookPhase.NIBBLE) {
                sineBob -= 0.15;
            }
            double forceY = (dy + sineBob) * 0.1;
            vel = new Vec(vel.x() * 0.8, forceY, vel.z() * 0.8);
        } else if (block == Block.WATER && blockAbove == Block.WATER) {
            vel = new Vec(vel.x() * 0.8, 0.15, vel.z() * 0.8);
        } else {
            vel = vel.add(0, -0.03 * 20.0, 0).mul(0.9);
        }

        setVelocity(vel);
    }

    private void checkEntityCollisions() {
        Instance inst = getInstance();
        if (inst == null) return;

        for (Entity ent : inst.getEntities()) {
            if (ent == this || ent == owner) continue;
            if (ent.getDistanceSquared(this) < 1.0) {
                latchedEntity = ent;
                setHookedEntity(ent);
                phase = HookPhase.LATCHED;
                ticksInState = 0;
                setVelocity(Vec.ZERO);
                break;
            }
        }
    }

    private boolean isInWater() {
        Instance inst = getInstance();
        if (inst == null) return false;
        Pos pos = getPosition();
        Block b1 = inst.getBlock(pos.blockX(), pos.blockY(), pos.blockZ());
        Block b2 = inst.getBlock(pos.blockX(), (int) Math.floor(pos.y() - 0.4), pos.blockZ());
        return b1 == Block.WATER || b2 == Block.WATER;
    }

    private void enterWater() {
        phase = HookPhase.BOBBING;
        ticksInState = 0;
        setVelocity(getVelocity().mul(0.1));

        double baseTimeSec = ThreadLocalRandom.current().nextDouble(5.0, 25.0);
        double fishingSpeed = owner.playerStat(Stats.FISHING_SPEED);
        double multiplier = 100.0 / (100.0 + fishingSpeed);
        double timeSec = baseTimeSec * multiplier;

        String baitId = owner.getTag(RodInteractionHandler.ACTIVE_BAIT_TAG);
        if (baitId != null) {
            BaitEffect effect = BaitDefinitions.get(baitId);
            timeSec = Math.max(1.5, timeSec - (effect.biteTimeReduction() / 20.0));
        }

        biteCountdownTicks = (int) (timeSec * 20.0);
        wakeDistance = -1;
    }

    private void handleBobbingLogic() {
        if (biteCountdownTicks > 0) {
            biteCountdownTicks--;

            if (biteCountdownTicks <= 80) {
                if (wakeDistance < 0) {
                    wakeAngle = ThreadLocalRandom.current().nextDouble(0, 2.0 * Math.PI);
                    wakeDistance = 4.0;
                }

                wakeDistance = 4.0 * (biteCountdownTicks / 80.0);

                Pos p = getPosition();
                double wx = p.x() + Math.cos(wakeAngle) * wakeDistance;
                double wz = p.z() + Math.sin(wakeAngle) * wakeDistance;
                double rx = ThreadLocalRandom.current().nextDouble(-0.1, 0.1);
                double rz = ThreadLocalRandom.current().nextDouble(-0.1, 0.1);

                Pos wakePos = new Pos(wx + rx, p.y() - 0.05, wz + rz);

                if (ThreadLocalRandom.current().nextBoolean()) {
                    sendPacketToViewersAndSelf(new ParticlePacket(Particle.BUBBLE, wakePos, new Vec(0.05, 0.0, 0.05), 0.01f, 2));
                }
                if (ThreadLocalRandom.current().nextDouble() < 0.35) {
                    sendPacketToViewersAndSelf(new ParticlePacket(Particle.SPLASH, wakePos, new Vec(0.05, 0.0, 0.05), 0.01f, 1));
                }
            }

            if (biteCountdownTicks <= 0) {
                triggerBite();
            }
        }
    }

    private void triggerBite() {
        phase = HookPhase.NIBBLE;
        ticksInState = 0;
        nibbleDurationTicks = ThreadLocalRandom.current().nextInt(30, 60);

        Instance inst = getInstance();
        if (inst != null) {
            Pos p = getPosition();
            sendPacketToViewersAndSelf(new ParticlePacket(Particle.SPLASH, p.add(0, 0.1, 0), new Vec(0.25, 0.05, 0.25), 0.1f, 18));
            owner.playSound(Sound.sound(SoundEvent.ENTITY_FISHING_BOBBER_SPLASH, Sound.Source.MASTER, 1f, 1f));
        }
    }

    private void handleNibbleLogic() {
        Instance inst = getInstance();
        if (inst != null && ticksInState % 4 == 0) {
            Pos p = getPosition();
            sendPacketToViewersAndSelf(new ParticlePacket(Particle.BUBBLE, p.add(0, -0.05, 0), new Vec(0.1, 0.02, 0.1), 0.02f, 4));
        }

        if (nibbleDurationTicks > 0) {
            nibbleDurationTicks--;
            if (nibbleDurationTicks <= 0) {
                missBite();
            }
        }
    }

    private void missBite() {
        phase = HookPhase.BOBBING;
        ticksInState = 0;
        biteCountdownTicks = (int) (ThreadLocalRandom.current().nextDouble(4.0, 12.0) * 20.0);
        wakeDistance = -1;
    }

    @Override
    public void remove() {
        if (owner.getTag(RodInteractionHandler.ACTIVE_BOBBER_TAG) == this) {
            owner.removeTag(RodInteractionHandler.ACTIVE_BOBBER_TAG);
        }
        super.remove();
    }

    public HookPhase getPhase() {
        return phase;
    }

    public SkyblockPlayer getOwner() {
        return owner;
    }

    public Entity getLatchedEntity() {
        return latchedEntity;
    }
}
