package com.fiskmods.jdec;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.function.Supplier;

@FunctionalInterface
public interface JsonTag<T> {
    Entry<T> initEntry();

    static <T> JsonTag<T> required(JsonDecoder<T> codec, String tagName) {
        return new TagImpl<>(codec, tagName);
    }

    static <T> JsonTag<T> optional(JsonDecoder<T> codec, String tagName, Supplier<T> defaultValue) {
        return new TagImpl<>(codec, tagName) {
            @Override
            public Entry<T> initEntry() {
                return new EntryImpl().setValue(defaultValue.get());
            }
        };
    }

    static <T> JsonTag<T> optional(JsonDecoder<T> codec, String tagName, T defaultValue) {
        return new TagImpl<>(codec, tagName) {
            @Override
            public Entry<T> initEntry() {
                return new EntryImpl().setValue(defaultValue);
            }
        };
    }

    @FunctionalInterface
    interface Entry<T> extends Supplier<T> {
        default boolean tryRead(JsonReader in, String name) throws IOException {
            return false;
        }
    }

    class TagImpl<T> implements JsonTag<T> {
        private final JsonDecoder<T> codec;
        private final String tagName;

        private TagImpl(JsonDecoder<T> codec, String tagName) {
            this.codec = codec;
            this.tagName = tagName;
        }

        @Override
        public Entry<T> initEntry() {
            return new EntryImpl();
        }

        private boolean read(JsonReader in, String name, JsonTag.Entry<T> e) throws IOException {
            if (tagName.equals(name)) {
                ((EntryImpl) e).setValue(codec.read(in));
                return true;
            }

            return false;
        }

        class EntryImpl implements JsonTag.Entry<T> {
            private T value;
            private boolean isAbsent = true;

            @Override
            public T get() {
                if (isAbsent) {
                    throw new IllegalStateException("Missing required tag '%s'".formatted(tagName));
                }
                return value;
            }

            public Entry<T> setValue(T value) {
                this.value = value;
                this.isAbsent = false;
                return this;
            }

            @Override
            public boolean tryRead(JsonReader in, String name) throws IOException {
                return TagImpl.this.read(in, name, this);
            }
        }
    }
}
