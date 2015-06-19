package com.lfa.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * The Class ImmutableLinkedMap. A very precarious implementation of an
 * Immutable linked map, which maintains iteration order. Similar to
 * {@link LinkedHashMap} with building system similar to that of Google's
 * {@link ImmutableMap}.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 *
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
@ToString(callSuper = false, of = "map")
@EqualsAndHashCode(callSuper = false, of = { "map" })
public final class ImmutableLinkedMap<K, V> implements Map<K, V>, Serializable {

	private static final long serialVersionUID = -4634534523581400045L;

	private final LinkedHashMap<K, V> map;

	public static final class Builder<K, V> {

		private final LinkedHashMap<K, V> listMap;

		public Builder() {
			this.listMap = new LinkedHashMap<K, V>();
		}

		public final Builder<K, V> put(K key, V value) {
			listMap.put(key, value);
			return this;
		}

		public final Builder<K, V> addAll(Map<K, V> collection) {
			listMap.putAll(collection);
			return this;
		}

		public final ImmutableLinkedMap<K, V> build() {
			return new ImmutableLinkedMap<K, V>(new LinkedHashMap<>(listMap));
		}

	}

	private ImmutableLinkedMap(LinkedHashMap<K, V> map) {
		this.map = map;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Deprecated
	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet() {
		return ImmutableLinkedSet.<K> builder().addAll(map.keySet()).build();
	}

	@Override
	public Collection<V> values() {
		return ImmutableList.<V> builder().addAll(map.values()).build();
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return ImmutableLinkedSet.<Map.Entry<K, V>> builder().addAll(map.entrySet()).build();
	}

	public static <K, V> Builder<K, V> builder() {
		return new Builder<>();
	}

}
