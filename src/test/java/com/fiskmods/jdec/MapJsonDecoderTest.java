package com.fiskmods.jdec;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapJsonDecoderTest {
    @Test
    void mapReadsExactly() throws IOException {
        MapJsonDecoder<String, Integer> codec = MapJsonDecoder.from(Function.identity(), Jdec.STRICT_INT);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("foo", 1);
        expected.put("bar", -8);
        expected.put("baz", 13);

        assertEquals(expected, TestUtils.deserialize(codec, """
                {
                  "foo": 1,
                  "baz": 13,
                  "bar": -8
                }
                """));
    }

    @Test
    void differentCodecsBasedOnKey() throws IOException {
        MapJsonDecoder<String, Object> codec = MapJsonDecoder.from(Function.identity(),
                (String k) -> k.charAt(0) == 'b' ? Jdec.STRICT_INT.asObject() : Jdec.BOOL.asObject());
        Map<String, Object> expected = new HashMap<>();
        expected.put("foo", true);
        expected.put("bar", -8);
        expected.put("baz", 13);

        assertEquals(expected, TestUtils.deserialize(codec, """
                {
                  "foo": true,
                  "baz": 13,
                  "bar": -8
                }
                """));
    }
}
