package com.fiskmods.jdec.tag;

import com.fiskmods.jdec.decoder.FunctionJsonDecoder;
import com.fiskmods.jdec.decoder.JsonDecoder;
import com.google.gson.stream.JsonReader;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.Supplier;

@FunctionalInterface
public interface JsonTag<T> {
    Entry<T> createEntry();

    default void initTag(@Nullable JsonTag<?>[] tags) {
    }

    default boolean matchesTag(String tagName) {
        return false;
    }

    static <T> JsonTag<T> required(String tagName, JsonDecoder<T> codec) {
        return new RequiredJsonTag<>(codec, tagName);
    }

    static <T> JsonTag<T> optionalGet(String tagName, JsonDecoder<T> codec, Supplier<T> defaultValue) {
        return new OptionalJsonTag<>(codec, tagName, defaultValue);
    }

    static <T> JsonTag<T> optional(String tagName, JsonDecoder<T> codec, T defaultValue) {
        return optionalGet(tagName, codec, () -> defaultValue);
    }

    static <T> JsonTag<T> optionalNullable(String tagName, JsonDecoder<T> codec) {
        return optional(tagName, codec, null);
    }

    static <T, U> JsonTag<T> requiredAfter(String tagName, String afterTag, FunctionJsonDecoder<U, T> codec) {
        return new DependentJsonTag<>(codec, tagName, afterTag);
    }

    static <T, U> JsonTag<T> optionalGetAfter(String tagName, String afterTag, FunctionJsonDecoder<U, T> codec,
                                              Supplier<T> defaultValue) {
        return new OptionalDependentJsonTag<>(codec, tagName, afterTag, defaultValue);
    }

    static <T, U> JsonTag<T> optionalAfter(String tagName, String afterTag, FunctionJsonDecoder<U, T> codec,
                                           T defaultValue) {
        return optionalGetAfter(tagName, afterTag, codec, () -> defaultValue);
    }

    static <T, U> JsonTag<T> optionalNullableAfter(String tagName, String afterTag, FunctionJsonDecoder<U, T> codec) {
        return optionalAfter(tagName, afterTag, codec, null);
    }

    @FunctionalInterface
    interface Entry<T> extends Supplier<T> {
        default boolean isAbsent() {
            return false;
        }

        default boolean tryRead(JsonReader in, String name, @Nullable Entry<?>[] entries) throws IOException {
            return false;
        }
    }
}
