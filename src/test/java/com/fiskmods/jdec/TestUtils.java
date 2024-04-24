package com.fiskmods.jdec;

import com.fiskmods.jdec.decoder.JsonDecoder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

public class TestUtils {
    public static <T> T deserialize(JsonDecoder<T> codec, String text) throws IOException {
        try (JsonReader in = new JsonReader(new StringReader(text))) {
            return codec.readChecked(in);
        }
    }

    public static <T> T deserialize(JsonDecoder<T> codec) throws IOException {
        return deserialize(codec, "");
    }
}
