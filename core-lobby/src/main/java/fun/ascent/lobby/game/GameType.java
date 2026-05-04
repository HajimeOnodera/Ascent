package fun.ascent.lobby.game;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.lobby.cache.ServerInfoCache;
import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

@Getter
public enum GameType {
    SKYBLOCK("SkyBlock",
            ItemStackCreator.getStackHead("686718d85e25b006f2c8f160f619b23c8fd6ae75ddf1c06308ec0f539d931703"),
            Category.PERSISTENT_GAME,
            "skyblock",
            "§7SkyBlock has finally arrived on",
            "§7Hypixel! Play with friends (or solo!),",
            "§7build your private islands and",
            "§7collect all the items!"),
    ;

    private final String displayName;
    private final ItemStack.Builder item;
    private final Category category;
    private final String serverPrefix;
    private final String[] lore;

    GameType(String displayName, ItemStack.Builder item, Category category, String serverPrefix, String... lore) {
        this.displayName = displayName;
        this.item = item;
        this.category = category;
        this.serverPrefix = serverPrefix;
        this.lore = lore;
    }

    GameType(String displayName, Material item, Category category, String serverPrefix, String... lore) {
        this.displayName = displayName;
        this.item = ItemStackCreator.getStack("", item, 1);
        this.category = category;
        this.serverPrefix = serverPrefix;
        this.lore = lore;
    }

    /**
     * Check if this game type is implemented and playable.
     */
    public boolean isImplemented() {
        return serverPrefix != null;
    }

    /**
     * Get the total player count for this game type using Redis server discovery.
     */
    public int getPlayerCount() {
        return ServerInfoCache.getTotalPlayersForType(this);
    }

    public enum Category {
        PROTOTYPE_GAME,
        PERSISTENT_GAME,
        SURVIVAL,
        TEAM_SURVIVAL,
        COMPETITIVE,
        CASUAL_GAMES,
    }
}
