package com.fiskmods.jdec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@FunctionalInterface
public interface MapJsonDecoder<K, V> extends JsonDecoder<Map<K, V>> {
    static <K, V> MapJsonDecoder<K, V> from(Function<String, K> keyFunction, Function<K, JsonDecoder<V>> valueCodec) {
        return in -> {
            Map<K, V> map = new HashMap<>();
            in.beginObject();
            while (in.hasNext()) {
                K key = keyFunction.apply(in.nextName());
                map.put(key, valueCodec.apply(key).read(in));
            }
            in.endObject();
            return map;
        };
    }

    static <K, V> MapJsonDecoder<K, V> from(Function<String, K> keyFunction, JsonDecoder<V> valueCodec) {
        return from(keyFunction, (Function<K, JsonDecoder<V>>) k -> valueCodec);
    }
}
