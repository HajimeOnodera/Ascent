package fun.ascent.discord.data;

public sealed interface Outcome<T> permits Outcome.Ok, Outcome.Fail {

    record Ok<T>(T value) implements Outcome<T> {}
    record Fail<T>(String reason) implements Outcome<T> {}

    static <T> Ok<T> ok(T value) { return new Ok<>(value); }
    static <T> Fail<T> fail(String reason) { return new Fail<>(reason); }

    default boolean ok() { return this instanceof Ok<T>; }

    @SuppressWarnings("unchecked")
    default T unwrap() {
        if (this instanceof Ok<T> o) return o.value();
        throw new IllegalStateException(((Fail<T>) this).reason());
    }
}