package com.fiskmods.jdec.decoder;

import com.fiskmods.jdec.tag.JsonTag;
import com.google.gson.stream.JsonToken;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface FunctionJsonDecoder<T, R> {
    JsonDecoder<R> with(Arg<? extends T> argument);

    default JsonDecoder<R> with(T argument) {
        return with(() -> argument);
    }

    default <S> FunctionJsonDecoder<T, S> map(Function<R, S> mappingFunction) {
        return arg -> with(arg).map(mappingFunction);
    }

    default FunctionJsonDecoder<T, R> or(JsonToken onToken, JsonDecoder<? extends R> other) {
        return arg -> in -> in.peek() == onToken ? other.read(in) : with(arg).read(in);
    }

    default FunctionJsonDecoder<T, R> orArg(JsonToken onToken, FunctionJsonDecoder<? super T, ? extends R> other) {
        return arg -> in -> in.peek() == onToken ? other.with(arg).read(in) : with(arg).read(in);
    }

    default FunctionJsonDecoder<T, R> or(Predicate<JsonToken> onToken, JsonDecoder<? extends R> other) {
        return arg -> in -> onToken.test(in.peek()) ? other.read(in) : with(arg).read(in);
    }

    default FunctionJsonDecoder<T, R> orArg(Predicate<JsonToken> onToken, FunctionJsonDecoder<? super T, ? extends R> other) {
        return arg -> in -> onToken.test(in.peek()) ? other.with(arg).read(in) : with(arg).read(in);
    }

    @FunctionalInterface
    interface Arg<T> {
        T resolve();

        default <R> Arg<R> map(Function<? super T, ? extends R> mappingFunction) {
            return () -> mappingFunction.apply(resolve());
        }
    }

    static <T, R> FunctionJsonDecoder<T, R> from(Function<ArgumentJsonTag<T>, JsonDecoder<R>> func) {
        ArgumentJsonTag<T> tag = new ArgumentJsonTag<>();
        JsonDecoder<R> codec = func.apply(tag);
        return arg -> in -> {
            synchronized (tag) {
                tag.stack.push(arg.resolve());
                R res = codec.read(in);
                tag.stack.pop();
                return res;
            }
        };
    }

    class ArgumentJsonTag<T> implements JsonTag<T>, Arg<T> {
        Deque<T> stack = new ArrayDeque<>();

        @Override
        public Entry<T> createEntry() {
            return this::resolve;
        }

        @Override
        public T resolve() {
            return stack.peek();
        }
    }
}
