package com.fiskmods.jdec.tag;

import com.fiskmods.jdec.decoder.JsonDecoder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

public class RequiredJsonTag<T> extends AbstractJsonTag<T> {
    private final JsonDecoder<T> codec;

    RequiredJsonTag(JsonDecoder<T> codec, String tagName) {
        super(tagName);
        this.codec = codec;
    }

    @Override
    protected boolean read(JsonReader in, String name, Entry<?>[] entries, EntryImpl e) throws IOException {
        if (tagName.equals(name)) {
            e.setValue(codec.read(in));
            return true;
        }
        return false;
    }
}
