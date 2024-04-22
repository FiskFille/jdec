package com.fiskmods.jdec;

import com.fiskmods.jdec.exception.JsonDecoderException;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentJsonDecoderTest {
    @Test
    void argumentFromTag() throws IOException {
        ArgumentJsonDecoder<Color, Car> codec = ArgumentJsonDecoder.fromTag(
                color -> ObjectJsonDecoder.group(Car::new,
                        EnumJsonDecoder.of(Brand.class).required("brand"),
                        color));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(Color.BLUE, obj.color);
    }

    @Test
    void argumentFromTagWorksMultipleTimes() throws IOException {
        ArgumentJsonDecoder<Color, Car> codec = ArgumentJsonDecoder.fromTag(
                color -> ObjectJsonDecoder.group(Car::new,
                        EnumJsonDecoder.of(Brand.class).required("brand"),
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
        ArgumentJsonDecoder<Color, Car> codec = ArgumentJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        EnumJsonDecoder.of(Brand.class).required("brand"),
                        EnumJsonDecoder.of(Color.class).optional("color", color::resolve)));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(Color.BLUE, obj.color);
    }

    @Test
    void argumentFromValueWorksMultipleTimes() throws IOException {
        ArgumentJsonDecoder<Color, Car> codec = ArgumentJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        EnumJsonDecoder.of(Brand.class).required("brand"),
                        EnumJsonDecoder.of(Color.class).optional("color", color::resolve)));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(Brand.VOLVO, obj.brand);
        assertEquals(Color.BLUE, obj.color);

        obj = TestUtils.deserialize(codec.with(Color.RED), "{ \"brand\": \"FORD\" }");
        assertEquals(Brand.FORD, obj.brand);
        assertEquals(Color.RED, obj.color);
    }

    @Test
    void argumentFromValueWithArgumentAsInput() throws IOException {
        ArgumentJsonDecoder<Color, Car> car = ArgumentJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        EnumJsonDecoder.of(Brand.class).required("brand"),
                        EnumJsonDecoder.of(Color.class).optional("color", color::resolve)));

        ArgumentJsonDecoder<Color, Factory> codec = ArgumentJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Factory::new,
                        car.with(color).required("car"),
                        Jdec.STRICT_INT.required("workers")));

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
        ArgumentJsonDecoder<Color, Car> car = ArgumentJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        EnumJsonDecoder.of(Brand.class).required("brand"),
                        EnumJsonDecoder.of(Color.class).optional("color", color::resolve)));

        ArgumentJsonDecoder<Color, Factory> codec = ArgumentJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Factory::new,
                        car.with(color).required("car"),
                        Jdec.STRICT_INT.required("workers")));

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
        ArgumentJsonDecoder<Color, Car> car = ArgumentJsonDecoder.fromValue(
                color -> ObjectJsonDecoder.group(Car::new,
                        EnumJsonDecoder.of(Brand.class).required("brand"),
                        EnumJsonDecoder.of(Color.class).optional("color", color::resolve)));

        ArgumentJsonDecoder<Color, Factory> codec = car
                .map(t -> new Factory(t, 900));

        var obj = TestUtils.deserialize(codec.with(Color.BLUE), "{ \"brand\": \"VOLVO\" }");
        assertEquals(900, obj.workers);
        assertEquals(Brand.VOLVO, obj.car.brand);
        assertEquals(Color.BLUE, obj.car.color);
    }

    @Test
    void orWithoutArgument() throws IOException {
        var codec = ArgumentJsonDecoder.<Brand, Dealership> fromTag(
                        brand -> ObjectJsonDecoder.group(Dealership::new,
                                brand,
                                Jdec.STRICT_INT.required("quantity"),
                                Jdec.STRICT_INT.required("rating")))
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
        var codec = ArgumentJsonDecoder.<Brand, Dealership> fromTag(
                        brand -> ObjectJsonDecoder.group(Dealership::new,
                                brand,
                                Jdec.STRICT_INT.required("quantity"),
                                Jdec.STRICT_INT.required("rating")))
                .orArg(JsonToken.NUMBER, ArgumentJsonDecoder.fromValue(brand ->
                        in -> new Dealership(brand.resolve(), in.nextInt(), 10)))
                .orArg(t -> t == JsonToken.BOOLEAN, ArgumentJsonDecoder.fromValue(brand ->
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
