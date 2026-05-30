package fun.ascent.skyblock.minion.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.ascent.skyblock.minion.base.SkyblockMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.NetworkBuffer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class MinionPersistence {
    private static final Gson GSON = new Gson();

    public static Document serialize(SkyblockMinion minion) {
        Document doc = new Document();
        doc.put("type", minion.getType().name());
        doc.put("tier", minion.getTier());
        doc.put("owner", minion.getOwnerUuid().toString());
        
        // Position
        Pos pos = minion.getPosition();
        doc.put("x", pos.x());
        doc.put("y", pos.y());
        doc.put("z", pos.z());
        doc.put("yaw", pos.yaw());
        doc.put("pitch", pos.pitch());

        // Storage
        List<ItemStack> items = minion.getStoredStacks();
        List<String> serializedItems = new ArrayList<>();
        for (ItemStack item : items) {
            if (item == null || item.isAir()) {
                serializedItems.add("null");
                continue;
            }
            try {
                byte[] bytes = NetworkBuffer.makeArray(buffer -> buffer.write(ItemStack.NETWORK_TYPE, item));
                serializedItems.add(Base64.getEncoder().encodeToString(bytes));
            } catch (Exception e) {
                serializedItems.add("null");
            }
        }
        doc.put("storage", GSON.toJson(serializedItems));
        doc.put("lastSavedAt", System.currentTimeMillis());
        doc.put("totalGenerated", minion.getTotalGenerated());
        
        return doc;
    }

    public static SkyblockMinion deserialize(Document doc, Instance instance) {
        MinionType type = MinionType.valueOf(doc.getString("type"));
        int tier = doc.getInteger("tier");
        UUID owner = UUID.fromString(doc.getString("owner"));
        
        Pos pos = new Pos(
            doc.getDouble("x"),
            doc.getDouble("y"),
            doc.getDouble("z"),
            doc.get("yaw") instanceof Double ? ((Double) doc.get("yaw")).floatValue() : ((Integer) doc.get("yaw")).floatValue(),
            doc.get("pitch") instanceof Double ? ((Double) doc.get("pitch")).floatValue() : ((Integer) doc.get("pitch")).floatValue()
        );

        SkyblockMinion minion = MinionFactory.create(owner, type, tier, instance, pos);
        
        // Load storage
        String json = doc.getString("storage");
        if (json != null && !json.isEmpty()) {
            List<String> serialized = GSON.fromJson(json, new TypeToken<List<String>>() {}.getType());
            List<ItemStack> items = new ArrayList<>();
            for (String s : serialized) {
                if (s == null || s.equals("null")) {
                    items.add(ItemStack.AIR);
                    continue;
                }
                try {
                    byte[] bytes = Base64.getDecoder().decode(s);
                    NetworkBuffer buffer = NetworkBuffer.wrap(bytes, 0, bytes.length);
                    items.add(buffer.read(ItemStack.NETWORK_TYPE));
                } catch (Exception e) {
                    items.add(ItemStack.AIR);
                }
            }
            minion.getStorage().addAll(items, minion.getData().getStorageSlots());
        }

        // Restore totalGenerated
        if (doc.containsKey("totalGenerated")) {
            minion.setTotalGenerated(doc.getLong("totalGenerated"));
        }

        // Apply offline production
        if (doc.containsKey("lastSavedAt")) {
            long lastSavedAt = doc.getLong("lastSavedAt");
            minion.applyOfflineProduction(lastSavedAt);
        }
        
        return minion;
    }
}
