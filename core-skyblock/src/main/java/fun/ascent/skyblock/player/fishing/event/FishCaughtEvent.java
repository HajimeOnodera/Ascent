package fun.ascent.skyblock.player.fishing.event;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.fishing.resolve.CatchResult;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class FishCaughtEvent implements PlayerEvent {

    private final SkyblockPlayer player;
    private final CatchResult catchResult;

    public FishCaughtEvent(@NotNull SkyblockPlayer player, @NotNull CatchResult catchResult) {
        this.player = player;
        this.catchResult = catchResult;
    }

    @Override
    public @NotNull SkyblockPlayer getPlayer() {
        return player;
    }

    public @NotNull CatchResult getCatchResult() {
        return catchResult;
    }
}
