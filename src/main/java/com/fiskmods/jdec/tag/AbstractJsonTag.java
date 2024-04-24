package com.fiskmods.jdec.tag;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public abstract class AbstractJsonTag<T> implements JsonTag<T> {
    protected final String tagName;

    public AbstractJsonTag(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public Entry<T> createEntry() {
        return new EntryImpl();
    }

    @Override
    public boolean matchesTag(String tagName) {
        return this.tagName.equals(tagName);
    }

    protected abstract boolean read(JsonReader in, String name, Entry<?>[] entries, EntryImpl e) throws IOException;

    protected class EntryImpl implements JsonTag.Entry<T> {
        private T value;
        private boolean isAbsent = true;

        @Override
        public T get() {
            if (isAbsent) {
                throw new IllegalStateException("Missing required tag '%s'".formatted(tagName));
            }
            return value;
        }

        @Override
        public boolean isAbsent() {
            return isAbsent;
        }

        public void setValue(T value) {
            this.value = value;
            this.isAbsent = false;
        }

        @Override
        public boolean tryRead(JsonReader in, String name, Entry<?>[] entries) throws IOException {
            return AbstractJsonTag.this.read(in, name, entries, this);
        }
    }
}
