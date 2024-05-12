package com.fiskmods.jdec.decoder;

import com.fiskmods.jdec.tag.JsonTag;
import com.mojang.datafixers.util.*;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ObjectJsonDecoder {
    private static <T> JsonDecoder<T> create(Function<JsonTag.Entry<?>[], T> func, JsonTag<?>... tags) {
        for (JsonTag<?> tag : tags) {
            tag.initTag(tags);
        }
        return in -> {
            JsonTag.Entry<?>[] entries = new JsonTag.Entry[tags.length];
            for (int i = 0; i < tags.length; ++i) {
                entries[i] = tags[i].createEntry();
            }

            in.beginObject();
            loop: while (in.hasNext()) {
                String name = in.nextName();
                for (JsonTag.Entry<?> e : entries) {
                    if (e.tryRead(in, name, entries)) {
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
        tag.initTag(null);
        return in -> {
            JsonTag.Entry<T> e = tag.createEntry();
            in.beginObject();
            while (in.hasNext()) {
                if (!e.tryRead(in, in.nextName(), null)) {
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

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> JsonDecoder<R> group(
            Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8,
            JsonTag<T9> t9) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get(),
                (T9) entries[8].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> JsonDecoder<R> group(
            Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8,
            JsonTag<T9> t9,
            JsonTag<T10> t10) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get(),
                (T9) entries[8].get(),
                (T10) entries[9].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> JsonDecoder<R> group(
            Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8,
            JsonTag<T9> t9,
            JsonTag<T10> t10,
            JsonTag<T11> t11) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get(),
                (T9) entries[8].get(),
                (T10) entries[9].get(),
                (T11) entries[10].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> JsonDecoder<R> group(
            Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8,
            JsonTag<T9> t9,
            JsonTag<T10> t10,
            JsonTag<T11> t11,
            JsonTag<T12> t12) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get(),
                (T9) entries[8].get(),
                (T10) entries[9].get(),
                (T11) entries[10].get(),
                (T12) entries[11].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> JsonDecoder<R> group(
            Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8,
            JsonTag<T9> t9,
            JsonTag<T10> t10,
            JsonTag<T11> t11,
            JsonTag<T12> t12,
            JsonTag<T13> t13) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get(),
                (T9) entries[8].get(),
                (T10) entries[9].get(),
                (T11) entries[10].get(),
                (T12) entries[11].get(),
                (T13) entries[12].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> JsonDecoder<R> group(
            Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8,
            JsonTag<T9> t9,
            JsonTag<T10> t10,
            JsonTag<T11> t11,
            JsonTag<T12> t12,
            JsonTag<T13> t13,
            JsonTag<T14> t14) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get(),
                (T9) entries[8].get(),
                (T10) entries[9].get(),
                (T11) entries[10].get(),
                (T12) entries[11].get(),
                (T13) entries[12].get(),
                (T14) entries[13].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> JsonDecoder<R> group(
            Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8,
            JsonTag<T9> t9,
            JsonTag<T10> t10,
            JsonTag<T11> t11,
            JsonTag<T12> t12,
            JsonTag<T13> t13,
            JsonTag<T14> t14,
            JsonTag<T15> t15) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get(),
                (T9) entries[8].get(),
                (T10) entries[9].get(),
                (T11) entries[10].get(),
                (T12) entries[11].get(),
                (T13) entries[12].get(),
                (T14) entries[13].get(),
                (T15) entries[14].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> JsonDecoder<R> group(
            Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> func,
            JsonTag<T1> t1,
            JsonTag<T2> t2,
            JsonTag<T3> t3,
            JsonTag<T4> t4,
            JsonTag<T5> t5,
            JsonTag<T6> t6,
            JsonTag<T7> t7,
            JsonTag<T8> t8,
            JsonTag<T9> t9,
            JsonTag<T10> t10,
            JsonTag<T11> t11,
            JsonTag<T12> t12,
            JsonTag<T13> t13,
            JsonTag<T14> t14,
            JsonTag<T15> t15,
            JsonTag<T16> t16) {
        return create(entries -> func.apply(
                (T1) entries[0].get(),
                (T2) entries[1].get(),
                (T3) entries[2].get(),
                (T4) entries[3].get(),
                (T5) entries[4].get(),
                (T6) entries[5].get(),
                (T7) entries[6].get(),
                (T8) entries[7].get(),
                (T9) entries[8].get(),
                (T10) entries[9].get(),
                (T11) entries[10].get(),
                (T12) entries[11].get(),
                (T13) entries[12].get(),
                (T14) entries[13].get(),
                (T15) entries[14].get(),
                (T16) entries[15].get()
        ), t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
}
