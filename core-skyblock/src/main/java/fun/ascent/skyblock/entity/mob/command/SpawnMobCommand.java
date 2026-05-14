package fun.ascent.skyblock.entity.mob.command;

import fun.ascent.skyblock.entity.mob.mobs.graveyard.GraveyardZombie;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class SpawnMobCommand extends Command {

    public SpawnMobCommand() {
        super("spawnmob");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player player)) return;

            GraveyardZombie zombie = new GraveyardZombie();
            zombie.setInstance(player.getInstance(), player.getPosition().add(2, 0, 0));
        });
    }
}
