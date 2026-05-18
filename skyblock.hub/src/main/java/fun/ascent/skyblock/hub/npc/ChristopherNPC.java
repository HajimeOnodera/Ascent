package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record ChristopherNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_christopher"; }
    @Override public String name() { return "<gold>Christopher"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NzIxNjY0Mzc4MjMsInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85ODdiODk1MmJlMmEyOWE1N2Q3ZjBlNzg4Zjc5ZmNmZGMxZmJkMjVmZmVjMDEyZmJmZWYzNGQ3YmU0NjM5YjQ5In19fQ==", "NxIaJGJuC8vk9gvaCp5knWa7V7fYO6XR7uwxQL6bjBA1ro+T2K4FZykWTXfo1nmnJKKWKxZ+LMtlUuYajXfD23rbov3Tjc2p73kKJV4CevP+cNYnvw8lmWYnXo91VhfO5AoAxcAb5lCCfwNPqcozC5JXcrMIGDpWQHpAaGYM9vdRJJXbCq/c2EGjuBeydD6i9cnDK9ar/aSstaXgXewEV8efvKfOGgeL3Y0KwO7jVV7qRpDyM+w5/AOLXFYM6031K28PSQtQMcBtaeAXjalLNfvBiJGSaX/FXdoKDQB/xeZRak1kUlYF6n6XDKgk+jXgzuvlamNQh6nNFXeMH5lkwQm9LvHRs6img6xx1ZT2AV4bB8g4lTP9H5sD6dLEwNf2AU1lhHTmiwziS/P5KwAzJL0imhGLuf9BABzv8LaWBaxrTubDuS9Y2Qdv+UmjlQ33pxKV0+U/rzME3UQuPwc+2GX/cZOdg0BGKxY9EcOvDonD4jMfBhgPVr2451+CqvtKQXPi26nZ+pLMCU2QCX0qAD9WfDCFGHdGmrjnaM+IzwdyQdD4Tqq7SMStTHaw/o4uibwUwloS7wuF7XcvNEGLnVXar7SZcejLU4NmiMFmt5482QbFANlVDApHQaw4h+c8QfXdWVYKJDtDDiG7OpzTwZZUS37fdL6SKDhafEHjBX4="); }
    @Override public Pos position() { return new Pos(-14.5, 75, -75.5, 0, 0); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Hello there! Nice day, isn't it?"); }
}
