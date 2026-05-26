package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import fun.ascent.skyblock.bazaar.BazaarRegistry;
import fun.ascent.skyblock.bazaar.ui.BazaarCategoryMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record BazaarNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_bazaar"; }
    @Override public String name() { return "<gold>Bazaar"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "eyJ0aW1lc3RhbXAiOjE1NzMyMjM2NDc4NDcsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2MyMzJlMzgyMDg5NzQyOTE1NzYxOWIwZWUwOTlmZWMwNjI4ZjYwMmZmZjEyYjY5NWRlNTRhZWYxMWQ5MjNhZDcifX19",
                "FDFrRosEo3GRN1QUX2XDIxxB9cWznwE1Nt6zoDCUL9Ya0sudiOMey3r0wL+qgKNItbDgeflDpTwlpA1JBWbfQWWVCRRQhsN6HWPAyTqFMXyy8skaR8UMgr6My8Xz6kcWIfv3g6toUe1sowoKDBXt9z3hn4j6qiARxMOb1nSSy1Cp19di4rYOIFa7Ibu5DNNKAo0bafPYA3Mexy1DYpkJ9FFO6wyW/3U30jPCTnbysZp6XJN0scnXQcoLeBw5wy0V/NI/C7TNJKhr7YWlZKqVKW8r1kyrGgkTvC1u1AWBj3PFV3KuIlhX+G7VUD8iCvz8hvwJVRJBPlsMT6CQ5sP0eCHs38YoN9kiHtO+gHElHzp0JctQXX/7eYXV1FCMGJ8ov+u9f9V/Xu9HEdjCxwdjrRS7I/FSy5/GuBOHY+G2YIVKzMsCTkOM+F52WWF+O6/mGTo6NAdgvJb0Wvvif6/edHbUucOp2OtH67XGD61p/ktg/DmHNoXvjDCD0ld1HLO24fZrdm/cuC85/VYrEb6m9NvFZZVIoLbjbwSFuZD7AyGvHiFVdBWa9Ps3IpxiKi8lroyW8D4VLEQteN/BoB2DHTvu+jEMFJK4W+X7MG0pPAQz5F+1JAaWufR6ZH6Jrx/r4+1gjZlWzV6tmv4OXQHtDnaY0HCRvB+srNfQ/c1UZt8="); }
    @Override public Pos position() { return new Pos(-33.5, 73, -22.5, -180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome to the Bazaar!", "Here you can instantly buy and sell resources at market prices.", "Supply and demand drive the prices, so keep an eye on trends!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) {
        //TODO: Implement Level Check
        BazaarCategoryMenu.openMenu((SkyblockPlayer) player, BazaarRegistry.bazaarItemList.getFarming());
//        npc.speak(player, "The market is open! Buy or sell resources instantly.");
    }
}
