package fun.ascent.lobby.item;

import net.minestom.server.entity.Player;

public class TestMinestomApi {
    public void test(Player player) {
        player.updateViewableRule(entity -> false);
        player.updateViewerRule(viewer -> false);
    }
}
