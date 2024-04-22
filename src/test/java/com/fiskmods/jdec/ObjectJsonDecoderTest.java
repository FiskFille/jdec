package com.fiskmods.jdec;

import com.fiskmods.jdec.exception.JsonDecoderException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ObjectJsonDecoderTest {
    @Test
    void singleTagRequired() throws IOException {
        JsonDecoder<TestObjectSingle> codec = ObjectJsonDecoder.single(TestObjectSingle::new,
                Jdec.STRICT_INT.required("count"));

        assertEquals(43, TestUtils.deserialize(codec, "{ \"count\": 43 }").num);
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, "{ \"other\": 44 }"));
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, "{ }"));
    }

    @Test
    void singleTagOptional() throws IOException {
        JsonDecoder<TestObjectSingle> codec = ObjectJsonDecoder.single(TestObjectSingle::new,
                Jdec.STRICT_INT.optional("count", -1));

        assertEquals(43, TestUtils.deserialize(codec, "{ \"count\": 43 }").num);
        assertEquals(-1, TestUtils.deserialize(codec, "{ \"other\": 44 }").num);
        assertEquals(-1, TestUtils.deserialize(codec, "{ }").num);
    }

    @Test
    void singleTagOptionalNull() throws IOException {
        JsonDecoder<TestObjectSingle> codec = ObjectJsonDecoder.single(TestObjectSingle::new,
                Jdec.STRICT_INT.optionalNull("count"));

        assertEquals(43, TestUtils.deserialize(codec, "{ \"count\": 43 }").num);
        assertNull(TestUtils.deserialize(codec, "{ \"other\": 44 }").num);
        assertNull(TestUtils.deserialize(codec, "{ }").num);
    }

    @Test
    void groupTagRequired() throws IOException {
        JsonDecoder<TestObjectGroup> codec = ObjectJsonDecoder.group(TestObjectGroup::new,
                Jdec.STRICT_INT.required("count"),
                Jdec.STRING.optional("name", "unknown"));

        TestObjectGroup obj = TestUtils.deserialize(codec, "{ \"count\": 43 }");
        assertEquals(43, obj.num);
        assertEquals("unknown", obj.str);
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, "{ \"other\": 44 }"));
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, "{ }"));
    }

    @Test
    void groupTagOptional() throws IOException {
        JsonDecoder<TestObjectGroup> codec = ObjectJsonDecoder.group(TestObjectGroup::new,
                Jdec.STRICT_INT.optional("count", -1),
                Jdec.STRING.optional("name", "unknown"));

        TestObjectGroup obj = TestUtils.deserialize(codec, "{ \"count\": 43 }");
        assertEquals(43, obj.num);
        assertEquals("unknown", obj.str);
        assertEquals(-1, TestUtils.deserialize(codec, "{ \"other\": 44 }").num);
        assertEquals(-1, TestUtils.deserialize(codec, "{ }").num);
    }

    @Test
    void groupTagOptionalNull() throws IOException {
        JsonDecoder<TestObjectGroup> codec = ObjectJsonDecoder.group(TestObjectGroup::new,
                Jdec.STRICT_INT.optionalNull("count"),
                Jdec.STRING.optional("name", "unknown"));

        TestObjectGroup obj = TestUtils.deserialize(codec, "{ \"count\": 43 }");
        assertEquals(43, obj.num);
        assertEquals("unknown", obj.str);
        assertNull(TestUtils.deserialize(codec, "{ \"other\": 44 }").num);
        assertNull(TestUtils.deserialize(codec, "{ }").num);
    }

    record TestObjectSingle(Integer num) {
    }

    record TestObjectGroup(Integer num, String str) {
    }
}
