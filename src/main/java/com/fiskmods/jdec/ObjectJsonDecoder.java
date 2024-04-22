package com.fiskmods.jdec;

import com.mojang.datafixers.util.*;

import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class ObjectJsonDecoder {
    private static <T> JsonDecoder<T> create(Function<JsonTag.Entry[], T> func, JsonTag... tags) {
        return in -> {
            JsonTag.Entry[] entries = new JsonTag.Entry[tags.length];
            for (int i = 0; i < tags.length; ++i) {
                entries[i] = tags[i].initEntry();
            }

            in.beginObject();
            loop: while (in.hasNext()) {
                String name = in.nextName();
                for (JsonTag.Entry e : entries) {
                    if (e.tryRead(in, name)) {
                        continue loop;
                    }
                }
                in.skipValue();
            }
            in.endObject();
            return func.apply(entries);
        };
    }

    public static <T, R> JsonDecoder<R> single(Function<T, R> func, JsonTag<T> tag) {
        return in -> {
            JsonTag.Entry<T> e = tag.initEntry();
            in.beginObject();
            while (in.hasNext()) {
                if (!e.tryRead(in, in.nextName())) {
                    in.skipValue();
                }
            }
            in.endObject();
            return func.apply(e.get());
        };
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, R> JsonDecoder<R> group(BiFunction<T1, T2, R> func, JsonTag<T1> t1, JsonTag<T2> t2) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get()
        ), t1, t2);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, R> JsonDecoder<R> group(
            Function3<T1, T2, T3, R> func, JsonTag<T1> t1, JsonTag<T2> t2, JsonTag<T3> t3) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get()
        ), t1, t2, t3);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, R> JsonDecoder<R> group(
            Function4<T1, T2, T3, T4, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get()
        ), t1, t2, t3, t4);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, R> JsonDecoder<R> group(
            Function5<T1, T2, T3, T4, T5, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get()
        ), t1, t2, t3, t4, t5);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, R> JsonDecoder<R> group(
            Function6<T1, T2, T3, T4, T5, T6, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get()
        ), t1, t2, t3, t4, t5, t6);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, R> JsonDecoder<R> group(
            Function7<T1, T2, T3, T4, T5, T6, T7, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get()
        ), t1, t2, t3, t4, t5, t6, t7);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> JsonDecoder<R> group(
            Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8);
    }
}
