package com.fiskmods.jdec.tag;

import com.fiskmods.jdec.decoder.JsonDecoder;

import java.util.function.Supplier;

public class OptionalJsonTag<T> extends RequiredJsonTag<T> {
    private final Supplier<T> defaultValue;

    OptionalJsonTag(JsonDecoder<T> codec, String tagName, Supplier<T> defaultValue) {
        super(codec, tagName);
        this.defaultValue = defaultValue;
    }

    @Override
    public EntryImpl createEntry() {
        EntryImpl e = new EntryImpl();
        e.setValue(defaultValue.get());
        return e;
    }
}
