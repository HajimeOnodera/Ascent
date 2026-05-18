package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record AnglerAngusNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_angler_angus"; }
    @Override public String name() { return "<gold>Angler Angus"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTc0MTI3MjI2MzY4MSwKICAicHJvZmlsZUlkIiA6ICIzZDIxZTYyMTk2NzQ0Y2QwYjM3NjNkNTU3MWNlNGJlZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTcl83MUJsYWNrYmlyZCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80YzRlYWY5OTM5ZTVjMGQ5NjRjZmZjOTUzZTM1YzEyMTA2Y2ZlZDlkNWFiMGNlZTE1YjY5YTE2MmEyNzA0MDciCiAgICB9CiAgfQp9",
                "yB8hnTVPlCklgjGtTA3DeSCmy2bbBt7eV21yIYBGrnXgdRW9pYITN/qvg6X9et7GPE8wh3w445tnSF8zlmwhpJHrsrEM+mjmapV1VWzca93wU0Ds6zpBxlNOjXsLrCkURTolaf9ujJXpvqFf0OxbzGjlbQ4PC9U8qog004NCT/ZpgDGlXdOIZuZAzEK++nyxcrc5QOpnv8X9VKOBjS0M5OXW1/3p7nrTr8bZPgsKQ6A9cSE+564JaqJXf9VIUfrSqR0BHsbcc7VN69MOeupsAZLxx6xmNeHnXFkSW5/a1/YS5s8pFLYuS9R4lK0XED+AYeH57xFqFnb97wjIzJa5rTUv8/yYZBHcZgWge/Q3s5jNdN3sb/ouxEfrgQRFqywmjpafC7++3LY02XLXrPPvANxZdAbz1zwBGT/8ydFKf1t7YSlgj1Guro89dRm7YCr0G96sVuntPNjiBYhpgXeZO2cVpi2XOhWFzpQaM0pa7aLiT+/LCApf7xVGvMeicuQd/m/0jCr/Usid6L2Yat75RrrHMSugd+3wQUGXw5aItGog4iNWb+sZEWg5AQ2GaK9EheToglHltg6jobHr99hsTE8bZN8Lg7RzY4FRgOiFKDX95kbnm4aavyEIeggP8eTyosjxt0jT7C3xJW/nAwAR4LVB0gmCyv7NVa++MO5PGlo="); }
    @Override public Pos position() { return new Pos(125.5, 70, -67.5, -180, 45); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Ah, a new face! Welcome to the fishing pond.", "I'm Angus, the best angler on this island.", "Cast your line and see what you can catch!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "The fish are biting today! Try your luck."); }
}
