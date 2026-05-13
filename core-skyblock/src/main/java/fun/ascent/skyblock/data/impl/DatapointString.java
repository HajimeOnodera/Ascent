package fun.ascent.skyblock.data.impl;

import fun.ascent.skyblock.data.Datapoint;

public class DatapointString extends Datapoint<String> {
    public DatapointString(String key, String value) {
        super(key, value, new GsonSerializer<>(String.class));
    }

    @Override
    public Datapoint<String> deepClone() {
        return new DatapointString(key, serializer.clone(value));
    }
}
