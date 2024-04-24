package com.fiskmods.jdec.decoder;

import java.util.function.Function;

public class EnumJsonDecoder {
    public static <T extends Enum<T>> JsonDecoder<T> of(Class<T> type, Function<T, String> keyFunction) {
        T[] values = type.getEnumConstants();
        return in -> {
            String key = in.nextString();
            for (T t : values) {
                if (key.equals(keyFunction.apply(t))) {
                    return t;
                }
            }
            throw new IllegalArgumentException("Unknown value '%s'".formatted(key));
        };
    }

    public static <T extends Enum<T>> JsonDecoder<T> of(Class<T> type) {
        return of(type, Enum::name);
    }
}
