package fun.ascent.skyblock.events;

import lombok.Getter;
import net.minestom.server.event.Event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
public abstract class SEvent<T extends Event> {

    private final Class<T> eventType;

    @SuppressWarnings("unchecked")
    public SEvent() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superclass;

            this.eventType = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new IllegalStateException("Classes extending MEvent must declare the generic type!");
        }
    }

    public abstract void onEvent(T event);

}
