package com.fiskmods.jdec.tag;

import com.fiskmods.jdec.decoder.ArgumentJsonDecoder;

import java.util.function.Supplier;

public class OptionalDependentJsonTag<K, V> extends DependentJsonTag<K, V> {
    private final Supplier<V> defaultValue;

    OptionalDependentJsonTag(ArgumentJsonDecoder<K, V> codec, String tagName, String afterTag, Supplier<V> defaultValue) {
        super(codec, tagName, afterTag);
        this.defaultValue = defaultValue;
    }

    @Override
    public EntryImpl createEntry() {
        EntryImpl e = new EntryImpl();
        e.setValue(defaultValue.get());
        return e;
    }
}
