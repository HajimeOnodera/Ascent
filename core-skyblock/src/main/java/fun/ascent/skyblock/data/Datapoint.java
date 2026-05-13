package fun.ascent.skyblock.data;

import lombok.Getter;
import lombok.Setter;

public abstract class Datapoint<T> {
    @Getter protected final String key;
    @Getter @Setter protected T value;
    protected final Serializer<T> serializer;

    protected Datapoint(String key, T value, Serializer<T> serializer) {
        this.key = key;
        this.value = value;
        this.serializer = serializer;
    }

    public String serialize() {
        return serializer.serialize(value);
    }

    public void deserialize(String json) {
        this.value = serializer.deserialize(json);
    }

    public abstract Datapoint<T> deepClone();
}
