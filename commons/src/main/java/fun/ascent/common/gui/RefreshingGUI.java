package fun.ascent.common.gui;

import net.minestom.server.entity.Player;

@Deprecated
public interface RefreshingGUI {
    /**
     * If the GUI implements this method, this is the method that should be using in setting all the items
     * This is called async
     */
    void refreshItems(Player player);

    /**
     * How long between each refresh (ticks)
     *
     * @return time in ticks
     */
    int refreshRate();
}
