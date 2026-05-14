package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record ElizabethNPC(Instance instance) implements NpcDefinition {

    @Override
    public String id() {
        return "hub_elizabeth";
    }

    @Override
    public String name() {
        return "<light_purple>Elizabeth</light_purple>";
    }

    @Override
    public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTYyMzI3ODQwMzE4NCwKICAicHJvZmlsZUlkIiA6ICI1NjY3NWIyMjMyZjA0ZWUwODkxNzllOWM5MjA2Y2ZlOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVJbmRyYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85MDE3YjZhY2YyMmRlYjE2ODYyOTNkN2NjY2ZmZTQwOTAxYWE1YWE0YTZmYTkwZjhlMTIxNzExNGUxZDM3MzAyIgogICAgfQogIH0KfQ==",
                "aspnB8Q4ZKPzWrXjR8aDqv0URrVj4JqV0NB96AY8sUJEgqf/g20+cLp9ceJM9aXNQplXGdi7PzS99qnAtklvBrkykRKr2005eVYr6r8JEdNn35cGXlG9I1cqMrO7Dq6WtOPgKGpcArISHlKE1PehNxu6TxMB47kXxAfb+/yE0e0avTbLtZz+97XnIXU8bjwfZZTvXDZkGzgv8Cvfnh3IZTr/jxWzsk66DNY3c1SL6cy0GhAUiCAtiaJ50g4uzZ4IV5aE9sJq4e0bh5Sy0ajdDiolcAefQGt5KRaAHK4cWy7HY93HGu7I0kaF3IXlgNqn+gY1rGuDE8jyymX3GxfWflZku+0xJ0u8m4VT89ZOkAVIRfFJgedxtE1CmFpwlXouypswkFS9T/jV7RdssrCbwlM04fIXaQ4ofh+yekVHpmV4EbjO/JfXIhc7oR7qKaO9PKvwn0jFWUHvOusDqu2/LhYC+RjdxvOHW2hb2Kjt1ROsaaOUd4snjs6hE4r9sFv8bRouTWD4qal0sDf8Io2C0TJg5kIO0S+tPZrx0drSKoE2rEkP2UfxxuFzp+OuuJHElAhFuxOF8+Eq0KlqPXFSPW7WPZtmibLX5ZidjhHOMVOp9G2fNP7sjFP9qI/vEJyynjBjsJWBbcIcMVDfBAqCTOQ8rFtIoDrVR2cmzr42R10="
        );
    }

    @Override
    public Pos position() {
        return new Pos(-6.5, 79, 19.25,-90,0);
    }

    @Override
    public void onInteract(Player player, AscentNpc npc) {
        npc.speak(player, "hi ;)");
    }
}


