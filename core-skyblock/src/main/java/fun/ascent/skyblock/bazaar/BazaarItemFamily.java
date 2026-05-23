package fun.ascent.skyblock.bazaar;

import net.minestom.server.item.Material;
import java.util.List;

public class BazaarItemFamily {
    public final String name;
    public final Material icon;
    public final List<String> productIds;

    public BazaarItemFamily(String name, Material icon, List<String> productIds) {
        this.name = name;
        this.icon = icon;
        this.productIds = productIds;
    }
}
