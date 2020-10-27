package net.flintmc.util.csv;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CSV<K, V> {
    protected final Map<K, List<V>> data;

    CSV(Map<K, List<V>> data) {
        this.data = data;
    }

    public List<V> get(final K key) {
        return data.get(key);
    }

    public Map<V, V> relation(final K key, final K value) {
        List<V> keys = data.get(key), values = data.get(value);
        Map<V, V> map = new HashMap<>();

        if (keys.size() != values.size()) {
            throw new IllegalStateException();
        }

        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }

        return map;
    }

    public Collection<List<V>> values() {
        return data.values();
    }
}
