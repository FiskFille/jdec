package com.fiskmods.jdec.tag;

import com.fiskmods.jdec.decoder.ArgumentJsonDecoder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

public class DependentJsonTag<K, V> extends AbstractJsonTag<V> {
    private final ArgumentJsonDecoder<K, V> codec;
    private final String afterTag;

    private int afterIndex;

    DependentJsonTag(ArgumentJsonDecoder<K, V> codec, String tagName, String afterTag) {
        super(tagName);
        this.codec = codec;
        this.afterTag = afterTag;
    }

    @Override
    public void initTag(JsonTag<?>[] tags) {
        if (tags == null) {
            throw new IllegalArgumentException("Unknown required tag '%s'".formatted(afterTag));
        }
        for (int i = 0; i < tags.length; ++i) {
            if (tags[i].matchesTag(afterTag)) {
                afterIndex = i;
                return;
            }
        }
        throw new IllegalArgumentException("Unknown required tag '%s'".formatted(afterTag));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean read(JsonReader in, String name, Entry<?>[] entries, EntryImpl e) throws IOException {
        if (!tagName.equals(name)) {
            return false;
        }
        if (afterIndex < 0 || entries[afterIndex].isAbsent()) {
            throw new IllegalStateException("Required tag '%s' must be set before '%s'"
                    .formatted(afterTag, tagName));
        }
        e.setValue(codec.with((K) entries[afterIndex].get()).read(in));
        return true;
    }
}
