package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record ClerkSeraphineNPC(Instance instance) implements NpcDefinition {
    @Override
    public String id() {
        return "hub_clerk_seraphine";
    }

    @Override
    public String name() {
        return "<white>Clerk Seraphine";
    }

    @Override
    public NpcSkin skin() {
        return new NpcSkin(
                "eyJ0aW1lc3RhbXAiOjE1NTk2NzYyNzYyNzYsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NkNjU0NTMxODE2ZWM3MjAwYTZhNDYxZDdhMDNjMmRhZGYzMWY0NDlhNTkxYzg1ZjNiMzFjYjJhODNkZDczNjYifX19",
                "nlSTknzGOCRA9nT4N8dQce9+2sRkCmpPBN/sKJqIYZxjbF+Uuejbuo32zSFqj1rdB720EA7jVplcZxMP6i9XscUAF83GWwCHX2N75AT1NRW3PeBg0gtrM/qQxKgaH4zOEquGZZ9l8NyV5k2rxDVZRG8YusxsP45WSUV4bnJyLpWeVXO/ZY5luX841BpseLQVTEHjkhYVbL32tauHM6DoB05VKh+5r8utv/OsV8oXQf37+5DMD530ftkriMt+QIlEcVogCk6BR9fvP7tA5Bxvuc6uDbVSkMZaXq62mHVbdRBmnh/nm2hWjrfwNrCPbGi3on5YY5qpnjY9t65Tjw4JsRqmpQXjrscIcfSir8sylisjZfi+kyrjoi4eGVGFVwS2HO1wpX303h3l/z2q9yCEwCHEjnnMx5CI3H8egTTQSLZC8pz60ZaFnyf+ZDwl9gs79wfYGDaqwi7rq7LFYeSrZctABb4oVs/DRuKs+4l8e0LOu90nHnMh7aseAT9YfSip+uTvGaybcpnG8kLcv4sm7J8n3EnhTtcU1ajS/dxZs9i/+p1bQ+wq+k1vwgAOXFLh02ISqUHVVYF9OlyZRiS+/84bPA6WS9SWz+ur6C49fCH44lCPaxxu1UJeHZXI2b1Luh8t7jtKj/gvtzj2GWHADLpcdw2M0IwrRH7ibew8OgE=");
    }

    @Override
    public Pos position() {
        return new Pos(-1.5, 79, 10.5, -166, 0);
    }
    @Override
    public void onInteract(Player player, AscentNpc npc) {
        npc.speak(player,
                "Welcome to the <aqua>Community Center<white>!",
                "Contribute to community projects, upgrade your account, and more by talking to <light_purple>Elizabeth<white>!",
                "You can also vote in the <aqua>mayor elections <white>by heading through the warp to the left!"
        );
    }
}
