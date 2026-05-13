package fun.ascent.skyblock.data.impl;

import fun.ascent.skyblock.data.Datapoint;

public class DatapointLong extends Datapoint<Long> {
    public DatapointLong(String key, Long value) {
        super(key, value, new GsonSerializer<>(Long.class));
    }

    @Override
    public Datapoint<Long> deepClone() {
        return new DatapointLong(key, serializer.clone(value));
    }
}
