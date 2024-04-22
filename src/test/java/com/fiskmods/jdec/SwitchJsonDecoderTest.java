package com.fiskmods.jdec;

import com.fiskmods.jdec.exception.JsonDecoderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SwitchJsonDecoderTest {
    static JsonDecoder<Float> codec;

    @BeforeAll
    static void setUp() {
        codec = SwitchJsonDecoder.build(b -> b
                .on("run", Jdec.FLOAT.map(t -> t * 2))
                .on("walk", Jdec.FLOAT)
                .on("sneak", Jdec.FLOAT.map(t -> t / 2)));
    }

    @Test
    void switchBuilderMapsToDifferentValues() throws IOException {
        assertEquals(8, TestUtils.deserialize(codec, "{ \"run\": 4 }"));
        assertEquals(4, TestUtils.deserialize(codec, "{ \"walk\": 4 }"));
        assertEquals(2, TestUtils.deserialize(codec, "{ \"sneak\": 4 }"));
    }

    @Test
    void switchThrowsExceptionOnUnknownKey() {
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, "{ \"sprint\": 4 }"));
    }

    @Test
    void switchThrowsExceptionOnBuilderWithNoKeys() {
        JsonDecoder<Float> codec = SwitchJsonDecoder.build(b -> { });
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, "{ \"run\": 4 }"));
    }
}
