package fun.ascent.common.item;

import net.minestom.server.item.Material;

public record GUIMaterial(Material material, String texture) {
    public GUIMaterial(Material material) {
        this(material, null);
    }

    public GUIMaterial(String texture) {
        this(Material.PLAYER_HEAD, texture);
    }

    public boolean hasTexture() {
        return texture != null && !texture.isEmpty();
    }
}
