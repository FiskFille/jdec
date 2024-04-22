package com.fiskmods.jdec;

import com.fiskmods.jdec.exception.JsonDecoderException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JdecTest {
    @Test
    void strictInt() throws IOException {
        assertEquals(255, TestUtils.deserialize(Jdec.STRICT_INT, "255"));
        assertThrows(JsonDecoderException.class, () ->
                TestUtils.deserialize(Jdec.STRICT_INT, "\"#FF\""));
        assertThrows(JsonDecoderException.class, () ->
                TestUtils.deserialize(Jdec.STRICT_INT, "\"0xFF\""));
    }

    @Test
    void anyInt() throws IOException {
        assertEquals(255, TestUtils.deserialize(Jdec.ANY_INT, "255"));
        assertEquals(255, TestUtils.deserialize(Jdec.ANY_INT, "\"#FF\""));
        assertEquals(255, TestUtils.deserialize(Jdec.ANY_INT, "\"0xFF\""));
    }

    @Test
    void array2fNoOverflow() throws IOException {
        assertArrayEquals(new float[] {1, 2.1F}, TestUtils.deserialize(Jdec.ARRAY_2F, "[1, 2.1, 3]"));
    }

    @Test
    void array2fNoUnderflow() throws IOException {
        assertArrayEquals(new float[] {1, 0}, TestUtils.deserialize(Jdec.ARRAY_2F, "[1]"));
    }

    @Test
    void array2fOnlyAcceptsNumbers() {
        assertThrows(JsonDecoderException.class, () ->
                TestUtils.deserialize(Jdec.ARRAY_2F, "[1, true]"));
    }

    @Test
    void array2iNoOverflow() throws IOException {
        assertArrayEquals(new int[] {1, 2}, TestUtils.deserialize(Jdec.ARRAY_2I, "[1, 2, 3]"));
    }

    @Test
    void array2iNoUnderflow() throws IOException {
        assertArrayEquals(new int[] {1, 0}, TestUtils.deserialize(Jdec.ARRAY_2I, "[1]"));
    }

    @Test
    void array2iOnlyAcceptsNumbers() {
        assertThrows(JsonDecoderException.class, () ->
                TestUtils.deserialize(Jdec.ARRAY_2I, "[1, true]"));
    }

    @Test
    void array2iOnlyAcceptsInts() {
        assertThrows(JsonDecoderException.class, () ->
                TestUtils.deserialize(Jdec.ARRAY_2I, "[1, 2.1]"));
    }
}
