package com.fiskmods.jdec;

import com.fiskmods.jdec.exception.JsonDecoderException;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonDecoderTest {
    @Test
    void readCheckedWrapsExceptions() {
        JsonDecoder<?> codec = in -> {
            throw new IllegalStateException();
        };
        assertThrows(JsonDecoderException.class, () ->
                TestUtils.deserialize(codec));
    }

    @Test
    void mapToSameType() throws IOException {
        JsonDecoder<Integer> codec = in -> 17;
        codec = codec.map(t -> t + 4);
        assertEquals(21, TestUtils.deserialize(codec));
    }

    @Test
    void mapToDifferentType() throws IOException {
        JsonDecoder<?> codec = in -> 17;
        codec = codec.map(t -> "I have " + t + " problems");
        assertEquals("I have 17 problems", TestUtils.deserialize(codec));
    }

    @Test
    void or() throws IOException {
        JsonDecoder<?> codec = Jdec.STRICT_INT.asObject()
                .or(JsonToken.BOOLEAN, Jdec.BOOL);
        assertEquals(17, TestUtils.deserialize(codec, "17"));
        assertEquals(false, TestUtils.deserialize(codec, "false"));
        assertThrows(JsonDecoderException.class, () ->
                TestUtils.deserialize(codec, "4.3"));
    }

    @Test
    void cantReadNullsNormally() {
        assertThrows(JsonDecoderException.class, () ->
                TestUtils.deserialize(Jdec.STRING, "null"));
    }

    @Test
    void orNullLetsYouReadNulls() throws IOException {
        assertNull(TestUtils.deserialize(Jdec.STRING.orNull(), "null"));
    }
}
