package com.fiskmods.jdec.decoder;

import com.fiskmods.jdec.decoder.FunctionJsonDecoder.Arg;
import com.fiskmods.jdec.decoder.FunctionJsonDecoder.ArgumentJsonTag;
import com.fiskmods.jdec.tag.JsonTag;
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

    static <T1, T2, R> BiFunctionJsonDecoder<T1, T2, R> fromTag(BiFunction<JsonTag<T1>, JsonTag<T2>, JsonDecoder<R>> func) {
        ArgumentJsonTag<T1> tag1 = new ArgumentJsonTag<>();
        ArgumentJsonTag<T2> tag2 = new ArgumentJsonTag<>();
        JsonDecoder<R> codec = func.apply(tag1, tag2);
        Object lock = new Object();
        return (arg1, arg2) -> in -> {
            synchronized (lock) {
                tag1.value = arg1.resolve();
                tag2.value = arg2.resolve();
                return codec.read(in);
            }
        };
    }

    static <T1, T2, R> BiFunctionJsonDecoder<T1, T2, R> fromValue(BiFunction<Arg<T1>, Arg<T2>, JsonDecoder<R>> func) {
        return fromTag((tag1, tag2) -> func.apply(
                () -> ((ArgumentJsonTag<T1>) tag1).value,
                () -> ((ArgumentJsonTag<T2>) tag2).value
        ));
    }
}
