package com.fiskmods.jdec.decoder;

import com.fiskmods.jdec.decoder.FunctionJsonDecoder.Arg;
import com.fiskmods.jdec.decoder.FunctionJsonDecoder.ArgumentJsonTag;
import com.google.gson.stream.JsonToken;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface BiFunctionJsonDecoder<T1, T2, R> {
    JsonDecoder<R> with(Arg<? extends T1> argument1, Arg<? extends T2> argument2);

    default JsonDecoder<R> with(T1 argument1, T2 argument2) {
        return with(() -> argument1, () -> argument2);
    }

    default JsonDecoder<R> with(Arg<? extends T1> argument1, T2 argument2) {
        return with(argument1, () -> argument2);
    }

    default JsonDecoder<R> with(T1 argument1, Arg<? extends T2> argument2) {
        return with(() -> argument1, argument2);
    }

    default FunctionJsonDecoder<T2, R> withFirst(Arg<T1> argument1) {
        return arg -> with(argument1, arg);
    }

    default FunctionJsonDecoder<T2, R> withFirst(T1 argument1) {
        return arg -> with(() -> argument1, arg);
    }

    default FunctionJsonDecoder<T1, R> withSecond(Arg<T2> argument2) {
        return arg -> with(arg, argument2);
    }

    default FunctionJsonDecoder<T1, R> withSecond(T2 argument2) {
        return arg -> with(arg, () -> argument2);
    }

    default <S> BiFunctionJsonDecoder<T1, T2, S> map(Function<R, S> mappingFunction) {
        return (arg1, arg2) -> with(arg1, arg2).map(mappingFunction);
    }

    default BiFunctionJsonDecoder<T1, T2, R> or(JsonToken onToken, JsonDecoder<? extends R> other) {
        return (arg1, arg2) -> in -> in.peek() == onToken
                ? other.read(in)
                : with(arg1, arg2).read(in);
    }

    default BiFunctionJsonDecoder<T1, T2, R> orArg(JsonToken onToken,
                                                   BiFunctionJsonDecoder<? super T1, ? super T2, ? extends R> other) {
        return (arg1, arg2) -> in -> in.peek() == onToken
                ? other.with(arg1, arg2).read(in)
                : with(arg1, arg2).read(in);
    }

    default BiFunctionJsonDecoder<T1, T2, R> or(Predicate<JsonToken> onToken, JsonDecoder<? extends R> other) {
        return (arg1, arg2) -> in -> onToken.test(in.peek())
                ? other.read(in)
                : with(arg1, arg2).read(in);
    }

    default BiFunctionJsonDecoder<T1, T2, R> orArg(Predicate<JsonToken> onToken,
                                                   BiFunctionJsonDecoder<? super T1, ? super T2, ? extends R> other) {
        return (arg1, arg2) -> in -> onToken.test(in.peek())
                ? other.with(arg1, arg2).read(in)
                : with(arg1, arg2).read(in);
    }

    static <T1, T2, R> BiFunctionJsonDecoder<T1, T2, R> from(
            BiFunction<ArgumentJsonTag<T1>, ArgumentJsonTag<T2>, JsonDecoder<R>> func) {
        ArgumentJsonTag<T1> tag1 = new ArgumentJsonTag<>();
        ArgumentJsonTag<T2> tag2 = new ArgumentJsonTag<>();
        JsonDecoder<R> codec = func.apply(tag1, tag2);
        Object lock = new Object();
        return (arg1, arg2) -> in -> {
            synchronized (lock) {
                tag1.stack.push(arg1.resolve());
                tag2.stack.push(arg2.resolve());
                R res = codec.read(in);
                tag1.stack.pop();
                tag2.stack.pop();
                return res;
            }
        };
    }
}
