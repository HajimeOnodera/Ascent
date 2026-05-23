package fun.ascent.skyblock.bazaar.price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Price {
    private double buyPrice;
    private double sellPrice;
    private long buyVolume;
    private long sellVolume;
    private int buyOrders;
    private int sellOrders;
}
