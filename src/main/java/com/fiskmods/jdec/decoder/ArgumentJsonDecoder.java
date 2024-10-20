package com.fiskmods.jdec.decoder;

import com.fiskmods.jdec.tag.JsonTag;
import com.google.gson.stream.JsonToken;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface ArgumentJsonDecoder<T, R> {
    JsonDecoder<R> with(Arg<? extends T> argument);

    default JsonDecoder<R> with(T argument) {
        return with(() -> argument);
    }

    default <S> ArgumentJsonDecoder<T, S> map(Function<R, S> mappingFunction) {
        return arg -> with(arg).map(mappingFunction);
    }

    default ArgumentJsonDecoder<T, R> or(JsonToken onToken, JsonDecoder<R> other) {
        return arg -> in -> in.peek() == onToken ? other.read(in) : with(arg).read(in);
    }

    default ArgumentJsonDecoder<T, R> orArg(JsonToken onToken, ArgumentJsonDecoder<T, R> other) {
        return arg -> in -> in.peek() == onToken ? other.with(arg).read(in) : with(arg).read(in);
    }

    default ArgumentJsonDecoder<T, R> or(Predicate<JsonToken> onToken, JsonDecoder<R> other) {
        return arg -> in -> onToken.test(in.peek()) ? other.read(in) : with(arg).read(in);
    }

    default ArgumentJsonDecoder<T, R> orArg(Predicate<JsonToken> onToken, ArgumentJsonDecoder<T, R> other) {
        return arg -> in -> onToken.test(in.peek()) ? other.with(arg).read(in) : with(arg).read(in);
    }

    @FunctionalInterface
    interface Arg<T> {
        T resolve();

        default <R> Arg<R> map(Function<T, R> mappingFunction) {
            return () -> mappingFunction.apply(resolve());
        }
    }

    static <T, R> ArgumentJsonDecoder<T, R> fromTag(Function<JsonTag<T>, JsonDecoder<R>> func) {
        ArgumentJsonTag<T> tag = new ArgumentJsonTag<>();
        JsonDecoder<R> codec = func.apply(tag);
        return arg -> in -> {
            synchronized (tag) {
                tag.value = arg.resolve();
                return codec.read(in);
            }
        };
    }

    static <T, R> ArgumentJsonDecoder<T, R> fromValue(Function<Arg<T>, JsonDecoder<R>> func) {
        return fromTag(tag -> func.apply(() -> ((ArgumentJsonTag<T>) tag).value));
    }

    class ArgumentJsonTag<T> implements JsonTag<T> {
        private T value;

        @Override
        public JsonTag.Entry<T> createEntry() {
            return () -> value;
        }
    }
}
