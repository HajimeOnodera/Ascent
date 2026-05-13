package fun.ascent.skyblock.data.impl;

import com.google.gson.reflect.TypeToken;
import fun.ascent.skyblock.data.Datapoint;
import fun.ascent.skyblock.data.Serializer;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DatapointMapDouble extends Datapoint<Map<String, Double>> {
    public DatapointMapDouble(String key, Map<String, Double> value) {
        super(key, value, new Serializer<>() {
            private final Gson GSON = new Gson();
            private final Type TYPE = new TypeToken<Map<String, Double>>() {
            }.getType();

            @Override
            public String serialize(Map<String, Double> value) {
                return GSON.toJson(value);
            }

            @Override
            public Map<String, Double> deserialize(String json) {
                return GSON.fromJson(json, TYPE);
            }

            @Override
            public Map<String, Double> clone(Map<String, Double> value) {
                return new HashMap<>(value);
            }
        });
    }

    @Override
    public Datapoint<Map<String, Double>> deepClone() {
        return new DatapointMapDouble(key, serializer.clone(value));
    }
}
