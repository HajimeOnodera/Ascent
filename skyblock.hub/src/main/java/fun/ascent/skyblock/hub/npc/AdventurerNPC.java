package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import fun.ascent.skyblock.hub.shop.ShopMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record AdventurerNPC(Instance instance) implements NpcDefinition {

    @Override
    public String id() {
        return "hub_adventurer";
    }

    @Override
    public String name() {
        return "<gold>Adventurer";
    }

    @Override
    public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTYxOTAwNTE5NDkzNCwKICAicHJvZmlsZUlkIiA6ICJiYjdjY2E3MTA0MzQ0NDEyOGQzMDg5ZTEzYmRmYWI1OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsYXVyZW5jaW8zMDMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMwN2IwMjJhOGQ0M2NiZTA1ODIxMDM0MGRjMmQ1YWE1ZGQ0YTRkMjk5YTNhOWI1MjM4ZmFiY2QxYmMzMGY3NiIKICAgIH0KICB9Cn0=",
                "ptaQtcV3bDRoRSN7Ix50CQOvSOcWBFydHmDasDAKQA+DQYrFm3CcKvaY/gdwkzr3MfwkdLerXUdmISIIj3iaDc0lOHTgoMo5txe+c9CtGxgYNsPhtWlNNgdTzwoET9ZNkORKEmM7qL8wc7LrLXHQqtFvEUlnjE+rvV7KhQzI1Ogeo4LmZY6DWGWEGjfA36FYZ25W0BuN67mKuJOfmeXJmrElNPq8TwZ3gkCwz/jxFxM2R3+J3mNHQlLNqmS4v66ET9Oij1YJ4sqJ8ZzHQpgWlR2iTEaWm4PbkS87lto9SOQo/Z7L20lutMiu6ROGRW2yMq/TE/yHX8pqfplnyMOHwK8ags4o4tIXePQY3BgqtKghLjGesUUsGOi4PZJRPzEQBhsh1VT1wXlJROwfXN+jk3q2XXKXxyPIUAgY+MsKE0c+hs6NoEuP3phNofn3no1kLkDgTEuHKFBajJFy+sXI5lyEe4HpT8nnfohIrokVF7iIFEGLRqCemw686a/X6kBZkA3AYLXvBpCODSXBHuwXulu8eaZWHC5psxxkGh5AHIepAZ8hXrFkHECPfjJtN/4dFcsKTprckUWrlAAUoYy8IEAOBXUTt+SJk/VkHOhogac21kVXjxzSSMPzyeNfi95m60GvONYUIW8xj0TEsjCtiXADQ/zqLGzHpI67l31Zkmg="
        );
    }

    @Override
    public Pos position() {
        return new Pos(-49.5, 69, -66.5,-90,0);
    }

    @Override public String[] firstInteractionMessages() { return new String[]{
            "I've seen it all - every island from here to the edge of the world!",
            "Over the years I've acquired a variety of Talismans and Artifact.",
            "For a price, you can have it all!",
            "Click me again to open the Adventurer Shop!"}; }

    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }

    @Override public void onInteract(Player player, AscentNpc npc) { ShopMenu.open((SkyblockPlayer) player, "adv_shop"); }
}

