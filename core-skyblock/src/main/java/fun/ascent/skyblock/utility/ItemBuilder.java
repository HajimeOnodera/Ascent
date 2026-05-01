package fun.ascent.skyblock.utility;

import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.ResolvableProfile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private final ItemStack.Builder builder;

    public ItemBuilder(Material material) {
        // Minestom handles item creation through builders rather than direct instantiation
        this.builder = ItemStack.builder(material);
    }

    /**
     * Adds or removes the glowing effect on the item.
     * In 1.21, this is done cleanly using the ENCHANTMENT_GLINT_OVERRIDE component.
     */
    public ItemBuilder setGlowing(boolean glowing) {
        this.builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, glowing);
        return this;
    }

    /**
     * Finalizes and returns the Minestom ItemStack.
     */
    public ItemStack build() {
        return builder.build();
    }

    /**
     * Takes a raw texture hash, formats it into a Mojang texture URL,
     * encodes it to Base64, and applies it to a Player Head.
     */
    public static ItemStack getHead(String textureHash) {
        String url = "http://textures.minecraft.net/texture/" + textureHash;
        String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";

        String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

        ResolvableProfile profile = new ResolvableProfile(
                new ResolvableProfile.Partial(
                        "",UUID.randomUUID(),
                        List.of(new GameProfile.Property("textures",base64,""))));

        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(DataComponents.PROFILE, profile)
                .build();
    }
}
