package fun.ascent.skyblock.bazaar;

import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BazaarItem {
    private String productId;
    private boolean isInfinite;
    private BazaarCategory category;
}
