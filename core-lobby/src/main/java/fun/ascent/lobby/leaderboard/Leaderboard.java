package fun.ascent.lobby.leaderboard;

import fun.ascent.common.hologram.Hologram;
import fun.ascent.common.user.Rank;
import fun.ascent.database.PlayerRepository;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;
import org.bson.Document;

import java.util.*;

import static fun.ascent.common.StringUtility.text;

public class Leaderboard {

    @Getter
    private final String id;
    private final Pos position;
    private final Instance instance;
    private final List<Category> categories;
    private int currentCategoryIndex = 0;
    private final boolean spawnSelectors;
    
    private final Hologram hologram;
    private final Pos selectorPosition;
    private final List<Entity> categoryClickers = new ArrayList<>();
    private final List<Hologram> categoryHolograms = new ArrayList<>();

    public Leaderboard(String id, Instance instance, Pos position, Pos selectorPosition, List<Category> categories, boolean spawnSelectors) {
        this.id = id;
        this.instance = instance;
        this.position = position;
        this.selectorPosition = selectorPosition;
        this.categories = categories;
        this.spawnSelectors = spawnSelectors;
        this.hologram = new Hologram(instance, position);
    }

    public void spawn() {
        update();
        if (spawnSelectors) {
            spawnCategorySelectors();
        }
        
        // Update periodically
        MinecraftServer.getSchedulerManager().buildTask(this::update)
                .repeat(TaskSchedule.tick(5 * 60 * 20)) // 5 minutes in ticks
                .schedule();
    }

    public void update() {
        Category category = categories.get(currentCategoryIndex);
        List<Document> topPlayers = PlayerRepository.getTopPlayers(category.field(), 10);
        
        List<String> lines = new ArrayList<>();
        lines.add("<aqua>Top Ascent Players</aqua>");
        lines.add(category.color() + category.name() + "</" + category.color().replace("<", "").replace(">", "") + ">");
        lines.add("");

        int rank = 1;
        for (Document doc : topPlayers) {
            Document profile = doc.get("profile", Document.class);
            if (profile == null) continue;
            
            String name = profile.getString("name");
            if (name == null) name = "Unknown";
            
            Rank playerRank = Rank.valueOf(profile.getString("rank") == null ? "DEFAULT" : profile.getString("rank"));
            
            Object valueObj = getNestedValue(doc, category.field());
            String formattedValue = category.formatter().format(valueObj);
            
            lines.add("<yellow>" + rank + ". " + playerRank.getPrefix() + name + " <gray>- " + category.color() + formattedValue);
            rank++;
        }
        
        hologram.setLines(lines.toArray(new String[0]));
    }

    private Object getNestedValue(Document doc, String field) {
        String[] parts = field.split("\\.");
        Object current = doc;
        for (String part : parts) {
            if (current instanceof Document d) {
                current = d.get(part);
            } else {
                return null;
            }
        }
        return current;
    }

    private void spawnCategorySelectors() {
        Pos selectorBase;
        if (selectorPosition != null) {
            selectorBase = selectorPosition;
        } else {
            // Calculate a position to the "left" of the leaderboard based on its yaw
            double radians = Math.toRadians(position.yaw());
            double offsetX = -Math.cos(radians) * 4;
            double offsetZ = Math.sin(radians) * 4;
            selectorBase = position.add(offsetX, 0, offsetZ);
        }
        
        Hologram title = new Hologram(instance, selectorBase.add(0, 0.5, 0));
        title.setLines("<aqua><bold>Click to toggle!");
        
        double yOffset = 0;
        for (Category cat : categories) {
            Hologram catHolo = new Hologram(instance, selectorBase.add(0, yOffset, 0));
            catHolo.setLines(cat.name());
            categoryHolograms.add(catHolo);

            // Create a clickable entity
            Entity clicker = new Entity(EntityType.ARMOR_STAND);
            ArmorStandMeta meta = (ArmorStandMeta) clicker.getEntityMeta();
            meta.setInvisible(true);
            meta.setSmall(true); // Small armor stand has a bounding box but is small
            meta.setMarker(false); // Must not be a marker to be clickable

            clicker.setNoGravity(true);
            clicker.setInstance(instance, selectorBase.add(0, yOffset - 1.2, 0)); // Adjust Y so it aligns with text
            categoryClickers.add(clicker);

            yOffset -= 0.4;
        }
    }

    public void handleInteract(Player player, Entity target, List<Leaderboard> linkedLeaderboards) {
        for (int i = 0; i < categoryClickers.size(); i++) {
            if (categoryClickers.get(i).getUuid().equals(target.getUuid())) {
                this.setCategoryIndex(i);
                if (linkedLeaderboards != null) {
                    for (Leaderboard lb : linkedLeaderboards) {
                        if (lb != this) lb.setCategoryIndex(i);
                    }
                }
                player.sendMessage(text("<green>Leaderboards switched to <white>" + categories.get(i).name() + "<green>!"));
                return;
            }
        }
    }

    public void setCategoryIndex(int index) {
        this.currentCategoryIndex = index;
        update();
    }

    public interface ValueFormatter {
        String format(Object value);
    }

    public record Category(String name, String field, String color, ValueFormatter formatter) {}
}
