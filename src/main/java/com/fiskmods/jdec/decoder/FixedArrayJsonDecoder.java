package com.fiskmods.jdec.decoder;

import java.util.function.IntFunction;

public class FixedArrayJsonDecoder {
    public static <T> JsonDecoder<T[]> from(JsonDecoder<T> codec, IntFunction<T[]> generator, int length) {
        return in -> {
            T[] array = generator.apply(length);
            int index = -1;
            in.beginArray();
            while (in.hasNext()) {
                if (index + 1 < array.length) {
                    array[++index] = codec.read(in);
                    continue;
                }
                in.skipValue();
            }
            in.endArray();
            return array;
        };
    }

    public static JsonDecoder<boolean[]> fromBoolean(JsonDecoder<Boolean> codec, int length) {
        return in -> {
            boolean[] array = new boolean[length];
            int index = -1;
            in.beginArray();
            while (in.hasNext()) {
                if (index + 1 < array.length) {
                    array[++index] = codec.read(in);
                    continue;
                }
                in.skipValue();
            }
            in.endArray();
            return array;
        };
    }

    public static JsonDecoder<byte[]> fromByte(JsonDecoder<? extends Number> codec, int length) {
        return in -> {
            byte[] array = new byte[length];
            int index = -1;
            in.beginArray();
            while (in.hasNext()) {
                if (index + 1 < array.length) {
                    array[++index] = codec.read(in).byteValue();
                    continue;
                }
                in.skipValue();
            }
            in.endArray();
            return array;
        };
    }

    public static JsonDecoder<short[]> fromShort(JsonDecoder<? extends Number> codec, int length) {
        return in -> {
            short[] array = new short[length];
            int index = -1;
            in.beginArray();
            while (in.hasNext()) {
                if (index + 1 < array.length) {
                    array[++index] = codec.read(in).shortValue();
                    continue;
                }
                in.skipValue();
            }
            in.endArray();
            return array;
        };
    }

    public static JsonDecoder<int[]> fromInt(JsonDecoder<? extends Number> codec, int length) {
        return in -> {
            int[] array = new int[length];
            int index = -1;
            in.beginArray();
            while (in.hasNext()) {
                if (index + 1 < array.length) {
                    array[++index] = codec.read(in).intValue();
                    continue;
                }
                in.skipValue();
            }
            in.endArray();
            return array;
        };
    }

    public static JsonDecoder<long[]> fromLong(JsonDecoder<? extends Number> codec, int length) {
        return in -> {
            long[] array = new long[length];
            int index = -1;
            in.beginArray();
            while (in.hasNext()) {
                if (index + 1 < array.length) {
                    array[++index] = codec.read(in).longValue();
                    continue;
                }
                in.skipValue();
            }
            in.endArray();
            return array;
        };
    }

    public static JsonDecoder<float[]> fromFloat(JsonDecoder<? extends Number> codec, int length) {
        return in -> {
            float[] array = new float[length];
            int index = -1;
            in.beginArray();
            while (in.hasNext()) {
                if (index + 1 < array.length) {
                    array[++index] = codec.read(in).floatValue();
                    continue;
                }
                in.skipValue();
            }
            in.endArray();
            return array;
        };
    }

    public static JsonDecoder<double[]> fromDouble(JsonDecoder<? extends Number> codec, int length) {
        return in -> {
            double[] array = new double[length];
            int index = -1;
            in.beginArray();
            while (in.hasNext()) {
                if (index + 1 < array.length) {
                    array[++index] = codec.read(in).doubleValue();
                    continue;
                }
                in.skipValue();
            }
            in.endArray();
            return array;
        };
    }
}
