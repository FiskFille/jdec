package com.fiskmods.jdec.decoder;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IteratorJsonDecoder {
    public static <T, U, R> JsonDecoder<R> of(JsonDecoder<T> codec, Supplier<U> supplier, BiConsumer<U, T> accumulator,
                                       Function<U, R> finisher) {
        return in -> {
            U u = supplier.get();
            in.beginArray();
            while (in.hasNext()) {
                accumulator.accept(u, codec.read(in));
            }
            in.endArray();
            return finisher.apply(u);
        };
    }

    public static <T, R> JsonDecoder<R> of(JsonDecoder<T> codec, Supplier<R> supplier, BiConsumer<R, T> accumulator) {
        return of(codec, supplier, accumulator, Function.identity());
    }

    public static <T> JsonDecoder<Set<T>> toSet(JsonDecoder<T> codec, Supplier<Set<T>> supplier) {
        return of(codec, supplier, Set::add);
    }

    public static <T> JsonDecoder<Set<T>> toHashSet(JsonDecoder<T> codec) {
        return toSet(codec, HashSet::new);
    }
}
