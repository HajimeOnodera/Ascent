package fun.ascent.skyblock.data.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.ascent.skyblock.data.Datapoint;
import fun.ascent.skyblock.data.Serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatapointStringList extends Datapoint<List<String>> {
    public DatapointStringList(String key, List<String> value) {
        super(key, value, new Serializer<>() {
            private final Gson GSON = new Gson();
            private final Type TYPE = new TypeToken<List<String>>() {
            }.getType();

            @Override
            public String serialize(List<String> value) {
                return GSON.toJson(value);
            }

            @Override
            public List<String> deserialize(String json) {
                return GSON.fromJson(json, TYPE);
            }

            @Override
            public List<String> clone(List<String> value) {
                return new ArrayList<>(value);
            }
        });
    }

    @Override
    public Datapoint<List<String>> deepClone() {
        return new DatapointStringList(key, serializer.clone(value));
    }
}
