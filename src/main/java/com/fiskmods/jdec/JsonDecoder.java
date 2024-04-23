package com.fiskmods.jdec;

import com.fiskmods.jdec.exception.JsonDecoderException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface JsonDecoder<T> {
    T read(JsonReader in) throws IOException;

    default T readChecked(JsonReader in) throws IOException, JsonDecoderException {
        try {
            return read(in);
        }
        catch (IOException | JsonDecoderException e) {
            throw e;
        }
        catch (Exception e) {
            throw new JsonDecoderException(e, in);
        }
    }

    @SuppressWarnings("unchecked")
    default JsonDecoder<Object> asObject() {
        return (JsonDecoder<Object>) this;
    }

    default <R> JsonDecoder<R> map(Function<T, R> mappingFunction) {
        return in -> mappingFunction.apply(read(in));
    }

    default JsonDecoder<T> or(JsonToken onToken, JsonDecoder<? extends T> other) {
        return in -> in.peek() == onToken ? other.read(in) : read(in);
    }

    default JsonDecoder<T> or(Predicate<JsonToken> onToken, JsonDecoder<? extends T> other) {
        return in -> onToken.test(in.peek()) ? other.read(in) : read(in);
    }

    default JsonDecoder<T> orNull() {
        return in -> {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return read(in);
        };
    }

    default JsonDecoder<T> andThen(Consumer<T> action) {
        return in -> {
            T t = read(in);
            action.accept(t);
            return t;
        };
    }
}
