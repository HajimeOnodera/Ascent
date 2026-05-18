package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record FarmMerchantNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_farm_merchant"; }
    @Override public String name() { return "<gold>Farm Merchant"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NjAxOTM1MzI5MjIsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzgzOTAzODQ0ODczOTk4ZmRjYThmOTVjMjNiMDZlMTA5NTQ5NGZkZmYyY2YzMzgyNGE1YzU2MGU0NjU0Mjc0ZmMifX19", "JzFsBS2+uF9KejwuhN9Y/ri1D7GckhODpugutLbPBdtCr4me3NURwE6owdr/rY3EUa2S36bbqRucuTsEM/HU3Tti4MXUqNJv9RnsZsr/o528EtpA4LfDMBO+MZKBXKvspe5rLYl1lb6ZVgWCJ1hTm7s6yhA9bBqZ5mtcRsgwMJV5fY9uzydiOuUe15vtLzDYrXfmuu8USoR6EIQOeSQe1Ui0RLE3MmueURwXBXOzdtYnLwdsdOKofv4haRe7W74f1Xab6hxRW3oyhtm4pKune0sOyxGsNA8/DQ0vgAWKF712F+DoRGEvVGiyhXIqMl9xEYi+oKcMIOWdkj9vwvIuJBuXKsagPMIMVKveKu/agP2OjFf71KY4ob/nreRKhLR2aNU5qpzC+KXbMa88gkiZUvEAmzzIECY/RgwI0DFxkL43GXetKQQ2tfElPvkgeqhnQkArOYFXIPlRQI01E8dku9ekKuSkLiNClwN4Knaw7OOYpPSe65viYUhREj3UWYzj3Al40ut7MEP7565pj4OKxKQ2ToOcfT9Zm/2FJ4X9PPN+ubKVAq1la1cZuh/O78lV3k5v/6UlhXXMTuKPVep1U86rd0Fdp1E/nYReKoJHX1NIg7xynSk2qy8dol9OZbLlKdxo00rrinckpoAY/GeABu8FR+ZhFgCqDdOaVo1Z+wQ="); }
    @Override public Pos position() { return new Pos(63.5, 72, -113.5, 45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome to my farm stand!", "I buy and sell all kinds of crops and farming supplies.", "Come back anytime you need seeds or want to sell your harvest!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking to buy or sell some crops?"); }
}
