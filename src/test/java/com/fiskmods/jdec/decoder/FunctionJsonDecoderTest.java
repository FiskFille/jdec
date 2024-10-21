package com.fiskmods.jdec.decoder;

import com.fiskmods.jdec.Jdec;
import com.fiskmods.jdec.TestUtils;
import com.fiskmods.jdec.exception.JsonDecoderException;
import com.fiskmods.jdec.tag.JsonTag;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FunctionJsonDecoderTest {
    @Test
    void argumentFromTag() throws IOException {
        FunctionJsonDecoder<Color, Car> codec = FunctionJsonDecoder.fromTag(
                color -> ObjectJsonDecoder.group(Car::new,
                        JsonTag.required("brand", EnumJsonDecoder.of(Brand.class)),
                        color));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(Color.BLUE, obj.color);
    }

    @Test
    void argumentFromTagWorksMultipleTimes() throws IOException {
        FunctionJsonDecoder<Color, Car> codec = FunctionJsonDecoder.fromTag(
                color -> ObjectJsonDecoder.group(Car::new,
                        JsonTag.required("brand", EnumJsonDecoder.of(Brand.class)),
                        color));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(Color.BLUE, obj.color);

        obj = TestUtils.deserialize(codec.with(Color.GREEN), "{ \"brand\": \"FORD\" }");
        assertEquals(Brand.FORD, obj.brand);
        assertEquals(Color.GREEN, obj.color);
    }

    @Test
    void argumentFromValue() throws IOException {
        FunctionJsonDecoder<Color, Car> codec = FunctionJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        JsonTag.required("brand", EnumJsonDecoder.of(Brand.class)),
                        JsonTag.optionalGet("color", EnumJsonDecoder.of(Color.class), color::resolve)));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(Color.BLUE, obj.color);
    }

    @Test
    void argumentFromValueWorksMultipleTimes() throws IOException {
        FunctionJsonDecoder<Color, Car> codec = FunctionJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        JsonTag.required("brand", EnumJsonDecoder.of(Brand.class)),
                        JsonTag.optionalGet("color", EnumJsonDecoder.of(Color.class), color::resolve)));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(Color.BLUE, obj.color);

        obj = TestUtils.deserialize(codec.with(Color.RED), "{ \"brand\": \"FORD\" }");
        assertEquals(Brand.FORD, obj.brand);
        assertEquals(Color.RED, obj.color);
    }

    @Test
    void argumentFromValueWithArgumentAsInput() throws IOException {
        FunctionJsonDecoder<Color, Car> car = FunctionJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        JsonTag.required("brand", EnumJsonDecoder.of(Brand.class)),
                        JsonTag.optionalGet("color", EnumJsonDecoder.of(Color.class), color::resolve)));

        FunctionJsonDecoder<Color, Factory> codec = FunctionJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Factory::new,
                        JsonTag.required("car", car.with(color)),
                        JsonTag.required("workers", Jdec.STRICT_INT)));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), """
                {
                  "workers": 100,
                  "car": {
                    "brand": "FORD"
                  }
                }
                """);
        assertEquals(100, obj.workers);
        assertEquals(Brand.FORD, obj.car.brand);
        assertEquals(Color.BLUE, obj.car.color);
    }

    @Test
    void argumentFromValueWithArgumentAsInputWorksMultipleTimes() throws IOException {
        FunctionJsonDecoder<Color, Car> car = FunctionJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        JsonTag.required("brand", EnumJsonDecoder.of(Brand.class)),
                        JsonTag.optionalGet("color", EnumJsonDecoder.of(Color.class), color::resolve)));

        FunctionJsonDecoder<Color, Factory> codec = FunctionJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Factory::new,
                        JsonTag.required("car", car.with(color)),
                        JsonTag.required("workers", Jdec.STRICT_INT)));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), """
                {
                  "workers": 100,
                  "car": {
                    "brand": "FORD"
                  }
                }
                """);
        assertEquals(100, obj.workers);
        assertEquals(Brand.FORD, obj.car.brand);
        assertEquals(Color.BLUE, obj.car.color);

        obj = TestUtils.deserialize(codec.with(Color.RED), """
                {
                  "workers": 85,
                  "car": {
                    "brand": "VOLVO"
                  }
                }
                """);
        assertEquals(85, obj.workers);
        assertEquals(Brand.VOLVO, obj.car.brand);
        assertEquals(Color.RED, obj.car.color);
    }

    @Test
    void map() throws IOException {
        FunctionJsonDecoder<Color, Car> car = FunctionJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        JsonTag.required("brand", EnumJsonDecoder.of(Brand.class)),
                        JsonTag.optionalGet("color", EnumJsonDecoder.of(Color.class), color::resolve)));

        FunctionJsonDecoder<Color, Factory> codec = car
                .map(t -> new Factory(t, 900));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(900, obj.workers);
        assertEquals(Brand.VOLVO, obj.car.brand);
        assertEquals(Color.BLUE, obj.car.color);
    }

    @Test
    void orWithoutArgument() throws IOException {
        var codec = FunctionJsonDecoder.<Brand, Dealership> fromTag(
                        brand -> ObjectJsonDecoder.group(Dealership::new,
                                brand,
                                JsonTag.required("quantity", Jdec.STRICT_INT),
                                JsonTag.required("rating", Jdec.STRICT_INT)))
                .or(JsonToken.NUMBER, in ->
                        new Dealership(Brand.VOLVO, in.nextInt(), 10))
                .or(t -> t == JsonToken.NULL, in -> {
                    throw new NullPointerException();
                });

        var obj = TestUtils.deserialize(codec.with(Brand.FORD),
                "{ \"quantity\": 12, \"rating\": 7 }");
        assertEquals(Brand.FORD, obj.brand);
        assertEquals(12, obj.quantity);
        assertEquals(7, obj.rating);

        obj = TestUtils.deserialize(codec.with(Brand.FORD), "5");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(5, obj.quantity);
        assertEquals(10, obj.rating);

        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec.with(Brand.FORD), "null"));
    }

    @Test
    void orWithArgument() throws IOException {
        var codec = FunctionJsonDecoder.<Brand, Dealership> fromTag(
                        brand -> ObjectJsonDecoder.group(Dealership::new,
                                brand,
                                JsonTag.required("quantity", Jdec.STRICT_INT),
                                JsonTag.required("rating", Jdec.STRICT_INT)))
                .orArg(JsonToken.NUMBER, FunctionJsonDecoder.fromValue(brand ->
                        in -> new Dealership(brand.resolve(), in.nextInt(), 10)))
                .orArg(t -> t == JsonToken.BOOLEAN, FunctionJsonDecoder.fromValue(brand ->
                        in -> new Dealership(brand.resolve(), in.nextBoolean() ? 0 : 19, 3)));

        var obj = TestUtils.deserialize(codec.with(Brand.FORD),
                "{ \"quantity\": 12, \"rating\": 7 }");
        assertEquals(Brand.FORD, obj.brand);
        assertEquals(12, obj.quantity);
        assertEquals(7, obj.rating);

        obj = TestUtils.deserialize(codec.with(Brand.FORD), "5");
        assertEquals(Brand.FORD, obj.brand);
        assertEquals(5, obj.quantity);
        assertEquals(10, obj.rating);

        obj = TestUtils.deserialize(codec.with(Brand.VOLVO), "false");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(19, obj.quantity);
        assertEquals(3, obj.rating);

        assertThrows(JsonDecoderException.class,
                () -> TestUtils.deserialize(codec.with(Brand.FORD), "\"foo\""));
    }

    record Car(Brand brand, Color color) {
    }

    enum Brand {
        FORD,
        VOLVO
    }

    enum Color {
        RED,
        GREEN,
        BLUE
    }

    record Factory(Car car, int workers) {
    }

    record Dealership(Brand brand, int quantity, int rating) {
    }
}
