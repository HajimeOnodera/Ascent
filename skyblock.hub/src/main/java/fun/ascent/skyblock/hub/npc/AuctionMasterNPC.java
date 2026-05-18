package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record AuctionMasterNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_auction_master"; }
    @Override public String name() { return "<gold>Auction Master"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTU5NzMwNTI3MzM5MiwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE5NzFkNzk2ZGM0ZGM5YWYzOGNjOTcwNjc5NjNkZTRlZDJiZjk5OTIyNDI1NzQxNmRmYjg0MTQ4OGZkOWUwZCIKICAgIH0KICB9Cn0=",
                "IHbiM8PpwXBFKB/x6Ue2msaxAg5uuAn7/4U8D1WIWFOy6vlTz7//aBAunwOpIAHwjI6wS+wMP+awFfcAQtL3CNnaQ6WWaUhPi+Vm2yfNDl7xOXxLKYy/soIBlPEHNteyaV7KEa22zG0a8H7cZ8UKBAdNvzWSMGeZmabBdDpboQAn3kuznmaqJh1Kij6HOvDo4fR5h87ihHy76+ljzGi62vl8ejKi37lwu2pOV+NmhEY37KSZbAtIN4s/UiYCqrwqJ8yP3lMMO7+iIjk8uyT5DJVgoc23bsw+sdDNJzNZ9OZNLvMhy/QvdE4UldIxY6Ikw2ZjP6k1Wb+oBgGDW25bAusvUKf2liPgvJtbcS2TpSanegSzreLfR9XThY9L1SHjja9CQbGeoRD4kmS6Vqi/oFZKDKhuGkWHgyJTcWm2+BGFrC183+ZfMt9JTu4g7GJfSJwL/5PrFpzBm2rbLNMmZP/zq5o0YZUSD0izdffVFoyaQ58oueE3DvZ1rnLuiuhBxGd+Ptc1xKM/sSmcdXIeAn+POCJvK3zb3I7adRCFAy432LzqRnLnLGzuufqvuyn506DdEOEgRaq4yc0VDR1IgmMAgdO/zE/pNdoR/p8LrVFRO5WQmxcXfCNwP888YbRt6t5a7/ExdSN39VYhtovnkPfO+SEsAVofw3wfBO0/FvE="); }
    @Override public Pos position() { return new Pos(-39.5, 73, -12.5, -90, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome to the Auction House!", "Here you can buy and sell rare items with other players.", "I oversee all transactions to make sure everything runs smoothly.", "Feel free to browse the listings!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking to make a deal? Check out the latest auctions!"); }
}
