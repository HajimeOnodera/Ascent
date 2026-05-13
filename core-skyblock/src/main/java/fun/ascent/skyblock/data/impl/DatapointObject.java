package fun.ascent.skyblock.data.impl;

import fun.ascent.skyblock.data.Datapoint;

public class DatapointObject<T> extends Datapoint<T> {
    private final Class<T> clazz;

    public DatapointObject(String key, T value, Class<T> clazz) {
        super(key, value, new GsonSerializer<>(clazz));
        this.clazz = clazz;
    }

    @Override
    public Datapoint<T> deepClone() {
        return new DatapointObject<>(key, serializer.clone(value), clazz);
    }
}
