package com.fiskmods.jdec.decoder;

import java.util.function.Consumer;
import java.util.function.Function;

public class SwitchJsonDecoder {
    public static <T> JsonDecoder<T> from(Function<String, JsonDecoder<T>> codecFunction) {
        return in -> {
            in.beginObject();
            String key = in.nextName();
            JsonDecoder<T> codec = codecFunction.apply(key);
            if (codec == null) {
                throw new IllegalArgumentException("Unknown codec type '%s'".formatted(key));
            }
            T t = codec.read(in);
            in.endObject();
            return t;
        };
    }

    public static <T> JsonDecoder<T> build(Consumer<Builder<T>> codecFunction) {
        Builder<T> builder = new Builder<>();
        codecFunction.accept(builder);
        return from(builder.build());
    }

    public static class Builder<T> {
        private Function<String, JsonDecoder<T>> function;

        public Builder<T> on(String key, JsonDecoder<T> codec) {
            if (function == null) {
                function = k -> k.equals(key) ? codec : null;
                return this;
            }
            Function<String, JsonDecoder<T>> prevFunc = function;
            function = k -> {
                JsonDecoder<T> v = prevFunc.apply(k);
                return v != null ? v : k.equals(key) ? codec : null;
            };
            return this;
        }

        private Function<String, JsonDecoder<T>> build() {
            if (function == null) {
                return k -> null;
            }
            return function;
        }
    }
}
