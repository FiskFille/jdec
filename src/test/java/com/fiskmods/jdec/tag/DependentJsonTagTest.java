package com.fiskmods.jdec.tag;

import com.fiskmods.jdec.Jdec;
import com.fiskmods.jdec.decoder.JsonDecoder;
import com.fiskmods.jdec.decoder.ObjectJsonDecoder;
import com.fiskmods.jdec.TestUtils;
import com.fiskmods.jdec.exception.JsonDecoderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DependentJsonTagTest {
    static JsonDecoder<Person> codec;

    @BeforeAll
    static void setUp() {
        codec = ObjectJsonDecoder.group(Person::new,
                JsonTag.required("name", Jdec.STRING),
                JsonTag.requiredAfter("address", "name",
                        name -> Jdec.STRING.map(address -> new House((String) name.resolve(), address))));
    }

    @Test
    void requiredAfterWorksProperly() throws IOException {
        var obj = TestUtils.deserialize(codec, """
                {
                  "name": "Steve",
                  "address": "Sample Street 12"
                }
                """);
        assertEquals("Steve", obj.name);
        assertEquals("Steve", obj.house.owner);
        assertEquals("Sample Street 12", obj.house.address);
    }

    @Test
    void errorIfRequiredTagComesAfter() {
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, """
                        {
                          "address": "Sample Street 12",
                          "name": "Steve"
                        }
                        """));
    }

    @Test
    void errorIfRequiredTagIsMissing() {
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, """
                        {
                          "address": "Sample Street 12"
                        }
                        """));
    }

    @Test
    void errorIfRequiredTagIsUndefined() {
        assertThrows(IllegalArgumentException.class,
                () -> ObjectJsonDecoder.group(Person::new,
                        JsonTag.required("name", Jdec.STRING),
                        JsonTag.requiredAfter("address", "first_name",
                                name -> Jdec.STRING.map(address -> new House((String) name.resolve(), address)))));
    }

    record Person(String name, House house) {
    }

    record House(String owner, String address) {
    }
}
