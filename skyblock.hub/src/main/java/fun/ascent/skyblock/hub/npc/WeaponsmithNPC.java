package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.shop.ShopMenu;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record WeaponsmithNPC(Instance instance) implements NpcDefinition {
    @Override 
    public String id() { 
        return "hub_weaponsmith"; 
    }

    @Override 
    public String name() { 
        return "Weaponsmith"; 
    }

    @Override 
    public NpcSkin skin() { 
        return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NTA2Nzg0NTQ3MTYsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FmM2Y3YTY5YzlhN2FkOTVlOTBmMjEyZGEzMDQ5OTBjOGJlMzZlOTMxOTdkYjg3NGMyYjBmZTA4MTA2ZTMyM2IifX19", 
        "Sw5WHUwuYX2OxF/U1BHPpFdAJzwzzyzZ78bxQkdCQL1Jyd9RsIiQWKJSeTHsnhScpHxXEBtWKQ71YOci6HzwLiYZOh/M6KJ3kY+RtlRdh8EUEVj1BCoo8xvwAt28Piiqke3uDs9dWEvVBzs1PR9qxvMaarw3DT8sTgxIBU/xMmt41uDiCOgn6M/rL3waFsnPdN+v2tTHsl1aNz+hMLn4NuIbBprE90X0tsT/qlQzCBPHuwUV8elGb9xAfjJ0eRSlH1jxcfEJdb+YsPKwfqQ6btwkCOK2hdfm9/S6/Fd2KJsaDhH5twfykFyD1PEn4sCAlAisibKiOADQcg6lYN7d2eE95RVYhlqHTsb6g0aBUSk8Gj8OOABbBB0sEhGPmzTKJOAoyu9ZWgfzAty/ipGooyL/gWlGByTmWnmXf0ek6TUtkPpmLNd3Ik364+GDI+C8H14Dltc7axQXh0GsFmepUL2t/fz0fDtZlqfhwz3ei7q7fJ8S20l2O4y/GKDCItqMTkY80rOEQuysln13pV32z/8oZwl7a1rGSvSjLQwC+cpbhA8tATTVu/ovUd5Aev+PUb5vqrh4DmF4Br5cRxLCDHj6Q2CG5glXVTULhbmMLQE0YLVFvnik2xGNnlKIJ9GEdsat+PBHR7ToHasGr4JiS7n2uQykxTntgpqB9QNS4fI="); 
    }

    @Override 
    public Pos position() { 
        return new Pos(-50.5, 69, -85.5, 45, 0); 
    }

    @Override 
    public String[] firstInteractionMessages() { 
        return new String[]{
            "You'll need some strong weapons to survive out in the wild! Lucky for you, I've got some!", 
            "Click me again to open the Weaponsmith Shop!"
        }; 
    }

    @Override 
    public void onFirstInteract(Player player, AscentNpc npc) { 
        npc.speak(player, firstInteractionMessages()); 
    }

    @Override 
    public void onInteract(Player player, AscentNpc npc) { 
        ShopMenu.open((SkyblockPlayer) player, "weaponsmith_shop");
    }
}
