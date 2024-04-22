package com.fiskmods.jdec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

@FunctionalInterface
public interface ListJsonDecoder<T> extends JsonDecoder<List<T>> {
    default JsonDecoder<T[]> toArray(IntFunction<T[]> generator) {
        return map(t -> t.toArray(generator));
    }

    default JsonDecoder<boolean[]> toBooleanArray(Predicate<T> function) {
        return map(list -> {
            boolean[] array = new boolean[list.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = function.test(list.get(i));
            }
            return array;
        });
    }

    default JsonDecoder<byte[]> toByteArray(Function<T, Byte> function) {
        return map(list -> {
            byte[] array = new byte[list.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = function.apply(list.get(i));
            }
            return array;
        });
    }

    default JsonDecoder<short[]> toShortArray(Function<T, Short> function) {
        return map(list -> {
            short[] array = new short[list.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = function.apply(list.get(i));
            }
            return array;
        });
    }

    default JsonDecoder<int[]> toIntArray(ToIntFunction<T> function) {
        return map(list -> {
            int[] array = new int[list.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = function.applyAsInt(list.get(i));
            }
            return array;
        });
    }

    default JsonDecoder<long[]> toLongArray(ToLongFunction<T> function) {
        return map(list -> {
            long[] array = new long[list.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = function.applyAsLong(list.get(i));
            }
            return array;
        });
    }

    default JsonDecoder<float[]> toFloatArray(Function<T, Float> function) {
        return map(list -> {
            float[] array = new float[list.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = function.apply(list.get(i));
            }
            return array;
        });
    }

    default JsonDecoder<double[]> toDoubleArray(ToDoubleFunction<T> function) {
        return map(list -> {
            double[] array = new double[list.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = function.applyAsDouble(list.get(i));
            }
            return array;
        });
    }

    static <T> ListJsonDecoder<T> of(JsonDecoder<T> codec, Supplier<List<T>> supplier) {
        return in -> {
            List<T> list = supplier.get();
            in.beginArray();
            while (in.hasNext()) {
                list.add(codec.read(in));
            }
            in.endArray();
            return list;
        };
    }

    static <T> ListJsonDecoder<T> of(JsonDecoder<T> codec) {
        return of(codec, ArrayList::new);
    }
}
