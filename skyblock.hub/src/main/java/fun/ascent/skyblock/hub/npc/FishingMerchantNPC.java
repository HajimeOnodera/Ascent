package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record FishingMerchantNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_fishing_merchant"; }
    @Override public String name() { return "<aqua>Fishing Merchant"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NTkxMzU3NjQ4NDMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTJjNGVlMDQ3OWI5MDFmMTc3YzI4Njg5NWRhMzEwYTgwNmRmZTg5N2M5YTg2NzhiOWRlODhhYmJiMWJmZjRiMSJ9fX0=", "piUjhfqpIwGQ/JbUxauA4SOGkH/2Li6Sb5y4ErzIob6Nv1Ai/BFyKac5aEroxh53M9a9Qdqz13tXnS03nlykCGXtlhFhJ/H/pEgIKGEFRvRmgF3L9R7u4ZYqZT7R89m6yruGCr+37ZUUk1rSF7idx7TtC0IFJt9k+QFOyQqtk2vjka8POPGQ2nXPqLnDaKi8TlAvlCIEWR3z0hSN4pLOupXFMF99a49GX0CWj7ewB6vJl+sEinNSYyviYPK7ncVYfJcn4gSN9EtsI1PQzAT2Tb3/EntzoBk88RLxpJNKjcySTzs2yALlRyUAtsn4QkOyoIBOkSEhixDY7BHd46ALllo5HwdzKjExc5JK6HUL/KdsRXIfK3ZEJ8QodhJUiwn7JkiMtMmLTpebIZwVWDQIB+1fz9KQ7bUq01OkQx4bGeIfg6H0nMRY/gPAVWSvAZbJwZkeJ811RbNbgHozfuvo2/Bq74KTiobxkD+M4MbfXfTi1XjawDZg+W/Jzb313cDL1EbgyU+DIDQUA4aPtoVcGFu3l9BoM8Yb+mD0XiMxwxw9cJGLS6f+xg5sG4IgK5RMpYjMuq9Mrt2ebu7nxx5JqjygJcxqy1/YQt4k4BL/D0tfZbmSjsQ45RZ7uMjP0FtALhFusLfykALCP78hYm8q4to8pDmIhvdrjssUVu0C9Uk="); }
    @Override public Pos position() { return new Pos(112.5, 71, -44.5, 0, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome, angler!", "I sell all kinds of fishing supplies and buy your catches.", "Come see me whenever you need bait, rods, or want to sell fish!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need fishing supplies? Take a look at what I've got!"); }
}
