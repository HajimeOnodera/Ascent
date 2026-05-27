package fun.ascent.skyblock.world.region;

import fun.ascent.skyblock.data.SkyblockDataHandler;
import fun.ascent.skyblock.data.impl.DatapointStringList;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.actionbar.ActionBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;

import java.time.Duration;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class RegionListener {
    private static final Tag<String> LAST_REGION = Tag.String("last_region");

    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerMoveEvent.class, event -> {
            if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;

            Region current = RegionManager.getRegion(player.getInstance(), event.getNewPosition());
            String lastRegionId = player.getTag(LAST_REGION);
            String currentId = current != null ? current.getId() : "none";

            if (!currentId.equals(lastRegionId)) {
                RegionType fromType = null;
                if (lastRegionId != null && !lastRegionId.equals("none")) {
                    try {
                        Region lastRegion = RegionManager.getRegion(lastRegionId);
                        if (lastRegion != null) fromType = lastRegion.getType();
                    } catch (Exception ignored) {}
                }
                
                player.setTag(LAST_REGION, currentId);
                if (current != null) {
                    ActionBar.of(player.getUuid()).addReplacement(
                            ActionBar.Section.DEFENSE,
                            "<gray> ⏣ " + current.getType().getColor() + current.getType().getName(),
                            20, 10);
                    checkNewZoneDiscovery(player, current.getType());
                    
                    net.minestom.server.MinecraftServer.getGlobalEventHandler().call(
                        new fun.ascent.skyblock.events.PlayerRegionChangeEvent(player, fromType, current.getType())
                    );
                }
            }
        });
    }

    private static void checkNewZoneDiscovery(SkyblockPlayer player, RegionType region) {
        if (player.getActiveProfileData() == null) return;

        DatapointStringList visitedRegions = player.getActiveProfileData().getDataHandler().get(
                SkyblockDataHandler.Data.VISITED_REGIONS, DatapointStringList.class);
        List<String> visited = visitedRegions.getValue();

        if (visited.contains(region.name())) return;

        visited.add(region.name());
        visitedRegions.setValue(visited);

        if (region == RegionType.PRIVATE_ISLAND) {
            displayIslandWelcome(player);
        } else {
            String[] features = getFeaturesForRegion(region);
            displayNewZone(player, region, features);
        }
    }

    private static void displayIslandWelcome(SkyblockPlayer player) {
        String username = player.getUsername();
        int usernameLength = username.length();
        int greetingPadding = Math.max(0, (56 - (21 + usernameLength)) / 2);
        String greetingSpaces = " ".repeat(greetingPadding);

        player.sendMessage(text("<yellow><bold>" + "■".repeat(60)));
        player.sendMessage(Component.empty());
        player.sendMessage(text(greetingSpaces + "<white><bold>Welcome to SkyBlock, <green><!bold>" + username + "<white><bold>!"));
        player.sendMessage(Component.empty());
        player.sendMessage(text("     <yellow>This is your island! The SkyBlock universe has"));
        player.sendMessage(text("      <yellow>many lands to discover, secrets to uncover,"));
        player.sendMessage(text("      <yellow>and people to meet. Collect resources, craft"));
        player.sendMessage(text("       <yellow>items, and complete objectives to advance"));
        player.sendMessage(text("               <yellow>your way through SkyBlock."));
        player.sendMessage(Component.empty());
        player.sendMessage(text("                       <white>Have fun!"));
        player.sendMessage(text("<yellow><bold>" + "■".repeat(60)));
        player.sendMessage(Component.empty());
    }

    private static void displayNewZone(SkyblockPlayer player, RegionType region, String... features) {
        player.sendMessage(Component.empty());
        player.sendMessage(text("<gold><bold> NEW AREA DISCOVERED!"));
        player.sendMessage(text("<gray>  ⏣ " + region.getColor() + region.getName()));
        player.sendMessage(Component.empty());
        for (String feature : features) {
            player.sendMessage(text("<gray>   ⬛ <white>" + feature));
        }
        player.sendMessage(Component.empty());

        player.playSound(Sound.sound()
                .type(SoundEvent.ENTITY_PLAYER_LEVELUP)
                .volume(1f)
                .pitch(1f)
                .build());

        player.showTitle(Title.title(
                text(region.getColor() + region.getName()),
                text("<gold><bold>NEW AREA DISCOVERED!"),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
        ));
    }

    private static String[] getFeaturesForRegion(RegionType region) {
        return switch (region) {
            case VILLAGE -> new String[]{
                    "Purchase items at the Market.",
                    "Visit the Auction House.",
                    "Manage your Coins in the Bank.",
                    "Enchant items at the Library."};
            case AUCTION_HOUSE -> new String[]{
                    "Auction off your special items.",
                    "Bid on other player's items."};
            case BANK -> new String[]{
                    "Talk to the Banker.",
                    "Store your coins to keep them safe.",
                    "Earn interest on your coins."};
            case BAZAAR -> new String[]{
                    "Buy and sell materials in bulk in the Bazaar."};
            case FARM -> new String[]{
                    "Learn about the Farming Skill at <yellow>Farmer Rigby<white>.",
                    "Gather <yellow>Wheat<white>.",
                    "Learn about Minion Upgrades at <yellow>Arthur<white>.",
                    "Travel to <aqua>The Barn<white>."};
            case COAL_MINE -> new String[]{
                    "Mine <dark_gray>Coal<white>.",
                    "Travel to the Gold Mine."};
            case FOREST -> new String[]{
                    "Visit the <green>Lumber Jack<white>.",
                    "Chop down trees.",
                    "Travel to the <green>Birch Park<white>."};
            case GRAVEYARD -> new String[]{
                    "Fight Zombies.",
                    "Travel to the Spider's Den.",
                    "Talk to <gray>Pat<white>.",
                    "Investigate the Catacombs."};
            case CRYPTS -> new String[]{
                    "Explore the Crypts.",
                    "Watch out for the Zombies that lurk here!"};
            case MOUNTAIN -> new String[]{
                    "Climb to the top!"};
            case WILDERNESS -> new String[]{
                    "Fish in the pond.",
                    "Visit <light_purple>Tia the Fairy<white> at the <light_purple>Fairy Pond<white>.",
                    "Discover hidden secrets."};
            case COMBAT_SETTLEMENT -> new String[]{
                    "Buy Combat Tools from the <red>Weaponsmith<white>.",
                    "Learn about <dark_green>Enchanting<white> at the <aqua>Library<white>.",
                    "Talk to <red>Maxwell<white> to learn about <gold>Magical Power<white>."};
            case COMMUNITY_CENTER -> new String[]{
                    "Participate in elections and community events.",
                    "Talk to <gold>Clerk Seraphine<white>."};
            case FISHING_OUTPOST -> new String[]{
                    "Buy fishing essentials from the <aqua>Fishing Merchant<white>.",
                    "Talk to <blue>Fisherman Gerald<white>.",
                    "Learn about <green>Fishing<white> stats from <blue>Gwynnie<white>."};
            case FLOWER_HOUSE -> new String[]{
                    "Talk to Marco about <green>Spray Cans<white>.",
                    "Gather flowers."};
            case FORAGING_CAMP -> new String[]{
                    "Unlock the <dark_green>Foraging Skill<white> at <dark_green>Lumber Jack<white>.",
                    "Purchase Foraging Tools from the <dark_green>Lumber Merchant<white>.",
                    "Travel to the <green>Birch Park<white>."};
            case LIBRARY -> new String[]{
                    "Enchant items at the Enchanting Tables.",
                    "Purchase Enchanted Books."};
            case MINING_DISTRICT -> new String[]{
                    "Purchase Mining Tools from the <gold>Mining Merchant<white>.",
                    "Learn about <aqua>Reforges<white> at the <gold>Blacksmith<white>.",
                    "Travel to the <dark_gray>Coal Mine<white>."};
            case MUSEUM -> new String[]{
                    "Donate items to the Museum.",
                    "Talk to the <aqua>Curator<white>."};
            case RUINS -> new String[]{
                    "Explore the ancient ruins.",
                    "Watch out for the guard dogs!"};
            case SHENS_AUCTION -> new String[]{
                    "Talk to Damia.",
                    "Bid on high-end items."};
            case THAUMATURGIST -> new String[]{
                    "Talk to <dark_purple>Maxwell<white> about <gold>Magical Power<white>."};
            case ARCHERY_RANGE -> new String[]{
                    "Talk to Jax to forge special arrows!"};
            case DEEP_CAVERNS -> new String[]{
                    "Talk to the Lift Operator.",
                    "Mine valuable ores.",
                    "Watch out for mobs!"};
            case GUNPOWDER_MINES -> new String[]{
                    "Talk to the Lift Operator.",
                    "Explore the caverns.",
                    "Mine Coal, Iron ore, and Gold ore."};
            case LAPIS_QUARRY -> new String[]{
                    "Access to Lapis Lazuli ore.",
                    "Watch out for the zombies!"};
            case GOLD_MINE -> new String[]{
                    "Talk to the Lazy Miner.",
                    "Mine for gold, iron, and coal.",
                    "Visit the Blacksmith."};
            case BIRCH_PARK -> new String[]{
                    "Chop down Birch logs."};
            case SPRUCE_WOODS -> new String[]{
                    "Chop down Spruce logs."};
            case DARK_THICKET -> new String[]{
                    "Chop down Dark Oak Logs."};
            case SAVANNA_WOODLAND -> new String[]{
                    "Chop down Acacia logs."};
            case THE_END -> new String[]{
                    "Talk to the Pearl Dealer.",
                    "Kill Endermen.",
                    "Fight Dragons!"};
            default -> new String[]{};
        };
    }
}
