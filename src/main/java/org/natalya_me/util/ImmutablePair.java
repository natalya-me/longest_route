package org.natalya_me.util;

import java.util.Objects;

public class ImmutablePair<K, V> {

    private final K key;
    private final V value;

    public ImmutablePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImmutablePair)) return false;
        ImmutablePair<?,?> other = (ImmutablePair<?, ?>) obj;
        return Objects.equals(key, other.key) && Objects.equals(value, other.value);
    }
}
