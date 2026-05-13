package fun.ascent.skyblock.data.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.ascent.skyblock.data.Datapoint;
import fun.ascent.skyblock.data.Serializer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.NetworkBuffer;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DatapointInventory extends Datapoint<List<ItemStack>> {
    public DatapointInventory(String key, List<ItemStack> value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(List<ItemStack> value) {
                if (value == null) return "[]";
                List<String> serialized = new ArrayList<>();
                for (ItemStack item : value) {
                    if (item == null || item.isAir()) {
                        serialized.add("null");
                        continue;
                    }
                    try {
                        byte[] bytes = NetworkBuffer.makeArray(buffer -> buffer.write(ItemStack.NETWORK_TYPE, item));
                        serialized.add(Base64.getEncoder().encodeToString(bytes));
                    } catch (Exception e) {
                        serialized.add("null");
                    }
                }
                return new Gson().toJson(serialized);
            }

            @Override
            public List<ItemStack> deserialize(String json) {
                List<ItemStack> items = new ArrayList<>();
                if (json == null || json.isEmpty() || json.equals("[]")) return items;

                try {
                    List<String> serialized = new Gson().fromJson(json, new TypeToken<List<String>>() {
                    }.getType());
                    for (String s : serialized) {
                        if (s == null || s.equals("null")) {
                            items.add(ItemStack.AIR);
                            continue;
                        }
                        byte[] bytes = Base64.getDecoder().decode(s);
                        NetworkBuffer buffer = NetworkBuffer.wrap(bytes, 0, bytes.length);
                        items.add(buffer.read(ItemStack.NETWORK_TYPE));
                    }
                } catch (Exception e) {
                    // Fallback to empty
                }
                return items;
            }

            @Override
            public List<ItemStack> clone(List<ItemStack> value) {
                return new ArrayList<>(value);
            }
        });
    }

    @Override
    public Datapoint<List<ItemStack>> deepClone() {
        return new DatapointInventory(key, serializer.clone(value));
    }
}
