package fun.ascent.skyblock.data.impl;

import fun.ascent.skyblock.data.Datapoint;

public class DatapointBoolean extends Datapoint<Boolean> {
    public DatapointBoolean(String key, Boolean value) {
        super(key, value, new GsonSerializer<>(Boolean.class));
    }

    @Override
    public Datapoint<Boolean> deepClone() {
        return new DatapointBoolean(key, serializer.clone(value));
    }
}
