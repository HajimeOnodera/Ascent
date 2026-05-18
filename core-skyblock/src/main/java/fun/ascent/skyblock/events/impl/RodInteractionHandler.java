package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.fishing.WaterHookEntity;
import fun.ascent.skyblock.player.fishing.HookPhase;
import fun.ascent.skyblock.player.fishing.resolve.CatchResolver;
import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.item.registries.FishingBaitRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;

public class RodInteractionHandler extends SEvent<PlayerUseItemEvent> {

    public static final Tag<String> ACTIVE_BAIT_TAG = Tag.String("active_bait");
    public static final Tag<WaterHookEntity> ACTIVE_BOBBER_TAG = Tag.Transient("active_bobber");

    @Override
    public void onEvent(PlayerUseItemEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;
        ItemStack item = event.getItemStack();
        if (item.material() != Material.FISHING_ROD) return;

        WaterHookEntity active = player.getTag(ACTIVE_BOBBER_TAG);

        if (active == null || active.isRemoved()) {
            castHook(player);
        } else {
            retrieveHook(player, active);
        }
    }

    private void castHook(SkyblockPlayer player) {
        WaterHookEntity bobber = new WaterHookEntity(player);
        player.setTag(ACTIVE_BOBBER_TAG, bobber);

        consumeBait(player);

        Pos playerPos = player.getPosition();
        float playerPitch = playerPos.pitch();
        float playerYaw = playerPos.yaw();

        float zDir = (float) Math.cos(Math.toRadians(-playerYaw) - Math.PI);
        float xDir = (float) Math.sin(Math.toRadians(-playerYaw) - Math.PI);

        double spawnX = playerPos.x() - (double) xDir * 0.3;
        double spawnY = playerPos.y() + player.getEyeHeight();
        double spawnZ = playerPos.z() - (double) zDir * 0.3;

        double maxVelocity = 0.4;
        Vec velocity = new Vec(
                -Math.sin(playerYaw / 180.0f * (float) Math.PI) * Math.cos(playerPitch / 180.0f * (float) Math.PI) * maxVelocity,
                -Math.sin(playerPitch / 180.0f * (float) Math.PI) * maxVelocity,
                Math.cos(playerYaw / 180.0f * (float) Math.PI) * Math.cos(playerPitch / 180.0f * (float) Math.PI) * maxVelocity
        );

        double length = velocity.length();
        if (length > 0) {
            velocity = velocity.div(length).mul(1.5);
        }

        bobber.setInstance(player.getInstance(), new Pos(spawnX, spawnY, spawnZ));
        bobber.setVelocity(velocity.mul(15.0));

        player.playSound(Sound.sound(SoundEvent.ENTITY_FISHING_BOBBER_THROW, Sound.Source.MASTER, 0.9f, 1.1f));
    }

    private void retrieveHook(SkyblockPlayer player, WaterHookEntity bobber) {
        player.removeTag(ACTIVE_BOBBER_TAG);

        if (bobber.getPhase() == HookPhase.NIBBLE) {
            CatchResolver.resolve(bobber);
        } else {
            player.playSound(Sound.sound(SoundEvent.ENTITY_FISHING_BOBBER_RETRIEVE, Sound.Source.MASTER, 1.0f, 1.2f));
        }

        player.removeTag(ACTIVE_BAIT_TAG);
        bobber.remove();
    }

    private void consumeBait(SkyblockPlayer player) {
        var inv = player.getInventory();
        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack stack = inv.getItemStack(slot);
            if (ItemNBT.isSkyblockItem(stack)) {
                String itemId = ItemNBT.getItemId(stack);
                if (itemId != null && FishingBaitRegistry.isFishingBait(itemId)) {
                    player.setTag(ACTIVE_BAIT_TAG, itemId);
                    int remaining = stack.amount() - 1;
                    inv.setItemStack(slot, remaining <= 0 ? ItemStack.AIR : stack.withAmount(remaining));
                    break;
                }
            }
        }
    }
}
