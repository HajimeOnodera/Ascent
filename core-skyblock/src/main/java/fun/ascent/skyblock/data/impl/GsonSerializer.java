package fun.ascent.skyblock.data.impl;

import com.google.gson.Gson;
import fun.ascent.skyblock.data.Serializer;

public class GsonSerializer<T> implements Serializer<T> {
    private static final Gson GSON = new Gson();
    private final Class<T> clazz;

    public GsonSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String serialize(T value) {
        return GSON.toJson(value);
    }

    @Override
    public T deserialize(String json) {
        return GSON.fromJson(json, clazz);
    }

    @Override
    public T clone(T value) {
        return GSON.fromJson(GSON.toJson(value), clazz);
    }
}
