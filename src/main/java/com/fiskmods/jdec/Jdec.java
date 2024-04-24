package com.fiskmods.jdec;

import com.fiskmods.jdec.decoder.FixedArrayJsonDecoder;
import com.fiskmods.jdec.decoder.JsonDecoder;
import com.fiskmods.jdec.decoder.ListJsonDecoder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

public interface Jdec {
    JsonDecoder<Boolean> BOOL = JsonReader::nextBoolean;
    JsonDecoder<Byte> BYTE = in -> (byte) in.nextInt();
    JsonDecoder<Short> SHORT = in -> (short) in.nextInt();
    JsonDecoder<Integer> STRICT_INT = JsonReader::nextInt;
    JsonDecoder<Integer> ANY_INT = Jdec::readAnyInt;
    JsonDecoder<Long> LONG = JsonReader::nextLong;
    JsonDecoder<Float> FLOAT = in -> (float) in.nextDouble();
    JsonDecoder<Double> DOUBLE = JsonReader::nextDouble;
    JsonDecoder<String> STRING = JsonReader::nextString;

    JsonDecoder<int[]> ARRAY_2I = FixedArrayJsonDecoder.fromInt(STRICT_INT, 2);
    JsonDecoder<int[]> ARRAY_3I = FixedArrayJsonDecoder.fromInt(STRICT_INT, 3);
    JsonDecoder<int[]> ARRAY_UI = ListJsonDecoder.of(STRICT_INT).toIntArray(Integer::intValue);
    JsonDecoder<float[]> ARRAY_2F = FixedArrayJsonDecoder.fromFloat(FLOAT, 2);
    JsonDecoder<float[]> ARRAY_3F = FixedArrayJsonDecoder.fromFloat(FLOAT, 3);
    JsonDecoder<float[]> ARRAY_UF = ListJsonDecoder.of(FLOAT).toFloatArray(Float::floatValue);

    private static int readAnyInt(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.STRING) {
            return Integer.decode(in.nextString());
        }
        return in.nextInt();
    }
}
