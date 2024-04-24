package com.fiskmods.jdec.decoder;

import com.fiskmods.jdec.TestUtils;
import com.fiskmods.jdec.exception.JsonDecoderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumJsonDecoderTest {
    @Test
    void enumReadsProperly() throws IOException {
        JsonDecoder<?> codec = EnumJsonDecoder.of(TestEnum.class);
        Assertions.assertEquals(TestEnum.BAR, TestUtils.deserialize(codec, "\"BAR\""));
    }

    @Test
    void errorOnNonexistentEnum() {
        JsonDecoder<?> codec = EnumJsonDecoder.of(TestEnum.class);
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, "\"CAFE\""));
    }

    @Test
    void enumWithKeyReadsProperly() throws IOException {
        JsonDecoder<?> codec = EnumJsonDecoder.of(TestEnum.class, TestEnum::getKey);
        assertEquals(TestEnum.BAZ, TestUtils.deserialize(codec, "\"three\""));
    }

    @Test
    void errorOnNonexistentEnumWithKey() {
        JsonDecoder<?> codec = EnumJsonDecoder.of(TestEnum.class, TestEnum::getKey);
        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec, "\"six\""));
    }

    enum TestEnum {
        FOO("one"),
        BAR("two"),
        BAZ("three");

        final String key;
        TestEnum(String key) {
            this.key = key;
        }

        String getKey() {
            return key;
        }
    }
}
